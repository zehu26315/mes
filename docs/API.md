# MES System API 文档

> 制造执行系统（MES）后端 REST API。本文档由控制器与实体源码逐一核对生成，字段类型与源码一致。
>
> 13 业务模块 · 25 个 Controller · Session（Redis）认证 · 统一 `Result<T>` 响应包裹。

---

## 目录

| # | 模块 | 前缀 | Controller |
|---|------|------|-----------|
| — | 认证 | `/api/login`,`/api/logout`,`/api/auth` | AuthController |
| 1 | System | `/api/sys` | 8 个 Controller |
| 2 | Masterdata | `/api/masterdata` | 3 |
| 3 | Process | `/api/process` | 2 |
| 4 | Warehouse | `/api/warehouse` | 1 |
| 5 | Equipment | `/api/equipment` | 1 |
| 6 | Production | `/api/production` | 2 |
| 7 | Material | `/api`（无前缀） | 1 |
| 8 | Quality | `/api/quality` | 1 |
| 9 | Andon | `/api/andon` | 1 |
| 10 | Barcode | `/api/barcode` | 1 |
| 11 | Wages | `/api/wages` | 1 |
| 12 | Trace | `/api/trace` | 1 |
| 13 | Dashboard | `/api/dashboard` | 1 |
| — | WebSocket | `/ws-andon` | STOMP/SockJS |

---

## 通用规范

### 请求 / 响应格式
- 请求 `Content-Type: application/json`，编码 UTF-8。
- **所有** 响应统一包裹为 `Result<T>`，HTTP 状态码与 `code` 字段一般一致（拦截器返回 401；异常处理器不改 HTTP 状态码，仅设置 `code`）。

**`Result<T>`（成功）**
```json
{ "code": 200, "message": "success", "data": { } }
```

**`Result<PageResult<T>>`（分页）**
```json
{ "code": 200, "message": "success",
  "data": { "total": 100, "page": 1, "pageSize": 10, "rows": [ ] } }
```

**`Result<List<TreeNode<T>>>`（树形）**
```json
{ "code": 200, "message": "success",
  "data": [ { "node": { }, "children": [ { "node": { }, "children": [] } ] } ] }
```
> ⚠️ 树节点结构是 `{ node, children }`——实体字段在 `node` 内，`children` 是子树数组。实体本身**不含** `children` 字段。

### 错误响应

| HTTP | code | 触发场景 | message 来源 |
|------|------|---------|-------------|
| 401 | 401 | 未登录/会话过期（拦截器拦截） | `未登录或会话已过期` |
| 401 | 401 | `UnauthorizedException`（如 `/api/auth/user-info` 无 Session） | 异常消息 |
| — | 400 | `IllegalArgumentException`（参数校验、业务校验失败） | 异常消息 |
| — | 500 | 其它未捕获异常，或业务层 `Result.fail(message)` | 异常消息 / 业务消息 |

> ⚠️ **重要**：业务层的 `Result.fail(String message)` 默认 `code=500`（例如 Trace 查无数据时返回 `{"code":500,"message":"产品不存在"}`）。前端**不能**只用 HTTP 200 判断成功，必须判断 `data.code === 200`。

### 分页参数（约定）

绝大多数列表接口使用如下参数（各接口的确切参数以对应章节为准）：

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `keyword` | String | `""` | 模糊搜索（部分接口无此参数） |
| `status` | Integer / String | 无（可选） | 状态筛选，**类型随接口而定**（见下方警告） |
| `page` | int | `1` | 页码，从 1 开始 |
| `pageSize` | int | `10` | 每页条数（`/api/sys/logs` 默认为 **20**） |

> ⚠️ **status 类型不统一**：多数主数据接口 `status` 为 `Integer`（`1` 启用 / `0` 禁用）；仓库、设备、生产工单/派工、安灯记录等**工作流状态**接口 `status` 为 `String`。个别接口（如派工单列表、条码记录列表）查询参数是 `Integer` 而实体字段是 `String`——传参时以本文档标注为准。

### 字段通用约定
- `id`：`Long`，主键。
- `isDeleted`：`Integer`，`0`=正常 / `1`=已软删除。日志/流水/中间表/快照表无此字段（物理表现为只增不删）。
- `status`（Integer 型）：`1`=启用 / `0`=禁用。
- `createTime` / `updateTime`：`LocalDateTime`，序列化为 ISO-8601 字符串（如 `2026-07-14T12:34:56`）。
- 命名：Java/JSON 为 camelCase，数据库列为 snake_case。
- **不可通过 update 修改的 code 类字段**：`username`、`password`、各类 `*Code`（`roleCode`、`productCode`、`materialCode` 等）——PUT 时即使传入也会被忽略。
- **PUT `/{id}` 一律以路径 `id` 覆盖请求体 `id`**，请求体内 `id` 可省略。

---

## 认证

项目使用 **Redis Session** 认证，无 Spring Security 过滤链。除白名单外，所有 `/api/**` 请求由 `LoginInterceptor` 校验 Session。

**白名单（无需登录）**：`/api/login`、`/api/logout`、`/api/auth/user-info`。

### 登录 — `POST /api/login`

**请求体**（原始 JSON 对象，仅读取两个键，非 DTO）：
```json
{ "username": "admin", "password": "123456" }
```

**响应** `Result<LoginVO>`：
```json
{ "code": 200, "message": "success",
  "data": { "userId": 1, "username": "admin", "realName": "管理员", "deptId": 1, "roles": ["admin"] } }
```

**LoginVO 字段**

| 字段 | 类型 | 说明 |
|------|------|------|
| `userId` | Long | 用户 ID |
| `username` | String | 用户名 |
| `realName` | String | 真实姓名 |
| `deptId` | Long | 部门 ID |
| `roles` | List\<String\> | 角色编码列表，如 `["admin","operator"]` |

**错误**：用户名或密码为空 / 错误、账号禁用 → `code=400`。

### 登出 — `POST /api/logout`
销毁 Session，返回 `Result<Void>`（`data=null`）。

### 当前用户 — `GET /api/auth/user-info`
从 Session 恢复登录态，返回与登录相同的 `LoginVO`。未登录时抛 `UnauthorizedException` → `code=401`。
> 前端刷新页面后调用此接口恢复用户信息，无需重新登录。

---

## 1. System 模块

### 1.1 用户 — `/api/sys/users`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/sys/users` | `keyword`(String,默认`""`), `status`(Integer,可选), `page`(默认1), `pageSize`(默认10) | — | `PageResult<SysUser>` |
| POST | `/api/sys/users` | — | `SysUser` | `SysUser` |
| GET | `/api/sys/users/{id}` | — | — | `SysUser` |
| PUT | `/api/sys/users/{id}` | — | `SysUser` | `SysUser` |
| DELETE | `/api/sys/users/{id}` | — | — | `Void`（软删除） |
| POST | `/api/sys/users/{id}/roles` | — | `List<Long>`（roleIds） | `Void` |

**SysUser**：`id:Long`, `username:String`, `password:String`, `realName:String`, `phone:String`, `email:String`, `deptId:Long`, `status:Integer`, `isDeleted:Integer`, `createTime`, `updateTime`
> `username`、`password` 为 code 类字段，update 不可改。返回体虽含 `password` 字段但通常为空/加密串，前端勿展示。

### 1.2 角色 — `/api/sys/roles`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/sys/roles` | `keyword`, `status`, `page`, `pageSize` | — | `PageResult<SysRole>` |
| GET | `/api/sys/roles/all` | — | — | `List<SysRole>`（全量，缓存） |
| POST | `/api/sys/roles` | — | `SysRole` | `SysRole` |
| GET | `/api/sys/roles/{id}` | — | — | `SysRole` |
| PUT | `/api/sys/roles/{id}` | — | `SysRole` | `SysRole` |
| DELETE | `/api/sys/roles/{id}` | — | — | `Void` |

**SysRole**：`id:Long`, `roleName:String`, `roleCode:String`, `description:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`（`roleCode` 不可改）

### 1.3 菜单 — `/api/sys/menus`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/sys/menus` | `keyword`, `status`, `page`, `pageSize` | — | `PageResult<SysMenu>` |
| GET | `/api/sys/menus/tree` | — | — | `List<TreeNode<SysMenu>>`（缓存） |
| POST | `/api/sys/menus` | — | `SysMenu` | `SysMenu` |
| GET | `/api/sys/menus/{id}` | — | — | `SysMenu` |
| PUT | `/api/sys/menus/{id}` | — | `SysMenu` | `SysMenu` |
| DELETE | `/api/sys/menus/{id}` | — | — | `Void` |

**SysMenu**：`id:Long`, `parentId:Long`, `menuName:String`, `menuCode:String`, `menuType:String`, `url:String`, `icon:String`, `sortOrder:Integer`, `visible:Integer`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

### 1.4 部门 — `/api/sys/departments`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/sys/departments` | `keyword`, `status`, `page`, `pageSize` | — | `PageResult<SysDepartment>` |
| GET | `/api/sys/departments/tree` | — | — | `List<TreeNode<SysDepartment>>` |
| POST | `/api/sys/departments` | — | `SysDepartment` | `SysDepartment` |
| GET | `/api/sys/departments/{id}` | — | — | `SysDepartment` |
| PUT | `/api/sys/departments/{id}` | — | `SysDepartment` | `SysDepartment` |
| DELETE | `/api/sys/departments/{id}` | — | — | `Void` |

**SysDepartment**：`id:Long`, `parentId:Long`, `deptName:String`, `deptCode:String`, `leader:String`, `phone:String`, `sortOrder:Integer`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

### 1.5 系统配置 — `/api/sys/configs`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/sys/configs` | — | — | `List<SysConfig>`（全量，缓存，无分页） |
| GET | `/api/sys/configs/{id}` | — | — | `SysConfig` |
| PUT | `/api/sys/configs/{id}` | — | `SysConfig` | `SysConfig` |

> ⚠️ 仅 **查 + 改**，**无创建（POST）、无删除（DELETE）**。

**SysConfig**：`id:Long`, `configName:String`, `configKey:String`, `configValue:String`, `configType:String`, `description:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

### 1.6 操作日志 — `/api/sys/logs`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/sys/logs` | `page`(默认1), `pageSize`(**默认20**) | `PageResult<SysOperationLog>` |

> 只读列表，**无 keyword/status 参数**，`pageSize` 默认 20。

**SysOperationLog**（无 `isDeleted`/`updateTime`）：`id:Long`, `userId:Long`, `username:String`, `operation:String`, `method:String`, `params:String`, `ip:String`, `status:Integer`, `errorMsg:String`, `duration:Long`, `createTime`

### 1.7 公告通知 — `/api/sys/notice`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/sys/notice` | `keyword`, `noticeType`(Integer,可选), `status`(Integer,可选), `page`, `pageSize` | — | `PageResult<SysNotice>` |
| POST | `/api/sys/notice` | — | `SysNotice` | `SysNotice` |
| GET | `/api/sys/notice/{id}` | — | — | `SysNotice` |
| PUT | `/api/sys/notice/{id}` | — | `SysNotice` | `SysNotice` |
| DELETE | `/api/sys/notice/{id}` | — | — | `Void`（软删除） |

**SysNotice**：`id:Long`, `noticeTitle:String`, `noticeType:Integer`, `noticeContent:String`, `createBy:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`
> `noticeType`：`0`=公告 / `1`=通知 / `2`=提醒。

### 1.8 登录日志 — `/api/sys/login-log`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/sys/login-log` | `loginName`(String,默认`""`), `status`(Integer,可选), `page`, `pageSize` | `PageResult<SysLoginLog>` |
| GET | `/api/sys/login-log/{id}` | — | `SysLoginLog` |
| DELETE | `/api/sys/login-log/{id}` | — | `Void`（软删除） |

> ⚠️ 列表过滤参数是 `loginName`（**不是** `keyword`）。登录日志由系统在登录时自动写入，**无创建接口**。

**SysLoginLog**：`id:Long`, `loginName:String`, `ipAddress:String`, `loginLocation:String`, `browserType:String`, `os:String`, `status:Integer`, `msg:String`, `isDeleted`, `createTime`, `updateTime`
> `status`：`0`=成功 / `1`=失败。

---

## 2. Masterdata 模块 — `/api/masterdata`

三个资源均为标准 CRUD，列表参数统一为 `keyword`(默认`""`), `status`(Integer,可选), `page`, `pageSize`。

| 资源 | 基路径 |
|------|--------|
| 产品 | `/api/masterdata/products` |
| 物料 | `/api/masterdata/materials` |
| 物料分类 | `/api/masterdata/material-categories` |

每个资源：`GET /`(分页) · `POST /` · `GET /{id}` · `PUT /{id}` · `DELETE /{id}`，返回对应实体或 `PageResult<实体>`。

**MdProduct**：`id:Long`, `productCode:String`, `productName:String`, `productSpec:String`, `productType:String`, `unit:String`, `category:String`, `voltage:String`, `capacity:String`, `weight:BigDecimal`, `sizeDiameter:BigDecimal`, `sizeHeight:BigDecimal`, `routeId:Long`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`（`productCode` 不可改）

**MdMaterial**：`id:Long`, `materialCode:String`, `materialName:String`, `materialSpec:String`, `categoryId:Long`, `unit:String`, `safetyStock:BigDecimal`, `currentStock:BigDecimal`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`（`materialCode` 不可改）

**MdMaterialCategory**：`id:Long`, `parentId:Long`, `categoryName:String`, `categoryCode:String`, `sortOrder:Integer`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

---

## 3. Process 模块 — `/api/process`

### 3.1 工艺路线 — `/api/process/routes`

标准 CRUD：`GET /`(参数 `keyword`,`status`,`page`,`pageSize`) · `POST /` · `GET /{id}` · `PUT /{id}` · `DELETE /{id}`，实体 `ProcessRoute`。

**ProcessRoute**：`id:Long`, `routeCode:String`, `routeName:String`, `productCode:String`, `description:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

### 3.2 工序（嵌套于路线）

| 方法 | 路径 | 请求体 | 返回 |
|------|------|--------|------|
| GET | `/api/process/routes/{routeId}/steps` | — | `List<ProcessStep>`（按 sortOrder） |
| POST | `/api/process/routes/{routeId}/steps` | `ProcessStep` | `ProcessStep` |
| GET | `/api/process/steps/{id}` | — | `ProcessStep` |
| PUT | `/api/process/steps/{id}` | `ProcessStep` | `ProcessStep` |
| DELETE | `/api/process/steps/{id}` | — | `Void` |
| PUT | `/api/process/steps/{id}/sort` | `{ "sortOrder": int }`（原始 Map） | `ProcessStep` |

**ProcessStep**（**无 `status` 字段**）：`id:Long`, `routeId:Long`, `stepCode:String`, `stepName:String`, `stepType:String`, `sortOrder:Integer`, `workCenter:String`, `standardTime:BigDecimal`, `yieldRate:BigDecimal`, `sopFile:String`, `prevStepId:Long`, `nextStepId:Long`, `isDeleted`, `createTime`, `updateTime`

---

## 4. Warehouse 模块 — `/api/warehouse`

> ⚠️ 本模块所有单据实体的 `status` 字段为 **String**（工作流状态），查询参数 `status`/`type` 亦为 String。

### 4.1 仓库 — `/api/warehouse/warehouses`

| 方法 | 路径 | 请求体 | 返回 |
|------|------|--------|------|
| GET | `/api/warehouse/warehouses` | — | `List<WhWarehouse>`（全量） |
| POST | `/api/warehouse/warehouses` | `WhWarehouse` | `WhWarehouse` |
| GET | `/api/warehouse/warehouses/{id}` | — | `WhWarehouse` |
| PUT | `/api/warehouse/warehouses/{id}` | `WhWarehouse` | `WhWarehouse` |
| DELETE | `/api/warehouse/warehouses/{id}` | — | `Void` |

**WhWarehouse**：`id:Long`, `whCode:String`, `whName:String`, `whType:String`, `location:String`, `manager:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`
> 注意：仓库主档 `status` 是 `Integer`；下方单据 `status` 是 `String`。

### 4.2 入库 — `/api/warehouse/inbounds`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/warehouse/inbounds` | `type`(String,可选), `warehouseId`(Long,可选), `status`(String,可选), `page`, `pageSize` | — | `PageResult<WhInbound>` |
| POST | `/api/warehouse/inbounds` | — | `WhInbound` | `WhInbound` |
| GET | `/api/warehouse/inbounds/{id}` | — | — | `WhInbound` |

**WhInbound**：`id:Long`, `inboundNo:String`, `inboundType:String`, `warehouseId:Long`, `materialCode:String`, `materialName:String`, `qty:BigDecimal`, `unit:String`, `batchNo:String`, `supplier:String`, `sourceOrderNo:String`, `status:String`, `operator:String`, `inboundTime:LocalDateTime`, `isDeleted`, `createTime`, `updateTime`

### 4.3 出库 — `/api/warehouse/outbounds`

同入库结构：`GET /`(参数 `type`,`warehouseId`,`status`,`page`,`pageSize`) · `POST /` · `GET /{id}`。

**WhOutbound**：`id:Long`, `outboundNo:String`, `outboundType:String`, `warehouseId:Long`, `materialCode:String`, `materialName:String`, `qty:BigDecimal`, `unit:String`, `batchNo:String`, `targetOrderNo:String`, `status:String`, `operator:String`, `outboundTime:LocalDateTime`, `isDeleted`, `createTime`, `updateTime`

### 4.4 调拨 — `/api/warehouse/transfers`

`GET /`(参数 `status`,`fromWarehouseId`,`toWarehouseId`,`page`,`pageSize`) · `POST /` · `GET /{id}`。

**WhTransfer**：`id:Long`, `transferNo:String`, `fromWarehouseId:Long`, `toWarehouseId:Long`, `materialCode:String`, `materialName:String`, `qty:BigDecimal`, `unit:String`, `batchNo:String`, `status:String`, `operator:String`, `transferTime:LocalDateTime`, `isDeleted`, `createTime`, `updateTime`

### 4.5 盘点 — `/api/warehouse/checks`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/warehouse/checks` | `warehouseId`(Long,可选), `status`(String,可选), `page`, `pageSize` | — | `PageResult<WhCheck>` |
| POST | `/api/warehouse/checks` | — | `WhCheck` | `WhCheck` |
| GET | `/api/warehouse/checks/{id}` | — | — | `WhCheck` |
| PUT | `/api/warehouse/checks/{id}/confirm` | — | — | `WhCheck`（确认盘点） |

**WhCheck**：`id:Long`, `checkNo:String`, `warehouseId:Long`, `checkType:String`, `checkDate:LocalDate`, `materialCode:String`, `bookQty:BigDecimal`, `actualQty:BigDecimal`, `diffQty:BigDecimal`, `diffReason:String`, `status:String`, `checker:String`, `isDeleted`, `createTime`, `updateTime`

---

## 5. Equipment 模块 — `/api/equipment`

### 5.1 设备分类 — `/api/equipment/categories`

| 方法 | 路径 | 请求体 | 返回 |
|------|------|--------|------|
| GET | `/api/equipment/categories` | — | `List<TreeNode<EquipCategory>>`（**树形**） |
| POST | `/api/equipment/categories` | `EquipCategory` | `EquipCategory` |
| GET | `/api/equipment/categories/{id}` | — | `EquipCategory` |
| PUT | `/api/equipment/categories/{id}` | `EquipCategory` | `EquipCategory` |
| DELETE | `/api/equipment/categories/{id}` | — | `Void` |

**EquipCategory**（无 `status`）：`id:Long`, `categoryName:String`, `categoryCode:String`, `parentId:Long`, `description:String`, `isDeleted`, `createTime`, `updateTime`

### 5.2 制造商 — `/api/equipment/manufacturers`

`GET /`(全量 `List<EquipManufacturer>`) · `POST /` · `PUT /{id}` · `DELETE /{id}`。

**EquipManufacturer**：`id:Long`, `manufacturerName:String`, `manufacturerCode:String`, `contactPerson:String`, `contactPhone:String`, `address:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

### 5.3 设备资产 — `/api/equipment/assets`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/equipment/assets` | `keyword`(String,默认`""`), `status`(**String**,可选), `categoryId`(Long,可选), `productionLine`(String,可选), `page`, `pageSize` | — | `PageResult<EquipAsset>` |
| POST | `/api/equipment/assets` | — | `EquipAsset` | `EquipAsset` |
| GET | `/api/equipment/assets/{id}` | — | — | `EquipAsset` |
| PUT | `/api/equipment/assets/{id}` | — | `EquipAsset` | `EquipAsset` |
| DELETE | `/api/equipment/assets/{id}` | — | — | `Void` |
| PUT | `/api/equipment/assets/{id}/status` | — | `{ "status":.., "reason":.., "operator":.. }`（原始 Map） | `EquipAsset`（自动记录 EquipStatusLog） |

**EquipAsset**（`status` 为 **String**）：`id:Long`, `equipCode:String`, `equipName:String`, `equipModel:String`, `equipSpec:String`, `categoryId:Long`, `manufacturerId:Long`, `departmentId:Long`, `productionLine:String`, `workCenter:String`, `location:String`, `serialNo:String`, `status:String`, `purchaseDate:LocalDate`, `warrantyEnd:LocalDate`, `serviceLife:Integer`, `supplier:String`, `isDeleted`, `createTime`, `updateTime`

### 5.4 保养计划 — `/api/equipment/maintenance-plans`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/equipment/maintenance-plans` | `equipId`(Long,可选), `status`(String,可选), `startDate`(String,可选), `endDate`(String,可选), `page`, `pageSize` | — | `PageResult<EquipMaintenancePlan>` |
| POST | `/api/equipment/maintenance-plans` | — | `EquipMaintenancePlan` | `EquipMaintenancePlan` |
| PUT | `/api/equipment/maintenance-plans/{id}` | — | `EquipMaintenancePlan` | `EquipMaintenancePlan` |
| DELETE | `/api/equipment/maintenance-plans/{id}` | — | — | `Void` |

**EquipMaintenancePlan**：`id:Long`, `planNo:String`, `equipId:Long`, `maintenanceType:String`, `planDate:LocalDate`, `content:String`, `responsiblePerson:String`, `status:String`, `isDeleted`, `createTime`, `updateTime`

### 5.5 保养记录 — `/api/equipment/maintenance-records`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/equipment/maintenance-records` | `equipId`(Long,可选), `startDate`(String,可选), `endDate`(String,可选), `page`, `pageSize` | — | `PageResult<EquipMaintenanceRecord>` |
| POST | `/api/equipment/maintenance-records` | — | `EquipMaintenanceRecord` | `EquipMaintenanceRecord`（若含 `planId` 自动完成对应计划） |

**EquipMaintenanceRecord**（无 `status`/`isDeleted`）：`id:Long`, `planId:Long`, `equipId:Long`, `executeDate:LocalDate`, `result:String`, `executor:String`, `remark:String`, `cost:BigDecimal`, `createTime`, `updateTime`

### 5.6 维修 — `/api/equipment/repairs`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/equipment/repairs` | `equipId`(Long,可选), `status`(String,可选), `page`, `pageSize` | — | `PageResult<EquipRepair>` |
| POST | `/api/equipment/repairs` | — | `EquipRepair` | `EquipRepair`（自动设 `status=REPORTED`） |
| PUT | `/api/equipment/repairs/{id}` | — | `EquipRepair` | `EquipRepair` |
| DELETE | `/api/equipment/repairs/{id}` | — | — | `Void` |
| PUT | `/api/equipment/repairs/{id}/complete` | — | — | `EquipRepair`（自动计算停机时间 downtime） |

**EquipRepair**：`id:Long`, `repairNo:String`, `equipId:Long`, `faultId:Long`, `faultDesc:String`, `repairType:String`, `reporter:String`, `repairPerson:String`, `startTime:LocalDateTime`, `endTime:LocalDateTime`, `downtime:Long`, `repairContent:String`, `partsUsed:String`, `cost:BigDecimal`, `status:String`, `isDeleted`, `createTime`, `updateTime`

> **EquipStatusLog**（设备状态变更日志，由 `/assets/{id}/status` 自动写入，无独立接口）：`id:Long`, `equipId:Long`, `fromStatus:String`, `toStatus:String`, `reason:String`, `operator:String`, `createTime`

---

## 6. Production 模块 — `/api/production`

> 由两个 Controller 组成：工单/派工在 `/api/production/*`；报工/在制品/返修在 `/api/production/mgmt/*`。

### 6.1 工单 — `/api/production/work-orders`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/production/work-orders` | `keyword`(String,默认`""`), `status`(**String**,可选), `page`, `pageSize` | — | `PageResult<ProdWorkOrder>` |
| POST | `/api/production/work-orders` | — | `ProdWorkOrder` | `ProdWorkOrder` |
| GET | `/api/production/work-orders/{id}` | — | — | `ProdWorkOrder` |
| PUT | `/api/production/work-orders/{id}` | — | `ProdWorkOrder` | `ProdWorkOrder` |
| DELETE | `/api/production/work-orders/{id}` | — | — | `Void` |
| PUT | `/api/production/work-orders/{id}/status` | `status`(String,**必填**,query) | — | `Void` |

> ⚠️ **工单 `status` 存中文标签**，取值仅限：`待生产` / `生产中` / `已完成` / `暂停`（见 `WorkOrderStatus` 常量）。状态变更用 query 参数：`PUT /work-orders/{id}/status?status=生产中`。

**ProdWorkOrder**：`id:Long`, `orderNo:String`, `productCode:String`, `productName:String`, `quantity:BigDecimal`, `completedQty:BigDecimal`, `defectiveQty:BigDecimal`, `startDate:LocalDate`, `endDate:LocalDate`, `currentProcess:String`, `status:String`, `workshop:String`, `productionLine:String`, `shift:String`, `priority:Integer`, `sourceOrderNo:String`, `planId:Long`, `isDeleted`, `createTime`, `updateTime`

### 6.2 派工单 — `/api/production/dispatch-orders`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/production/dispatch-orders` | `workOrderId`(Long,可选), `status`(**Integer**,可选), `page`, `pageSize` | — | `PageResult<ProdDispatchOrder>` |
| POST | `/api/production/dispatch-orders` | — | `ProdDispatchOrder` | `ProdDispatchOrder` |
| PUT | `/api/production/dispatch-orders/{id}` | — | `ProdDispatchOrder` | `ProdDispatchOrder` |

> ⚠️ 列表查询参数 `status` 为 `Integer`，但实体 `ProdDispatchOrder.status` 为 `String`——按此文档传参。

**ProdDispatchOrder**：`id:Long`, `dispatchNo:String`, `workOrderId:Long`, `productCode:String`, `quantity:BigDecimal`, `processCode:String`, `processName:String`, `workCenter:String`, `equipmentId:Long`, `assignedTo:String`, `status:String`, `planStartTime:LocalDateTime`, `planEndTime:LocalDateTime`, `actualStartTime:LocalDateTime`, `actualEndTime:LocalDateTime`, `isDeleted`, `createTime`, `updateTime`

### 6.3 报工 — `/api/production/mgmt/reportings`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/production/mgmt/reportings` | `workOrderId`(Long,可选), `processCode`(String,可选), `page`, `pageSize` | — | `PageResult<ProdReporting>` |
| POST | `/api/production/mgmt/reportings` | — | `ProdReporting` | `ProdReporting`（自动更新 WIP 数量） |
| GET | `/api/production/mgmt/completions` | `page`, `pageSize` | — | `PageResult<ProdReporting>`（已完成报工） |

**ProdReporting**：`id:Long`, `workOrderId:Long`, `dispatchId:Long`, `processCode:String`, `processName:String`, `workCenter:String`, `equipmentId:Long`, `worker:String`, `shift:String`, `reportType:String`, `totalQty:BigDecimal`, `goodQty:BigDecimal`, `defectQty:BigDecimal`, `defectReason:String`, `startTime:LocalDateTime`, `endTime:LocalDateTime`, `duration:Integer`, `remark:String`, `isDeleted`, `createTime`, `updateTime`

### 6.4 在制品 WIP — `/api/production/mgmt/wip`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/production/mgmt/wip` | `page`, `pageSize` | `PageResult<ProdWip>` |
| GET | `/api/production/mgmt/wip/process/{processCode}` | — | `List<ProdWip>`（按工序） |

**ProdWip**（无 `status`/`isDeleted`/`createTime`）：`id:Long`, `workOrderId:Long`, `productCode:String`, `productName:String`, `processCode:String`, `processName:String`, `workCenter:String`, `wipQty:BigDecimal`, `holdQty:BigDecimal`, `updateTime`

### 6.5 返修单 — `/api/production/mgmt/repair-orders`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/production/mgmt/repair-orders` | `page`, `pageSize` | — | `PageResult<ProdRepairOrder>` |
| POST | `/api/production/mgmt/repair-orders` | — | `ProdRepairOrder` | `ProdRepairOrder` |
| PUT | `/api/production/mgmt/repair-orders/{id}/complete` | `result`(String,**必填**,query) | — | `Void` |

**ProdRepairOrder**：`id:Long`, `repairNo:String`, `workOrderId:Long`, `productCode:String`, `quantity:BigDecimal`, `defectDesc:String`, `repairProcess:String`, `status:String`, `result:String`, `isDeleted`, `createTime`, `updateTime`

---

## 7. Material 模块 — `/api`（无模块前缀）

> ⚠️ 本模块各资源直接挂在 `/api/*` 下，**无** `/api/material` 前缀。

### 7.1 领料 — `/api/requisitions`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/requisitions` | `status`(String,可选), `workOrderId`(Long,可选), `materialCode`(String,可选), `page`, `pageSize` | — | `PageResult<MatRequisition>` |
| POST | `/api/requisitions` | — | `MatRequisition` | `MatRequisition` |
| GET | `/api/requisitions/{id}` | — | — | `MatRequisition` |
| PUT | `/api/requisitions/{id}/approve` | — | — | `Void`（审批） |

**MatRequisition**：`id:Long`, `reqNo:String`, `workOrderId:Long`, `materialId:Long`, `materialCode:String`, `materialName:String`, `reqQty:BigDecimal`, `actualQty:BigDecimal`, `unit:String`, `warehouse:String`, `location:String`, `batchNo:String`, `status:String`, `applicant:String`, `approver:String`, `issueTime:LocalDateTime`, `isDeleted`, `createTime`, `updateTime`

### 7.2 退料 — `/api/returns`

`GET /`(参数 `status`,`workOrderId`,`materialCode`,`page`,`pageSize`) · `POST /` · `GET /{id}`（无审批接口）。

**MatReturn**：`id:Long`, `returnNo:String`, `workOrderId:Long`, `reqId:Long`, `materialId:Long`, `materialCode:String`, `materialName:String`, `returnQty:BigDecimal`, `unit:String`, `returnReason:String`, `warehouse:String`, `status:String`, `applicant:String`, `isDeleted`, `createTime`, `updateTime`

### 7.3 补料 — `/api/supplements`

`GET /`(参数同上) · `POST /` · `GET /{id}` · `PUT /{id}/approve`（审批）。

**MatSupplement**：`id:Long`, `suppNo:String`, `workOrderId:Long`, `materialId:Long`, `materialCode:String`, `materialName:String`, `suppQty:BigDecimal`, `unit:String`, `suppReason:String`, `status:String`, `applicant:String`, `approver:String`, `isDeleted`, `createTime`, `updateTime`

### 7.4 库存 — `/api/inventory`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/inventory` | `materialId`(Long,可选), `materialCode`(String,可选), `warehouse`(String,可选), `page`, `pageSize` | `PageResult<MatInventory>` |
| GET | `/api/inventory/low-stock` | `page`, `pageSize` | `PageResult<MatInventory>`（低库存预警） |

**MatInventory**（无 `isDeleted`/`createTime`）：`id:Long`, `materialId:Long`, `materialCode:String`, `materialName:String`, `warehouse:String`, `location:String`, `batchNo:String`, `qty:BigDecimal`, `lockedQty:BigDecimal`, `unit:String`, `supplier:String`, `productionDate:LocalDate`, `expiryDate:LocalDate`, `updateTime`

### 7.5 库存流水 — `/api/inventory-logs`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/inventory-logs` | `materialId`(Long,可选), `materialCode`(String,可选), `changeType`(String,可选), `page`, `pageSize` | `PageResult<MatInventoryLog>` |

**MatInventoryLog**（只增，无 `isDeleted`/`updateTime`）：`id:Long`, `materialId:Long`, `materialCode:String`, `changeType:String`, `changeQty:BigDecimal`, `beforeQty:BigDecimal`, `afterQty:BigDecimal`, `referenceNo:String`, `operator:String`, `createTime`

---

## 8. Quality 模块 — `/api/quality`

### 8.1 检验分类 — `/api/quality/categories`

标准 CRUD：`GET /`(参数 `keyword`(默认`""`),`page`,`pageSize`) · `POST /` · `GET /{id}` · `PUT /{id}` · `DELETE /{id}`。

**QualityInspectionCategory**：`id:Long`, `categoryName:String`, `categoryCode:String`, `description:String`, `isDeleted`, `createTime`, `updateTime`

### 8.2 检验项目 — `/api/quality/items`

CRUD，列表参数 `keyword`(默认`""`), `categoryId`(Long,可选), `page`, `pageSize`。

**QualityInspectionItem**（限值为 **String**）：`id:Long`, `itemCode:String`, `itemName:String`, `categoryId:Long`, `unit:String`, `standardValue:String`, `upperLimit:String`, `lowerLimit:String`, `inspectionType:String`, `testMethod:String`, `isDeleted`, `createTime`, `updateTime`

### 8.3 检验标准 — `/api/quality/standards`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/quality/standards` | `keyword`(默认`""`), `status`(Integer,可选), `page`, `pageSize` | — | `PageResult<QualityInspectionStandard>` |
| POST | `/api/quality/standards` | — | `QualityInspectionStandard` | `QualityInspectionStandard` |
| GET | `/api/quality/standards/{id}` | — | — | `QualityInspectionStandard` |
| PUT | `/api/quality/standards/{id}` | — | `QualityInspectionStandard` | `QualityInspectionStandard` |
| DELETE | `/api/quality/standards/{id}` | — | — | `Void` |
| GET | `/api/quality/standards/{id}/details` | — | — | `List<QualityStandardDetail>` |
| POST | `/api/quality/standards/{id}/details` | — | `List<QualityStandardDetail>` | `Void`（批量保存明细） |

**QualityInspectionStandard**：`id:Long`, `standardNo:String`, `standardName:String`, `productCode:String`, `customerName:String`, `sampleSize:Integer`, `aqlLevel:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

**QualityStandardDetail**（限值为 **BigDecimal**，无审计字段）：`id:Long`, `standardId:Long`, `itemId:Long`, `standardValue:BigDecimal`, `upperLimit:BigDecimal`, `lowerLimit:BigDecimal`

### 8.4 检验订单 — `/api/quality/orders`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/quality/orders` | `keyword`(默认`""`), `orderType`(String,可选), `result`(String,可选), `page`, `pageSize` | — | `PageResult<QualityInspectionOrder>` |
| POST | `/api/quality/orders` | — | `QualityInspectionOrder` | `QualityInspectionOrder` |
| GET | `/api/quality/orders/{id}` | — | — | `QualityInspectionOrder` |
| PUT | `/api/quality/orders/{id}` | — | `QualityInspectionOrder` | `QualityInspectionOrder` |
| DELETE | `/api/quality/orders/{id}` | — | — | `Void` |
| PUT | `/api/quality/orders/{id}/result` | — | `{ "result":String, "defectQty":int }`（原始 Map，均可空） | `Void` |
| GET | `/api/quality/orders/{id}/results` | — | — | `List<QualityInspectionResult>` |
| POST | `/api/quality/orders/{id}/results` | — | `List<QualityInspectionResult>` | `Void`（批量保存结果） |

**QualityInspectionOrder**：`id:Long`, `orderNo:String`, `orderType:String`, `sourceOrderNo:String`, `productCode:String`, `productName:String`, `batchNo:String`, `sampleQty:Integer`, `defectQty:Integer`, `result:String`, `inspector:String`, `inspectionTime:LocalDateTime`, `remark:String`, `isDeleted`, `createTime`, `updateTime`

**QualityInspectionResult**（无审计字段）：`id:Long`, `orderId:Long`, `itemId:Long`, `itemName:String`, `standardValue:BigDecimal`, `upperLimit:BigDecimal`, `lowerLimit:BigDecimal`, `actualValue:BigDecimal`, `unit:String`, `isPass:Integer`

### 8.5 首末件检验 — `/api/quality/first-last`

CRUD，列表参数 `keyword`(默认`""`), `workOrderId`(Long,可选), `result`(String,可选), `page`, `pageSize`。

**QualityFirstLast**：`id:Long`, `orderNo:String`, `workOrderId:Long`, `inspectionType:String`, `productCode:String`, `processCode:String`, `equipmentId:Long`, `result:String`, `inspector:String`, `inspectionTime:LocalDateTime`, `measurementData:String`, `isDeleted`, `createTime`, `updateTime`

### 8.6 巡检 — `/api/quality/patrols`

CRUD，列表参数 `keyword`(默认`""`), `workOrderId`(Long,可选), `result`(String,可选), `page`, `pageSize`。

**QualityPatrol**：`id:Long`, `orderNo:String`, `workOrderId:Long`, `productionLine:String`, `processCode:String`, `checkInterval:Integer`, `sampleSize:Integer`, `result:String`, `inspector:String`, `inspectionTime:LocalDateTime`, `findDesc:String`, `isDeleted`, `createTime`, `updateTime`

### 8.7 缺陷记录 — `/api/quality/defects`

CRUD，列表参数 `keyword`(默认`""`), `workOrderId`(Long,可选), `defectType`(String,可选), `page`, `pageSize`。

**QualityDefectRecord**（只增，无 `isDeleted`/`updateTime`/`status`）：`id:Long`, `workOrderId:Long`, `productCode:String`, `processCode:String`, `defectType:String`, `defectLocation:String`, `defectDesc:String`, `defectQty:Integer`, `rootCause:String`, `correctiveAction:String`, `reporter:String`, `createTime`

---

## 9. Andon 模块 — `/api/andon`

> ⚠️ 所有增/改/删操作会向 STOMP 主题 `/topic/andon` 广播（见 WebSocket 章节）。

### 9.1 安灯类型 — `/api/andon/types`

CRUD，列表参数 `keyword`(默认`""`), `page`, `pageSize`。

**AndonType**：`id:Long`, `typeName:String`, `typeCode:String`, `handleMode:String`, `lightColor:String`, `description:String`, `isDeleted`, `createTime`, `updateTime`

### 9.2 安灯配置 — `/api/andon/configs`

CRUD，列表参数 `keyword`(默认`""`), `andonTypeId`(Long,可选), `status`(Integer,可选), `page`, `pageSize`。

**AndonConfig**：`id:Long`, `configName:String`, `andonTypeId:Long`, `workCenter:String`, `triggerCondition:String`, `notifyUsers:String`, `escalationTime:Integer`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

### 9.3 异常原因 — `/api/andon/reasons`

CRUD + 树，列表参数 `keyword`(默认`""`), `andonTypeId`(Long,可选), `status`(Integer,可选), `page`, `pageSize`。
- `GET /api/andon/reasons/tree` → `List<TreeNode<AndonReason>>`（**树形**）。

**AndonReason**：`id:Long`, `reasonName:String`, `reasonCode:String`, `andonTypeId:Long`, `parentId:Long`, `sortOrder:Integer`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

### 9.4 异常记录 — `/api/andon/records`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/andon/records` | `keyword`(默认`""`), `andonTypeId`(Long,可选), `status`(**String**,可选), `workCenter`(String,可选), `startTime`(String,可选), `endTime`(String,可选), `page`, `pageSize` | — | `PageResult<AndonRecord>` |
| POST | `/api/andon/records` | — | `AndonRecord` | `AndonRecord` |
| GET | `/api/andon/records/{id}` | — | — | `AndonRecord` |
| GET | `/api/andon/records/active` | — | — | `List<AndonRecord>`（活跃异常） |
| PUT | `/api/andon/records/{id}/status` | — | `{ "status":.., "handler":.., "resolveDesc":.. }`（原始 Map） | `AndonRecord` |
| DELETE | `/api/andon/records/{id}` | — | — | `Void` |

**AndonRecord**：`id:Long`, `andonTypeId:Long`, `reasonId:Long`, `workCenter:String`, `productionLine:String`, `equipmentId:Long`, `workOrderId:Long`, `description:String`, `status:String`, `severity:String`, `reporter:String`, `handler:String`, `reportTime:LocalDateTime`, `confirmTime:LocalDateTime`, `resolveTime:LocalDateTime`, `resolveDesc:String`, `duration:Integer`, `isDeleted`, `createTime`, `updateTime`

---

## 10. Barcode 模块 — `/api/barcode`

四个基础资源均为标准 CRUD，列表参数统一 `keyword`(默认`""`), `status`(**Integer**,可选), `page`, `pageSize`：

| 资源 | 基路径 |
|------|--------|
| 条码类型 | `/api/barcode/types` |
| 条码规则 | `/api/barcode/rules` |
| 条码模板 | `/api/barcode/templates` |
| 条码应用 | `/api/barcode/applications` |

**条码记录**（无标准 CRUD，特殊路径）：

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| POST | `/api/barcode/generate` | — | `BarcodeRecord` | `BarcodeRecord`（自动生成条码号） |
| GET | `/api/barcode/records` | `keyword`(默认`""`), `status`(**Integer**,可选), `page`, `pageSize` | — | `PageResult<BarcodeRecord>` |
| GET | `/api/barcode/records/{id}` | — | — | `BarcodeRecord` |

> ⚠️ 记录列表 `status` 查询参数为 `Integer`，但实体 `BarcodeRecord.status` 为 `String`。

**BarcodeType**：`id:Long`, `typeName:String`, `typeCode:String`, `description:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

**BarcodeRule**：`id:Long`, `ruleName:String`, `ruleCode:String`, `barcodeTypeId:Long`, `prefix:String`, `dateFormat:String`, `serialLength:Integer`, `separator:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

**BarcodeTemplate**：`id:Long`, `templateName:String`, `templateCode:String`, `templateContent:String`, `width:BigDecimal`, `height:BigDecimal`, `description:String`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

**BarcodeApplication**：`id:Long`, `applicationName:String`, `ruleId:Long`, `templateId:Long`, `productCode:String`, `triggerPoint:String`, `printQty:Integer`, `status:Integer`, `isDeleted`, `createTime`, `updateTime`

**BarcodeRecord**（无 `isDeleted`，`status` 为 String）：`id:Long`, `barcodeNo:String`, `barcodeTypeId:Long`, `ruleId:Long`, `productCode:String`, `workOrderId:Long`, `batchNo:String`, `status:String`, `printCount:Integer`, `createTime`, `updateTime`

---

## 11. Wages 模块 — `/api/wages`

| 方法 | 路径 | 参数 | 请求体 | 返回 |
|------|------|------|--------|------|
| GET | `/api/wages/records` | `keyword`(默认`""`), `page`, `pageSize` | — | `PageResult<WageRecord>` |
| POST | `/api/wages/records` | — | `WageRecord` | `WageRecord`（自动计算 `totalAmount = quantity × unitPrice`） |
| GET | `/api/wages/records/{id}` | — | — | `WageRecord` |
| PUT | `/api/wages/records/{id}` | — | `WageRecord` | `WageRecord`（自动重算 `totalAmount`） |
| DELETE | `/api/wages/records/{id}` | — | — | `Void` |

**WageRecord**：`id:Long`, `employeeName:String`, `employeeCode:String`, `workOrderId:Long`, `processCode:String`, `quantity:BigDecimal`, `unitPrice:BigDecimal`, `totalAmount:BigDecimal`, `recordDate:LocalDate`, `isDeleted`, `createTime`, `updateTime`
> `totalAmount` 由后端计算，前端无需传入（传了也会被覆盖）。

---

## 12. Trace 模块 — `/api/trace`

| 方法 | 路径 | 请求体 | 返回 |
|------|------|--------|------|
| GET | `/api/trace/product/{barcode}` | — | `Map`（正向追溯，见下） |
| GET | `/api/trace/material/{batchNo}` | — | `Map`（反向追溯，见下） |
| POST | `/api/trace/products` | `TraceProduct` | `TraceProduct` |
| POST | `/api/trace/material-batches` | `TraceMaterialBatch` | `TraceMaterialBatch` |
| POST | `/api/trace/product-materials` | `TraceProductMaterial` | `TraceProductMaterial` |

**正向追溯 `GET /product/{barcode}`**（查无返回 `{"code":500,"message":"产品不存在"}`）：
```json
{ "product": { /* TraceProduct */ },
  "materials": [ { "productMaterial": { }, "materialBatch": { } } ] }
```

**反向追溯 `GET /material/{batchNo}`**（查无返回 `{"code":500,"message":"物料批次不存在"}`）：
```json
{ "materialBatch": { /* TraceMaterialBatch */ },
  "products": [ { "productMaterial": { }, "product": { } } ] }
```

**TraceProduct**（无 `status`/`isDeleted`/`updateTime`）：`id:Long`, `barcodeNo:String`, `productCode:String`, `productName:String`, `workOrderId:Long`, `productionLine:String`, `equipmentId:Long`, `processCode:String`, `worker:String`, `shift:String`, `batchNo:String`, `productionDate:LocalDate`, `productionTime:LocalDateTime`, `inspectionResult:String`, `createTime`

**TraceMaterialBatch**：`id:Long`, `batchNo:String`, `materialId:Long`, `materialCode:String`, `materialName:String`, `supplier:String`, `supplierBatch:String`, `receivedDate:LocalDate`, `qty:BigDecimal`, `inspectionResult:String`, `certificateNo:String`, `createTime`

**TraceProductMaterial**：`id:Long`, `barcodeNo:String`, `materialBatchNo:String`, `materialCode:String`, `usedQty:BigDecimal`, `processCode:String`, `createTime`

---

## 13. Dashboard 模块 — `/api/dashboard`

> 返回值均为 `Map` / `List<Map>`（跨模块聚合，无实体）。缓存 TTL 60 秒。

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/dashboard/summary` | — | `Map`（综合摘要） |
| GET | `/api/dashboard/recent-orders` | `limit`(int,默认10) | `List<Map>`（最近工单） |
| GET | `/api/dashboard/recent-alarms` | `limit`(int,默认10) | `List<Map>`（最近报警） |
| GET | `/api/dashboard/line-status` | — | `List<Map>`（产线状态，按产线聚合） |
| GET | `/api/dashboard/today-stats` | — | `Map`（今日统计） |
| GET | `/api/dashboard/equip-status` | — | `Map`（设备状态汇总） |
| GET | `/api/dashboard/energy-summary` | — | `Map`（今日能耗） |

**`/summary`**：`totalWorkOrders:long`, `completedWorkOrders:long`, `activeAlarms:long`, `totalEquipment:long`, `oeeAverage:BigDecimal`, `yieldRate:BigDecimal`, `wipTotal:BigDecimal`, `lowStockCount:long`

**`/recent-orders`** 每项：`id`, `orderNo`, `productName`, `quantity`, `completedQty`, `status`, `productionLine`, `createTime`

**`/recent-alarms`** 每项：`id`, `andonTypeId`, `workCenter`, `productionLine`, `status`, `severity`, `description`, `reportTime`, `duration`

**`/line-status`** 每项（按 `lineId` 排序）：`lineId:String`, `activeOrders:Long`, `runningEquipment:long`, `totalEquipment:long`, `wipQty:BigDecimal`, `activeAlarms:Long`

**`/today-stats`**：`todayOutput:BigDecimal`, `defectRate:BigDecimal`(百分比), `inspectionPassRate:BigDecimal`(百分比), `activeOrders:long`

**`/equip-status`**：固定键 `RUNNING`, `IDLE`, `MAINTENANCE`, `BROKEN`（均为 Long），另可能追加其它大写状态键（未知状态归入 `UNKNOWN`）。

**`/energy-summary`**：`electricity:BigDecimal`, `water:BigDecimal`, `gas:BigDecimal`, `compressedAir:BigDecimal`, `steam:BigDecimal`

---

## WebSocket 实时推送 — `/ws-andon`

| 项目 | 值 |
|------|-----|
| 协议 | STOMP over SockJS |
| 连接端点 | `/ws-andon`（`allowedOriginPatterns=*`） |
| 订阅主题 | `/topic/andon` |
| 客户端→服务端前缀 | `/app`（当前无 `@MessageMapping` 处理器） |
| 触发时机 | 安灯 类型/配置/原因/记录 的增/改/删 及记录状态变更 |

**推送消息格式**（`Map`，键顺序不保证）：
```json
{
  "type":   "type" | "config" | "reason" | "record",
  "action": "create" | "update" | "delete",
  "id":     1,
  "time":   "2026-07-14T12:34:56.789"
}
```

**前端集成示例**：
```javascript
const socket = new SockJS('/ws-andon');
const stomp = Stomp.over(socket);
stomp.connect({}, () => {
  stomp.subscribe('/topic/andon', (msg) => {
    const data = JSON.parse(msg.body);
    // data.type: 'type'|'config'|'reason'|'record'
    // data.action: 'create'|'update'|'delete'
    // data.id, data.time
  });
});
```

---

## 前端对接注意事项（易踩坑清单）

1. **成功判定用 `data.code === 200`**，不要只看 HTTP 状态码——业务失败（如 Trace 查无数据）返回 HTTP 200 但 `code=500`。
2. **`status` 类型不统一**：主数据/条码/安灯配置为 `Integer`；仓库单据、设备、生产工单/派工、安灯记录为 `String`。派工单列表、条码记录列表的 **查询参数** 是 `Integer`，但实体字段是 `String`。
3. **工单状态是中文标签**：`待生产`/`生产中`/`已完成`/`暂停`，状态变更走 `?status=` query 参数而非请求体。
4. **原始 Map 请求体**（非实体）：`PUT /process/steps/{id}/sort`、`PUT /equipment/assets/{id}/status`、`PUT /quality/orders/{id}/result`、`PUT /andon/records/{id}/status`、`POST /login`。
5. **Material 模块无 `/api/material` 前缀**，资源直接挂 `/api/requisitions` 等。
6. **登录日志列表用 `loginName` 过滤**，不是 `keyword`；操作日志无过滤参数且 `pageSize` 默认 20。
7. **SysConfig 无创建/删除接口**；退料无审批接口；条码记录无更新/删除接口。
8. **code 类字段（`*Code`、`username`、`password`）update 时被忽略**，请通过独立业务流程处理。
9. **树接口返回 `{ node, children }` 结构**，实体字段在 `node` 内。
10. **计算字段由后端生成**：工资 `totalAmount`、维修 `downtime`、报工触发的 WIP 更新——前端传入无效。

---

## 端点统计

| 模块 | Controller 数 | 端点数 |
|------|--------------|--------|
| 认证（Auth） | 1 | 3 |
| System | 8 | 33 |
| Masterdata | 3 | 15 |
| Process | 2 | 11 |
| Warehouse | 1 | 18 |
| Equipment | 1 | 24 |
| Production | 2 | 17 |
| Material | 1 | 14 |
| Quality | 1 | 40 |
| Andon | 1 | 22 |
| Barcode | 1 | 23 |
| Wages | 1 | 5 |
| Trace | 1 | 5 |
| Dashboard | 1 | 7 |
| WebSocket | — | 1（`/ws-andon`） |
