# MES System API 文档

> 制造执行系统（MES）后端 REST API。本文档由实际控制器源码逐一核对生成。
>
> 10 业务模块 · 14 个 Controller · Session（Redis）认证 · 统一 `Result<T>` 响应包裹。

---

## 目录

| # | 模块 | 前缀 | Controller |
|---|------|------|-----------|
| — | 认证 | `/api/login`, `/api/logout`, `/api/auth/user-info` | AuthController |
| 1 | System | `/api/sys/*` | 4 个 Controller |
| 2 | Production | `/api/production/*` | ProductionController（统一，7 子资源） |
| 3 | Process | `/api/process/*` | ProcessController（统一，4 子资源） |
| 4 | Equipment | `/api/equipment/*` | EquipmentController（统一，4 子资源） |
| 5 | Quality | `/api/quality/*` | QualityController |
| 6 | Andon | `/api/andon/*` | AndonController（2 子资源） |
| 7 | Barcode | `/api/barcode/*` | BarcodeController（2 子资源） |
| 8 | Wages | `/api/wages` | WagesController |
| 9 | Trace | `/api/trace/*` | TraceController（2 子资源） |
| 10 | Dashboard | `/api/dashboard` | DashboardController |
| — | WebSocket | `/ws-andon` | STOMP/SockJS |

---

## 通用规范

### 请求 / 响应格式
- 请求 `Content-Type: application/json`，编码 UTF-8。
- **所有** 响应统一包裹为 `Result<T>`。

**`Result<T>`（成功）**
```json
{ "code": 200, "message": "success", "data": { } }
```

**`Result<PageResult<T>>`（分页）**
```json
{ "code": 200, "message": "success",
  "data": { "total": 100, "page": 1, "pageSize": 10, "rows": [ ] } }
```

### 错误响应

| HTTP | code | 触发场景 |
|------|------|---------|
| 401 | 401 | 未登录/会话过期（拦截器拦截） |
| 401 | 401 | `UnauthorizedException` |
| 200 | 400 | `IllegalArgumentException`（参数校验失败） |
| 200 | 500 | 未捕获异常或 `Result.fail(message)` |

> **重要**：前端必须判断 `data.code === 200`，不能只看 HTTP 状态码。

### 分页参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `keyword` | String | `""` | 模糊搜索（部分接口使用特定字段名） |
| `status` | Integer | 无 | 状态筛选 |
| `page` | int | `1` | 页码，从 1 开始 |
| `pageSize` | int | `10` | 每页条数（操作日志默认 20） |

### 字段通用约定
- `id`：`Long`，主键
- `isDeleted`：`Integer`，`0`=正常 / `1`=已软删除
- `status`：`Integer`，`1`=启用 / `0`=禁用（生产类实体使用不同编码，见各实体标注）
- `createTime` / `updateTime`：`LocalDateTime`，ISO-8601 格式
- 命名：camelCase（JSON）/ snake_case（数据库列）
- **不可通过 update 修改的 code 类字段**：`username`、`password`、各类 `*Code`

---

## 认证

白名单（无需登录）：`/api/login`、`/api/logout`。

### 登录 — `POST /api/login`

请求体：`{ "username": "admin", "password": "123456" }`

响应 `Result<LoginVO>`：
```json
{ "code": 200, "message": "success",
  "data": { "userId": 1, "username": "admin", "nickname": "管理员" } }
```

**LoginVO**：`userId:Long`, `username:String`, `nickname:String`

### 登出 — `POST /api/logout`
返回 `Result<Void>`。

### 当前用户 — `GET /api/auth/user-info`
从 Session 恢复登录态，返回 `LoginVO`。

---

## 1. System 模块 — `/api/sys`

### 1.1 用户 — `/api/sys/users`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/sys/users` | `keyword`(String,默认""), `status`(Integer,可选), `page`(默认1), `pageSize`(默认10) | `PageResult<SysUser>` |
| POST | `/api/sys/users` | `SysUser` | `SysUser` |
| GET | `/api/sys/users/{id}` | — | `SysUser` |
| PUT | `/api/sys/users/{id}` | `SysUser` | `SysUser` |
| DELETE | `/api/sys/users/{id}` | — | `Void` |

**SysUser**：`id`, `username`, `password`, `nickname`, `phone`, `email`, `lastLoginTime`, `status`, `isDeleted`, `createTime`, `updateTime`

### 1.2 公告通知 — `/api/sys/notice`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/sys/notice` | `keyword`(String,默认""), `noticeType`(Integer,可选), `status`(Integer,可选), `page`(默认1), `pageSize`(默认10) | `PageResult<SysNotice>` |
| POST | `/api/sys/notice` | `SysNotice` | `SysNotice` |
| GET | `/api/sys/notice/{id}` | — | `SysNotice` |
| PUT | `/api/sys/notice/{id}` | `SysNotice` | `SysNotice` |
| DELETE | `/api/sys/notice/{id}` | — | `Void` |

**SysNotice**：`id`, `noticeTitle`, `noticeType`(0=公告/1=通知/2=提醒), `noticeContent`, `createBy`, `status`, `isDeleted`, `createTime`, `updateTime`

### 1.3 登录日志 — `/api/sys/login-log`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/sys/login-log` | `loginName`(String,默认""), `status`(Integer,可选), `page`(默认1), `pageSize`(默认10) | `PageResult<SysLoginLog>` |
| GET | `/api/sys/login-log/{id}` | — | `SysLoginLog` |
| DELETE | `/api/sys/login-log/{id}` | — | `Void` |

> 无创建接口，由系统自动写入。过滤参数为 `loginName`（不是 `keyword`）。

**SysLoginLog**：`id`, `loginName`, `ipAddress`, `loginLocation`, `browserType`, `os`, `status`(0=成功/1=失败), `msg`, `isDeleted`, `createTime`, `updateTime`

### 1.4 操作日志 — `/api/sys/logs`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/sys/logs` | `operationName`, `operationType`, `operationIp`, `startTime`, `endTime`(均String,可选), `page`(默认1), `pageSize`(默认20) | `PageResult<SysOperationLog>` |
| GET | `/api/sys/logs/{id}` | — | `SysOperationLog` |
| DELETE | `/api/sys/logs/{id}` | — | `Void` |
| DELETE | `/api/sys/logs/batch` | `[1, 2, 3]`（Long 数组） | `Void` |
| GET | `/api/sys/logs/stats` | — | `Map`（统计信息） |
| GET | `/api/sys/logs/clean-suggest` | `keepDays`(默认90) | `Long`（可删除条数） |

> **batch 删除请求体为 `List<Long>`（裸数组），不是 `{ids: [...]}`。**

**SysOperationLog**：`id`, `operationIp`, `operLocation`, `method`, `args`, `operationName`, `operationType`, `returnValue`, `isDeleted`, `createTime`, `updateTime`

---

## 2. Production 模块 — `/api/production`

统一控制器 `ProductionController`，7 个子资源。所有实体标准 CRUD：`GET /{plural}`（列表）+ `GET /{plural}/{id}` + `POST /{plural}` + `PUT /{plural}/{id}` + `DELETE /{plural}/{id}`。

### 2.1 生产订单 — `/api/production/orders`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/production/orders` | `keyword`(String,可选), `status`(Integer,可选), `page`(默认1), `pageSize`(默认10) | `PageResult<ProductionOrder>` |
| GET | `/api/production/orders/{id}` | — | `ProductionOrder` |
| POST | `/api/production/orders` | `ProductionOrder` | `ProductionOrder` |
| PUT | `/api/production/orders/{id}` | `ProductionOrder` | `ProductionOrder` |
| DELETE | `/api/production/orders/{id}` | — | `Void` |
| GET | `/api/production/dashboard` | — | `Map`（stats, dailyOutput, lineOutput） |

**ProductionOrder**：`id`, `orderNo`, `productModel`, `batchNo`, `plannedQty`, `completedQty`, `productionLine`, `status`(0=待排产/1=生产中/2=已完成), `deliveryDate`, `createBy`, `remark`, `isDeleted`, `createTime`, `updateTime`

### 2.2 生产工单 — `/api/production/work-orders`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/production/work-orders` | `keyword`(String,可选,别名workOrderNo), `materialId`(Long,可选), `status`(Integer,可选), `sourceType`(Integer,可选), `priority`(Integer,可选), `page`(默认1), `pageSize`(默认10) | `PageResult<ProWorkOrder>` |

**ProWorkOrder**：`id`, `workOrderNo`, `materialId`, `planQuantity`(BigDecimal), `actualQuantity`(BigDecimal), `planStartTime`, `planEndTime`, `actualStartTime`, `actualEndTime`, `routeId`, `sourceType`(1-4), `status`(1=待生产/2=进行中/3=已完成/4=已关闭), `priority`(0=普通/1=紧急/2=关键), `remark`, `createBy`, `isDeleted`, `createTime`, `updateTime`

### 2.3 生产任务 — `/api/production/tasks`

| 参数 | `taskNo`(String,可选), `workOrderId`(Long,可选), `productionLine`(String,可选), `status`(Integer,可选), `sourceType`(Integer,可选), `page`, `pageSize` |

**ProTask**：`id`, `taskNo`, `workOrderId`, `dispatchId`, `productionLine`, `planQuantity`(BigDecimal), `completeQuantity`(BigDecimal), `defectQuantity`(BigDecimal), `sourceType`, `status`(1-4), `startTime`, `endTime`, `isDeleted`, `createTime`, `updateTime`

### 2.4 派工单 — `/api/production/dispatches`

| 参数 | `dispatchNo`(String,可选), `workOrderId`(Long,可选), `productionLine`(String,可选), `status`(Integer,可选), `assigneeId`(Long,可选), `page`, `pageSize` |

**ProDispatch**：`id`, `dispatchNo`, `workOrderId`, `operationId`, `productionLine`, `planQuantity`(BigDecimal), `actualQuantity`(BigDecimal), `planStartTime`, `planEndTime`, `assigneeId`, `status`(1-4), `isDeleted`, `createTime`, `updateTime`

### 2.5 完工单 — `/api/production/completions`

| 参数 | `completionNo`(String,可选), `workOrderId`(Long,可选), `materialId`(Long,可选), `status`(Integer,可选), `page`, `pageSize` |

**ProCompletion**：`id`, `completionNo`, `workOrderId`, `materialId`, `planQuantity`(BigDecimal), `completedQuantity`(BigDecimal), `qualifiedQuantity`(BigDecimal), `defectQuantity`(BigDecimal), `warehouseId`, `locationId`, `completionTime`, `inspectorId`, `status`(1-3), `isDeleted`, `createTime`, `updateTime`

### 2.6 返修单 — `/api/production/repairs`

| 参数 | `repairNo`(String,可选), `workOrderId`(Long,可选), `status`(Integer,可选), `page`, `pageSize` |

**ProRepair**：`id`, `repairNo`, `workOrderId`, `completionId`, `materialId`, `barcode`, `repairQuantity`(BigDecimal), `repairReason`, `repairType`(1=内部返修/2=外部返修), `assigneeId`, `status`(1=待返修/2=返修中/3=返修完成/4=已报废), `startTime`, `endTime`, `isDeleted`, `createTime`, `updateTime`

### 2.7 报工记录 — `/api/production/reports`

| 参数 | `orderId`(Long,可选), `page`, `pageSize` |

**ProReport**：`id`, `reportNo`, `orderId`, `taskId`, `operationId`, `processName`, `worker`, `workstation`, `workerId`, `reportType`, `reportQuantity`(BigDecimal), `qualifiedQuantity`(BigDecimal), `defectQuantity`(BigDecimal), `defectReason`, `equipmentId`, `barcode`, `reportDate`, `shift`, `reportTime`, `isDeleted`, `createTime`, `updateTime`

---

## 3. Process 模块 — `/api/process`

统一控制器，4 个子资源。

### 3.1 工序 — `/api/process/operations`

| 参数 | `operationCode`(String), `operationName`(String), `operationType`(Integer), `status`(Integer), `keyword`(String), `page`, `pageSize` |

**PrcOperation**：`id`, `operationCode`, `operationName`, `operationType`(1=加工/2=检验/3=包装/4=组装), `sopId`, `description`, `status`(0=禁用/1=启用), `isDeleted`, `createTime`, `updateTime`

### 3.2 工艺路线 — `/api/process/routes`

| 参数 | `routeCode`(String), `routeName`(String), `keyword`(String,模糊匹配编码和名称), `materialId`(Long), `status`(Integer), `page`, `pageSize` |

**PrcRoute**：`id`, `routeCode`, `routeName`, `materialId`, `version`, `description`, `productModel`, `processIds`, `status`(0=禁用/1=启用), `isDeleted`, `createTime`, `updateTime`

### 3.3 路线明细 — `/api/process/route-details`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/process/route-details` | `routeId`(Long,可选), `page`(默认1), `pageSize`(默认10) | `PageResult<PrcRouteDetail>` |

**PrcRouteDetail**：`id`, `routeId`, `operationId`, `seqNo`, `standardTime`(BigDecimal), `price`(BigDecimal), `prevOpId`, `nextOpId`, `isDeleted`, `createTime`, `updateTime`

### 3.4 SOP 文件 — `/api/process/sops`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/process/sops` | `operationId`(Long,可选), `keyword`(String,可选), `page`(默认1), `pageSize`(默认10) | `PageResult<PrcSop>` |

**PrcSop**：`id`, `sopName`, `operationId`, `fileName`, `fileType`, `fileUrl`, `fileSize`, `autoPlay`(0/1), `sortOrder`, `isDeleted`, `createTime`, `updateTime`

---

## 4. Equipment 模块 — `/api/equipment`

统一控制器，4 个子资源。CRUD 模式同上。

### 4.1 设备台账 — `/api/equipment/ledgers`

| 参数 | `keyword`(String), `equipmentCode`(String), `equipmentName`(String), `category`(String), `status`(Integer), `page`, `pageSize` |

**EquipLedger**：`id`, `equipmentCode`, `equipmentName`, `category`, `equipmentModel`, `manufacturer`, `workshop`, `serialNo`, `purchaseDate`, `warrantyDate`, `location`, `productionLine`, `ratedPower`(BigDecimal), `status`(0=闲置/1=运行/2=维修/3=报废), `remark`, `isDeleted`, `createTime`, `updateTime`

### 4.2 保养 — `/api/equipment/maintenance`

| 参数 | `equipmentId`(Long), `maintenanceType`(String), `status`(Integer), `page`, `pageSize` |

**EquipMaintenance**：`id`, `taskNo`, `equipmentId`, `maintenanceType`, `planDate`, `actualDate`, `maintenanceContent`, `operator`, `result`, `status`(0=计划中/1=执行中/2=已完成), `remark`, `isDeleted`, `createTime`, `updateTime`

### 4.3 巡检 — `/api/equipment/inspections`

| 参数 | `equipmentId`(Long), `result`(Integer), `page`, `pageSize` |

**EquipInspection**：`id`, `inspectionNo`, `equipmentId`, `inspectionDate`, `inspector`, `inspectionItems`, `result`(0=正常/1=异常), `remark`, `isDeleted`, `createTime`, `updateTime`

### 4.4 维修 — `/api/equipment/repairs`

| 参数 | `equipmentId`(Long), `status`(String), `page`, `pageSize` |
| 额外 | `PUT /api/equipment/repairs/{id}/complete` — 完成维修 |

**EquipRepair**：`id`, `repairNo`, `equipmentId`, `faultType`, `faultReason`, `failureDesc`, `reporter`, `repairPerson`, `reportTime`, `repairTime`, `startTime`, `endTime`, `repairContent`, `status`(0=已报修/1=维修中/2=已修复/3=无法修复), `remark`, `isDeleted`, `createTime`, `updateTime`

---

## 5. Quality 模块 — `/api/quality`

### 质量检验 — `/api/quality/inspections`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/quality/inspections` | `inspectionType`(String,可选), `page`, `pageSize` | `PageResult<QualityInspection>` |
| GET | `/api/quality/stats` | — | `Map` |

**QualityInspection**：`id`, `reportNo`, `orderId`, `productModel`, `batchNo`, `inspectionType`, `sampleQty`, `defectQty`, `result`(0=合格/1=不合格), `inspector`, `remark`, `isDeleted`, `createTime`, `updateTime`

---

## 6. Andon 模块 — `/api/andon`

所有增/改/删操作广播 WebSocket 消息到 `/topic/andon`。

### 6.1 安灯配置 — `/api/andon/configs`

| 方法 | 路径 | 参数 | 返回 |
|------|------|------|------|
| GET | `/api/andon/configs` | `keyword`(String), `andonType`(String,可选), `status`(Integer), `page`, `pageSize` | `PageResult<AndonConfig>` |

**AndonConfig**：`id`, `andonType`, `andonName`, `handleType`(1=仅记录/2=自行处理/3=协助处理), `notifyUsers`, `notifyMethod`(1=系统消息/2=看板显示/3=消息+看板), `productionLine`, `status`(0=禁用/1=启用), `isDeleted`, `createTime`, `updateTime`

### 6.2 安灯记录 — `/api/andon/records`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/andon/records` | 分页列表（keyword, andonConfigId, status, productionLine, startTime, endTime） |
| GET | `/api/andon/records/{id}` | 按 ID 查询 |
| POST | `/api/andon/records` | 创建（广播 create） |
| PUT | `/api/andon/records/{id}` | 全量更新（广播 update） |
| GET | `/api/andon/records/active` | 活跃记录列表（status=1,2） |
| DELETE | `/api/andon/records/{id}` | 删除（广播 delete） |

**AndonRecord**：`id`, `andonConfigId`, `reasonId`, `productionLine`, `workstation`, `exceptionType`, `initiatorId`, `handlerId`, `operationId`, `equipmentId`, `description`, `reportUser`, `handleUser`, `initTime`, `confirmTime`, `handleTime`, `handleResult`, `status`(Integer: 1=未确认/2=已确认处理中/3=已处理/4=已关闭), `isDeleted`, `createTime`, `updateTime`

---

## 7. Barcode 模块 — `/api/barcode`

### 7.1 条码规则 — `/api/barcode/rules`

标准 CRUD，`keyword`/`status`/`page`/`pageSize` 分页参数。

**BarcodeRule**：`id`, `ruleCode`, `ruleName`, `ruleType`, `prefix`, `dateFormat`, `serialLength`, `status`(0/1), `isDeleted`, `createTime`, `updateTime`

### 7.2 条码记录 — `/api/barcode/records`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/barcode/records` | 分页列表（keyword, status） |
| GET | `/api/barcode/records/{id}` | 按 ID 查询 |
| POST | `/api/barcode/generate` | 生成条码（`BarcodeRecord`） |

**BarcodeRecord**：`id`, `barcode`, `productModel`, `batchNo`, `orderNo`, `productionLine`, `printTime`, `printUser`, `ruleId`, `isDeleted`, `createTime`, `updateTime`

---

## 8. Wages 模块 — `/api/wages`

标准 CRUD（没有子路径前缀，直接在 `/api/wages` 下）。

| 参数 | `worker`(String), `page`, `pageSize` |

**PieceWage**：`id`, `worker`, `processName`, `unitPrice`(BigDecimal), `completedQty`, `totalWage`(BigDecimal,自动计算=unitPrice×completedQty), `wageDate`, `status`(0=待结算/1=已结算), `isDeleted`, `createTime`, `updateTime`

---

## 9. Trace 模块 — `/api/trace`

### 9.1 物料追溯 — `/api/trace/materials`
标准 CRUD，`orderId`(Long) + `page`/`pageSize`。

### 9.2 产品追溯 — `/api/trace/products`
标准 CRUD，`keyword`(String) + `page`/`pageSize`。

---

## 10. Dashboard — `/api/dashboard`

| GET | `/api/dashboard` | 聚合 production + quality 统计数据 |

---

## WebSocket — `/ws-andon`

| 项目 | 值 |
|------|-----|
| 协议 | STOMP over SockJS |
| 端点 | `/ws-andon` |
| 订阅 | `/topic/andon` |

推送消息格式：
```json
{ "type": "config|record", "action": "create|update|delete", "id": 1, "time": "2026-07-15T12:34:56.789" }
```

---

## 前端对接注意事项

1. **成功判定用 `data.code === 200`**，不要只看 HTTP 状态码
2. **`status` 类型统一为 Integer**：所有实体的 status 字段均为 Integer 类型
3. **POST 创建、PUT 更新时传实体 JSON**，后端自动处理 ID 覆盖和不可改字段忽略
4. **计件工资 `totalWage` 由后端计算**，前端传入值会被覆盖
5. **code 类字段 update 时被忽略**：`*Code`、`username`、`password`
6. **条码生成接口为 `POST /api/barcode/generate`**
7. **登录日志列表用 `loginName` 过滤**（不是 `keyword`），无创建接口
8. **操作日志 batch 删除请求体为 `[1, 2, 3]`**（裸数组）
9. **安灯模块增/改/删会 WebSocket 推送**到 `/topic/andon`
10. **LoginVO 仅含 `userId`、`username`、`nickname`**

---

## 端点统计

| 模块 | 子资源数 | 端点数（约） |
|------|---------|-------------|
| 认证 | — | 3 |
| System | 4 | 22 |
| Production | 7 | 37 |
| Process | 4 | 20 |
| Equipment | 4 | 20 |
| Quality | 1 | 6 |
| Andon | 2 | 12 |
| Barcode | 2 | 9 |
| Wages | 1 | 5 |
| Trace | 2 | 10 |
| Dashboard | — | 1 |
| **合计** | **27** | **145** |
