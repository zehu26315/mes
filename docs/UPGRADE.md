# blog-uzi → mes 升级迁移指南

> blog-uzi（旧版 MES，Spring Boot 3.5 + MyBatis 3.0.5）→ mes（新版 MES，Spring Boot 4.1 + MyBatis 4.0.1）

## 版本对比

| 维度 | blog-uzi（旧版） | mes（新版） |
|------|:---:|:---:|
| Spring Boot | 3.5.16 | **4.1.0** |
| MyBatis | 3.0.5 | **4.0.1** |
| Jackson | 2.x | **3.x**（`tools.jackson`） |
| 数据库 | `total` | `mes` |
| 模块数 | 14 | 13 |
| Java 源文件 | ~55 | **282** |
| XML Mapper | 29 | **67** |
| Redis 缓存 | ❌ | ✅ 34 个缓存 |
| BCrypt 密码 | ❌ | ✅ |
| WebSocket | ✅ SockJS/STOMP | ✅ SockJS/STOMP |
| 统一响应 | `Map<String,Object>` | `Result<T>` |
| 软删除 | ❌ 物理删除 | ✅ `is_deleted` |
| 前端 | ✅ 嵌入式 SPA | ❌（独立部署） |
| 测试 | ✅ H2 + MyBatis Test | ❌ |

## 功能模块映射

| blog-uzi 模块 | mes 模块 | 状态 |
|------|------|:--:|
| Dashboard（Chart.js） | dashboard（跨模块聚合） | ✅ 已迁移 |
| Production Orders（5实体） | production（5实体） | ✅ 已迁移 |
| Quality Inspection | quality（9实体） | ✅ 已迁移（增强版） |
| Equipment（4子模块） | equipment（12实体） | ✅ 已迁移（增强版） |
| Process（含SOP） | process（2实体） | ✅ 已迁移 |
| Andon（WebSocket） | andon（4实体） | ✅ 已迁移（保留WS） |
| Work Report（报工） | 合并到 production | ✅ 已迁移 |
| Barcode | barcode（5实体） | ✅ 已迁移（增强版） |
| Trace | trace（3实体） | ✅ 已迁移 |
| Piece Wage | wages | ✅ 已迁移 |
| User Management | system（用户+角色+菜单+部门） | ✅ 已迁移（增强版） |
| Notice（公告） | system/SysNotice | ✅ 已迁移 |
| Operation Log | system/SysOperationLog | ✅ 已迁移 |
| Login Log | system/SysLoginLog | ✅ 已迁移 |
| 主数据 | masterdata | 🆕 新增 |
| 仓库管理 | warehouse（5实体） | 🆕 新增 |
| 物料管理 | material（5实体） | 🆕 新增 |
| 计划排程 | ❌ 已移除 | — |
| ERP 同步 | ❌ 已移除 | — |
| 报表 | ❌ 已移除 | — |

## API 端点对照

### 认证

| blog-uzi | mes | 变化 |
|------|------|------|
| `POST /api/login` | `POST /api/login` | 密码加密（BCrypt），返回 `LoginVO` 含 roles |
| `POST /api/logout` | `POST /api/logout` | Redis Session 销毁 |

### 公告

| blog-uzi | mes |
|------|------|
| `GET /api/notice/list` | `GET /api/sys/notice` |
| `POST /api/notice` | `POST /api/sys/notice` |
| `PUT /api/notice` | `PUT /api/sys/notice/{id}` |
| `DELETE /api/notice/{id}` | `DELETE /api/sys/notice/{id}` |

### 登录日志

| blog-uzi | mes |
|------|------|
| `GET /api/login-log/list` | `GET /api/sys/login-log` |
| `POST /api/login-log` | ❌ 自动记录（登录时系统内部创建） |
| `DELETE /api/login-log/{id}` | `DELETE /api/sys/login-log/{id}` |

### 安灯 WebSocket

| 项目 | blog-uzi | mes |
|------|------|------|
| STOMP 端点 | `/ws-andon` | `/ws-andon` |
| 订阅主题 | `/topic/andon` | `/topic/andon` |
| 触发时机 | config/record 的 CUD | type/config/reason/record 的 CUD |

## 响应格式变化

### blog-uzi（旧）
```json
// 成功
{ "code": 0, "msg": "success", "data": {...} }

// 分页
{ "code": 0, "msg": "success", "total": 100, "data": [...] }
```

### mes（新）
```json
// 成功
{ "code": 200, "message": "success", "data": {...} }

// 分页
{ "code": 200, "message": "success", "data": { "total": 100, "page": 1, "pageSize": 10, "rows": [...] } }

// 错误
{ "code": 400, "message": "错误描述", "data": null }
```

## 数据库变更

### 表名对照

| blog-uzi | mes | 说明 |
|------|------|------|
| `person_user` | `sys_user` | 用户表 |
| `person_notice` | `sys_notice` | 公告表 |
| `person_login_log` | `sys_login_log` | 登录日志 |
| `person_operation_log` | `sys_operation_log` | 操作日志 |
| `mes_production_order` | `prod_work_order` | 生产工单 |
| `eqp_*` | `equip_*` | 设备相关表 |
| `prc_*` | `process_*` | 工艺相关表 |

### 新增标准字段

mes 所有业务表统一增加：
- `is_deleted TINYINT UNSIGNED DEFAULT 0` — 软删除标记
- `create_time DATETIME DEFAULT CURRENT_TIMESTAMP` — 创建时间
- `update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` — 更新时间

### 删除策略变化

| blog-uzi | mes |
|------|------|
| `DELETE FROM table WHERE id = ?` | `UPDATE table SET is_deleted = 1 WHERE id = ?` |

## 迁移步骤

### 1. 数据库迁移

```sql
-- 在旧数据库上执行，导出数据时排除已删除的表
-- blog-uzi 数据库名: total
-- mes 数据库名: mes

-- 示例：导出用户数据
mysqldump -u root -p total person_user --where="1=1" > user_data.sql

-- 导入到 mes 时注意：
-- 1. 表名映射（person_user → sys_user）
-- 2. 补充 is_deleted=0 字段
-- 3. 密码需 BCrypt 加密（旧版是明文）
```

### 2. 前端适配

mes 是纯 REST API，前端独立部署。适配要点：
- 响应结构：`code: 200`（非 0），分页包在 `data` 内
- API 路径：模块前缀变化（如 `/api/notice` → `/api/sys/notice`）
- 认证：Session cookie 自动传递（同域），跨域需配置 CORS + withCredentials
- WebSocket：端点和消息格式兼容，无需修改

### 3. 密码迁移

```sql
-- 旧版 blog-uzi 密码是明文，迁移到 mes 需要 BCrypt 加密
-- 方法一：用 mes 的 SecurityConfig BCryptPasswordEncoder 逐个加密
-- 方法二：重置为统一密码让用户登录后修改
UPDATE sys_user SET password = '$2a$10$...' WHERE username = 'xxx';
```

## 已删除的功能

以下 blog-uzi 功能在 mes 中被删除，如有需要请在前端或独立服务实现：

| 功能 | 删除原因 | 替代方案 |
|------|------|------|
| 嵌入式前端 SPA | 前后端分离 | 独立部署前端项目 |
| Chart.js 仪表盘 | 看板模块保留后端聚合 | 前端用 ECharts/Chart.js 渲染 |
| 计划排程（MPS/日计划/排程/拆单） | 不属于核心制造执行 | 独立计划系统或 excel |
| ERP 同步接口 | 定制化程度高 | 独立集成服务 |
| 报表模块 | 看板模块已覆盖核心指标 | BI 工具或独立报表服务 |
| H2 测试 | 维护成本高 | 后续按需添加集成测试 |
