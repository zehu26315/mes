# MES System API 文档

> 290 Java 源文件 | 69 XML Mapper | 16 业务模块 | 248 REST 端点

## 通用规范

### 请求格式
- Content-Type: `application/json`
- 字符编码: UTF-8

### 响应格式

**成功响应** (`Result<T>`):
```json
{ "code": 200, "message": "success", "data": { ... } }
```

**分页响应** (`Result<PageResult<T>>`):
```json
{ "code": 200, "message": "success", "data": { "total": 100, "page": 1, "pageSize": 10, "rows": [...] } }
```

**错误响应**:
```json
{ "code": 400, "message": "错误描述", "data": null }
{ "code": 500, "message": "Internal server error", "data": null }
```

### 分页参数

所有列表接口支持统一分页参数：
| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `keyword` | String | `""` | 模糊搜索关键词 |
| `status` | Integer | — | 状态筛选（可选） |
| `page` | int | `1` | 页码（从1开始） |
| `pageSize` | int | `10` | 每页条数 |

### 树形响应 (`Result<List<TreeNode<T>>>`):
```json
{ "code": 200, "data": [{ "node": { ... }, "children": [{ "node": { ... }, "children": [] }] }] }
```

---

## 1. System 模块 (`/api/sys`)

### 用户管理 — `/api/sys/users`

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/sys/users` | 分页列表 (keyword, status, page, pageSize) |
| `POST` | `/api/sys/users` | 创建用户 (body: SysUser) |
| `GET` | `/api/sys/users/{id}` | 查询用户 |
| `PUT` | `/api/sys/users/{id}` | 更新用户 (body: SysUser) |
| `DELETE` | `/api/sys/users/{id}` | 删除用户（软删除） |
| `POST` | `/api/sys/users/{id}/roles` | 分配角色 (body: List\<Long\> roleIds) |

### 角色管理 — `/api/sys/roles`

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/sys/roles` | 分页列表 |
| `GET` | `/api/sys/roles/all` | 全量列表（缓存） |
| `POST` | `/api/sys/roles` | 创建角色 |
| `GET` | `/api/sys/roles/{id}` | 查询角色 |
| `PUT` | `/api/sys/roles/{id}` | 更新角色 |
| `DELETE` | `/api/sys/roles/{id}` | 删除角色 |

### 菜单管理 — `/api/sys/menus`

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/sys/menus` | 分页列表 |
| `GET` | `/api/sys/menus/tree` | **树形结构**（缓存） |
| `POST` | `/api/sys/menus` | 创建菜单 |
| `GET` | `/api/sys/menus/{id}` | 查询菜单 |
| `PUT` | `/api/sys/menus/{id}` | 更新菜单 |
| `DELETE` | `/api/sys/menus/{id}` | 删除菜单 |

### 部门管理 — `/api/sys/departments`

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/sys/departments` | 分页列表 |
| `GET` | `/api/sys/departments/tree` | **树形结构** |
| `POST` | `/api/sys/departments` | 创建部门 |
| `GET` | `/api/sys/departments/{id}` | 查询部门 |
| `PUT` | `/api/sys/departments/{id}` | 更新部门 |
| `DELETE` | `/api/sys/departments/{id}` | 删除部门 |

### 系统配置 — `/api/sys/configs`

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/sys/configs` | 全量列表（缓存，无分页） |
| `GET` | `/api/sys/configs/{id}` | 查询配置 |
| `PUT` | `/api/sys/configs/{id}` | 更新配置（无创建/删除） |

### 操作日志 — `/api/sys/logs`

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/sys/logs` | 分页列表 (page, pageSize=20) |

---

## 2. Master Data 模块 (`/api/masterdata`)

### 产品 — `/api/masterdata/products`
### 物料 — `/api/masterdata/materials`
### 物料分类 — `/api/masterdata/material-categories`

均支持标准 CRUD：`GET` 分页列表 | `POST` 创建 | `GET /{id}` 查询 | `PUT /{id}` 更新 | `DELETE /{id}` 删除

---

## 3. Process 模块 (`/api/process`)

### 工艺路线 — `/api/process/routes`

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/process/routes` | 分页列表 |
| `POST` | `/api/process/routes` | 创建路线 |
| `GET` | `/api/process/routes/{id}` | 查询路线 |
| `PUT` | `/api/process/routes/{id}` | 更新路线 |
| `DELETE` | `/api/process/routes/{id}` | 删除路线 |

### 工序（嵌套资源）

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/process/routes/{routeId}/steps` | 路线下工序列表（按 sortOrder 排序） |
| `POST` | `/api/process/routes/{routeId}/steps` | 添加工序 |
| `GET` | `/api/process/steps/{id}` | 查询工序 |
| `PUT` | `/api/process/steps/{id}` | 更新工序 |
| `DELETE` | `/api/process/steps/{id}` | 删除工序 |
| `PUT` | `/api/process/steps/{id}/sort` | 更新排序 (body: `{sortOrder: int}`) |

---

## 4. Warehouse 模块 (`/api/warehouse`)

### 仓库 — `/api/warehouse/warehouses`
标准 CRUD + `GET /warehouses` 全量列表

### 入库 — `/api/warehouse/inbounds`
`GET` 分页列表 (type, warehouseId, status) | `POST` 创建 | `GET /{id}` 查询

### 出库 — `/api/warehouse/outbounds`
同上

### 调拨 — `/api/warehouse/transfers`
`GET` 分页列表 (status, fromWarehouseId, toWarehouseId) | `POST` 创建 | `GET /{id}` 查询

### 盘点 — `/api/warehouse/checks`
`GET` 分页列表 | `POST` 创建 | `GET /{id}` 查询 | `PUT /{id}/confirm` 确认盘点

---

## 5. Equipment 模块 (`/api/equipment`)

### 设备分类 — `/api/equipment/categories`
标准 CRUD + `GET /categories` 返回**树形结构**

### 制造商 — `/api/equipment/manufacturers`
`GET` 全量列表 | `POST` 创建 | `PUT/DELETE /{id}`

### 设备资产 — `/api/equipment/assets`
分页列表 (keyword, status, categoryId, productionLine) | 标准 CRUD
| `PUT` | `/api/equipment/assets/{id}/status` | 状态变更 (body: `{status, reason, operator}`) — 自动记录 EquipStatusLog |

### 保养计划 — `/api/equipment/maintenance-plans`
分页列表 (equipId, status, startDate, endDate) | `POST` 创建

### 保养记录 — `/api/equipment/maintenance-records`
`POST` 创建 (若有 planId 自动完成对应计划)

### 维修 — `/api/equipment/repairs`
分页列表 (equipId, status) | `POST` 创建 (自动设 status=REPORTED)
| `PUT` | `/api/equipment/repairs/{id}/complete` | 完成维修（自动计算停机时间） |

---

## 6. Plan 模块 (`/api/plan`)

### MPS 主生产计划 — `/api/plan/mps`
标准 CRUD

### 日计划 — `/api/plan/daily`
`GET` 分页列表 (含 planDate 过滤) | `POST` 创建

### 排程 — `/api/plan/schedules`
`GET` 分页列表 | `POST` 创建

### 订单拆分 — `/api/plan/splits`
`GET` 分页列表 | `POST` 创建

---

## 7. Production 模块 (`/api/production`)

### 工单 — `/api/production/work-orders`
标准 CRUD
| `PUT` | `/api/production/work-orders/{id}/status` | 状态变更 (?status=2) |

### 派工单 — `/api/production/dispatch-orders`
分页列表 (workOrderId, status) | `POST` 创建 | `PUT /{id}` 更新

### 报工 — `/api/production/mgmt/reportings`
分页列表 (workOrderId, processCode) | `POST` 创建（自动更新 WIP 数量）
| `GET` | `/api/production/mgmt/completions` | 已完成的报工记录 |

### 在制品 WIP — `/api/production/mgmt/wip`
分页列表 | `GET /wip/process/{processCode}` 按工序查询

### 返修 — `/api/production/mgmt/repair-orders`
分页列表 | `POST` 创建
| `PUT` | `/api/production/mgmt/repair-orders/{id}/complete` | 返修完成 (?result=...) |

---

## 8. Material 模块 (`/api`)

无模块级路径前缀，各子资源独立：

### 领料 — `/api/requisitions`
分页列表 (status, workOrderId, materialCode) | `POST` 创建 | `GET /{id}` 查询 | `PUT /{id}/approve` 审批

### 退料 — `/api/returns`
分页列表 (status, workOrderId, materialCode) | `POST` 创建 | `GET /{id}` 查询

### 补料 — `/api/supplements`
分页列表 (status, workOrderId, materialCode) | `POST` 创建 | `GET /{id}` 查询 | `PUT /{id}/approve` 审批

### 库存 — `/api/inventory`
分页列表 (materialId, materialCode, warehouse)
| `GET` | `/api/inventory/low-stock` | 低库存预警（qty ≤ safetyStock） |

### 库存日志 — `/api/inventory-logs`
分页列表 (materialId, materialCode, changeType)

---

## 9. Quality 模块 (`/api/quality`)

### 检验分类 — `/api/quality/categories`
标准 CRUD

### 检验项目 — `/api/quality/items`
分页列表 (keyword, categoryId) | 标准 CRUD

### 检验标准 — `/api/quality/standards`
标准 CRUD
| `GET` | `/api/quality/standards/{id}/details` | 标准明细列表 |
| `POST` | `/api/quality/standards/{id}/details` | 批量保存明细 (body: List) |

### 检验订单 — `/api/quality/orders`
分页列表 (keyword, orderType, result) | 标准 CRUD
| `PUT` | `/api/quality/orders/{id}/result` | 填写检验结果 (body: `{result, inspector}`) |
| `GET` | `/api/quality/orders/{id}/results` | 检验结果列表 |
| `POST` | `/api/quality/orders/{id}/results` | 批量保存结果 (body: List) |

### 首末件检验 — `/api/quality/first-last`
分页列表 (keyword, workOrderId, result) | 标准 CRUD

### 巡检 — `/api/quality/patrols`
分页列表 (keyword, workOrderId, result) | 标准 CRUD

### 缺陷记录 — `/api/quality/defects`
分页列表 (keyword, workOrderId, defectType) | 标准 CRUD

---

## 10. Andon 模块 (`/api/andon`)

### 安灯类型 — `/api/andon/types`
标准 CRUD

### 安灯配置 — `/api/andon/configs`
分页列表 (keyword, andonTypeId, status) | 标准 CRUD

### 异常原因 — `/api/andon/reasons`
分页列表 | 标准 CRUD
| `GET` | `/api/andon/reasons/tree` | **树形结构** |

### 异常记录 — `/api/andon/records`
分页列表 (keyword, andonTypeId, status, workCenter, startTime, endTime) | `POST` 创建 (自动设 status=REPORTED)
| `PUT` | `/api/andon/records/{id}/status` | 状态变更 (PENDING→CONFIRMED→RESOLVED，自动记录时间戳) |
| `GET` | `/api/andon/records/active` | 活跃异常 (REPORTED/CONFIRMED) |

---

## 11. Barcode 模块 (`/api/barcode`)

### 条码类型 — `/api/barcode/types`
### 条码规则 — `/api/barcode/rules`
### 条码模板 — `/api/barcode/templates`
### 条码应用 — `/api/barcode/applications`

均支持标准 CRUD

### 条码记录
| `POST` | `/api/barcode/generate` | 生成条码 (自动生成 UUID 条码号) |
| `GET` | `/api/barcode/records` | 分页列表 |
| `GET` | `/api/barcode/records/{id}` | 查询记录 |

---

## 12. Wages 模块 (`/api/wages`)

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/wages/records` | 分页列表 |
| `POST` | `/api/wages/records` | 创建工资记录 (**自动计算 totalAmount = qty × unitPrice**) |
| `GET` | `/api/wages/records/{id}` | 查询记录 |
| `DELETE` | `/api/wages/records/{id}` | 删除记录 |

---

## 13. Trace 模块 (`/api/trace`)

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/trace/product/{barcode}` | 正向追溯：产品 → 物料批次 |
| `GET` | `/api/trace/material/{batchNo}` | 反向追溯：物料批次 → 产品 |
| `POST` | `/api/trace/products` | 创建产品追溯记录 |
| `POST` | `/api/trace/material-batches` | 创建物料批次记录 |
| `POST` | `/api/trace/product-materials` | 创建产品-物料关联 |

---

## 14. ERP 模块 (`/api/erp`)

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/erp/sync-logs` | 分页列表 (keyword, status) |
| `POST` | `/api/erp/sync-logs` | 创建同步日志 |

---

## 15. Dashboard 模块 (`/api/dashboard`)

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/dashboard/summary` | 综合摘要 (工单/设备/报警/OEE/WIP/低库存) |
| `GET` | `/api/dashboard/recent-orders?limit=10` | 最近工单 |
| `GET` | `/api/dashboard/recent-alarms?limit=10` | 最近报警 |
| `GET` | `/api/dashboard/line-status` | 产线状态（按产线聚合） |
| `GET` | `/api/dashboard/today-stats` | 今日统计 (产量/缺陷率/合格率) |
| `GET` | `/api/dashboard/equip-status` | 设备状态汇总 (RUNNING/IDLE/MAINTENANCE/BROKEN) |
| `GET` | `/api/dashboard/energy-summary` | 今日能耗汇总 |

> 看板数据缓存 TTL 60 秒

---

## 16. Report 模块 (`/api/report`)

| 方法 | 路径 | 参数 | 说明 |
|------|------|------|------|
| `GET` | `/api/report/output` | **startDate**, **endDate** | 产量报表（按工序聚合） |
| `GET` | `/api/report/defect` | **startDate**, **endDate** | 缺陷报表（按缺陷类型聚合） |
| `GET` | `/api/report/oee` | **startDate**, **endDate**, equipId(可选) | OEE 趋势报表 |
| `GET` | `/api/report/energy` | **startDate**, **endDate** | 能耗趋势报表 |
| `GET` | `/api/report/trace/product/{barcode}` | — | 产品追溯（含工单+物料） |
| `GET` | `/api/report/trace/material/{batchNo}` | — | 物料追溯（含所有产品） |
| `GET` | `/api/report/realtime` | — | 实时生产（进行中工单+最新报工） |
| `GET` | `/api/report/workshop` | **workshop**, **period** | 车间周期报表 (period=YYYY-MM) |

---

## 概述统计

| 模块 | 控制器数 | 端点数 |
|------|---------|--------|
| System | 6 | 30 |
| Masterdata | 3 | 15 |
| Process | 2 | 11 |
| Warehouse | 1 | 18 |
| Equipment | 1 | 21 |
| Plan | 1 | 11 |
| Production | 2 | 17 |
| Material | 1 | 14 |
| Quality | 1 | 40 |
| Andon | 1 | 22 |
| Barcode | 1 | 23 |
| Wages | 1 | 4 |
| Trace | 1 | 5 |
| ERP | 1 | 2 |
| Dashboard | 1 | 7 |
| Report | 1 | 8 |
| **总计** | **25** | **248** |

### HTTP 方法分布

| 方法 | 数量 | 用途 |
|------|------|------|
| `GET` | ~140 | 查询/列表/报表 |
| `POST` | ~60 | 创建/批量操作 |
| `PUT` | ~30 | 更新/状态变更 |
| `DELETE` | ~18 | 软删除 |
