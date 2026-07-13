# src1 → src 架构迁移文档

## 概述

将 `src1`（MyBatis-Plus + Thymeleaf 全栈 MES）的完整业务功能迁移到 `src`（MyBatis XML + Redis 缓存 + 纯 REST API）的架构风格中。

- **迁移日期**：2026-07-13
- **源文件**：`src1/`（375 文件，不在 Git 版本控制中）
- **目标文件**：`src/main/java/com/example/mes/` + `src/main/resources/mappers/`
- **最终规模**：290 源文件 + 69 XML Mapper = 359 文件

## 架构对比

| 维度 | src1 (迁移前) | src (迁移后) |
|------|-------------|-------------|
| 包名 | `com.mes` | `com.example.mes` |
| 架构 | 扁平结构 | 模块化 `modules/{name}/entity\|mapper\|service\|controller` |
| ORM | MyBatis-Plus (`BaseMapper`, `IService`, `@TableLogic`) | 原生 MyBatis + XML Mapper |
| 渲染 | Thymeleaf 服务端渲染 (76 模板) | 纯 REST API (JSON) |
| 前端 | Bootstrap + jQuery (内嵌) | 独立前端项目 (未迁移) |
| 数据库 | H2(dev) / MySQL(prod) | MySQL only |
| 缓存 | 无 | Redis + Spring Cache (@Cacheable/@CacheEvict) |
| 注入 | `@Autowired` 字段注入 | `@RequiredArgsConstructor` 构造器注入 |
| 响应格式 | `Map<String,Object>` {code, msg, data} | `Result<T>` / `PageResult<T>` |
| 软删除 | `deleted` + `@TableLogic` | `isDeleted` + 手动 UPDATE SQL |
| 分页 | MyBatis-Plus `Page<T>` | 手动 `LIMIT #{offset}, #{limit}` |
| 异常处理 | try/catch 每个接口 | 全局 `@RestControllerAdvice` |
| 树结构 | `@TableField(exist=false) children` | `TreeNode<T>` + `TreeUtils` |

## 核心转换规则

### 实体层 (Entity)

```
src1: @Data @TableName("xxx") implements Serializable { @TableId @TableLogic Integer deleted }
src:  @Data @Builder @NoArgsConstructor @AllArgsConstructor { Integer isDeleted, status, createTime, updateTime }
```

- 去除所有 MyBatis-Plus 注解
- 去除 `implements Serializable`
- 添加 `@Builder @NoArgsConstructor @AllArgsConstructor`
- `deleted` → `isDeleted`（全局重命名）
- 新增标准字段：`status(Integer, 默认1)`, `createTime(LocalDateTime)`, `updateTime(LocalDateTime)`
- 移除 `@TableField(exist = false) children`（树结构移至 Service 层）

### Mapper 层

```
src1: @Mapper extends BaseMapper<Entity>  (空接口)
src:  interface { int insert(Entity); int update(Entity); Entity selectById(@Param Long id);
       List<Entity> selectList(@Param); long countList(@Param); int deleteById(@Param Long id); }
       + XML mapper file
```

**XML 模板**：
- `<resultMap id="BaseResultMap">` — 显式映射所有列
- `<sql id="BaseColumns">` — 可复用的列名片段
- `insert`：`useGeneratedKeys="true" keyProperty="id"`
- `update`：动态 `<set><if test>`，末尾 `WHERE id=#{id} AND is_deleted=0`
- `deleteById`：`UPDATE table SET is_deleted = 1 WHERE id = #{id}`
- `selectById`：`AND is_deleted = 0`
- `selectList`：keyword LIKE CONCAT 搜索 + 可选 status 过滤 + `AND is_deleted = 0` + `ORDER BY id DESC` + `LIMIT #{offset}, #{limit}`
- `countList`：与 selectList 相同过滤条件，返回 `long`
- `BigDecimal` 字段：`jdbcType="DECIMAL"`

### Service 层

```java
@Service
@RequiredArgsConstructor
public class XxxServiceImpl implements XxxService {
    private final XxxMapper mapper;

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.X, CacheNames.X_LIST}, allEntries = true)
    public Xxx create(Xxx entity) { if (entity.getStatus() == null) entity.setStatus(1); mapper.insert(entity); return entity; }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.X, CacheNames.X_LIST}, allEntries = true)
    public Xxx update(Xxx entity) { entity.setImmutableCode(null); mapper.update(entity); return mapper.selectById(entity.getId()); }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.X, CacheNames.X_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override @Cacheable(value = CacheNames.X, key = "#id", unless = "#result == null")
    public Xxx getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<Xxx> list(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Xxx> rows = mapper.selectList(keyword, status, offset, pageSize);
        long total = mapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
```

### Controller 层

```java
@RestController
@RequestMapping("/api/{module}/{resource-plural}")
@RequiredArgsConstructor
public class XxxController {
    private final XxxService service;

    @PostMapping                   → Result<Xxx> create(@RequestBody Xxx)
    @PutMapping("/{id}")           → Result<Xxx> update(@PathVariable Long id, @RequestBody Xxx)
    @DeleteMapping("/{id}")        → Result<Void> delete(@PathVariable Long id)
    @GetMapping("/{id}")           → Result<Xxx> getById(@PathVariable Long id)
    @GetMapping                    → Result<PageResult<Xxx>> list(...)
    @GetMapping("/tree")           → Result<List<TreeNode<Xxx>>> tree()  (树形结构)
}
```

## 模块迁移清单

| Phase | 模块 | 实体数 | 新增文件 | 备注 |
|-------|------|--------|---------|------|
| 0 | system (users/roles) | 3 | — | 迁移前已存在 |
| 1.A | system-remaining | 5 | 25 | SysMenu树, SysDepartment树, SysRoleMenu |
| 1.B | master-data | 3 | 15 | 产品, 物料, 物料分类 |
| 1.C | process | 2 | 12 | 工艺路线 + 工序(主从) |
| 2.A | warehouse | 5 | 26 | 仓库, 入库, 出库, 调拨, 盘点 |
| 2.B | equipment | 12 | 64 | 最大模块: 资产, 故障, 保养, 维修, OEE, 能耗 |
| 2.C | plan | 4 | 17 | MPS, 日计划, 排程, 订单拆分 |
| 2.D | production | 5 | 22 | 工单, 派工, 报工, WIP, 返修 |
| 2.E | material | 5 | 21 | 领料, 退料, 补料, 库存, 库存日志 |
| 3.A | quality | 9 | 46 | 检验分类/项目/标准/订单/结果, 首末检, 巡检 |
| 3.B | andon | 4 | 21 | 安灯类型/配置/原因(树)/记录(生命周期) |
| 3.C | barcode | 5 | 26 | 条码类型/规则/模板/应用/记录 |
| 3.D | wages | 1 | 6 | 计件工资 (totalAmount = qty × unitPrice) |
| 4.A | trace | 3 | 16 | 产品追溯, 物料批次, 产品-物料关联 |
| 4.B | erp | 1 | 6 | ERP同步日志 |
| 4.C | dashboard | 0 | 3 | 跨模块聚合: 摘要, 产线状态, 能耗统计 |
| 4.D | report | 0 | 3 | 跨模块报表: 产量, 缺陷, OEE, 追溯 |
| **合计** | **16** | **67** | **359** | |

## 新增基础设施

| 文件 | 说明 |
|------|------|
| `common/response/TreeNode.java` | 泛型树节点包装 `{node, children}` |
| `common/util/TreeUtils.java` | 泛型 `buildTree()` 方法 |
| `common/exception/GlobalExceptionHandler.java` | `@RestControllerAdvice` 全局异常处理 |
| `config/CacheNames.java` | 统一缓存名称常量 (42个) |

## URL 规范

统一采用 `/api/{module}/{resource-plural}` 格式：

| 示例 | 说明 |
|------|------|
| `/api/sys/users` | 系统用户 |
| `/api/masterdata/products` | 产品主数据 |
| `/api/process/routes/{id}/steps` | 工艺路线下的工序(嵌套资源) |
| `/api/production/work-orders` | 生产工单 |
| `/api/warehouse/inbounds` | 入库记录 |
| `/api/equipment/assets/{id}/status` | 设备状态变更(子资源操作) |
| `/api/dashboard/summary` | 看板摘要 |

## 软删除策略

| 类型 | 实现 | 适用表 |
|------|------|--------|
| 有 `isDeleted` | `UPDATE SET is_deleted = 1 WHERE id = #{id}` | 大多数业务表 |
| 无 `isDeleted` | 物理删除或无删除 | 日志表, 关联中间表, 流转记录 |

无软删除的表：`sys_operation_log`, `sys_user_role`, `sys_role_menu`, `sys_config`, `prod_wip`, `mat_inventory`, `mat_inventory_log`, `equip_maintenance_record`, `equip_inspection_record`, `equip_status_log`, `equip_oee_record`, `equip_energy_record`, `quality_standard_detail`, `quality_inspection_result`, `quality_defect_record`, `barcode_record`, `trace_*`, `erp_sync_log`

## 缓存策略

| 缓存类型 | TTL | 示例 |
|---------|-----|------|
| 单条缓存 | 30 min | `@Cacheable(value="sys_user", key="#id")` |
| 全量列表 | 30 min | `@Cacheable(value="sys_role_all")` |
| 树结构 | 30 min | `@Cacheable(value="sys_menu_tree")` |
| 看板数据 | 60s | `@Cacheable(value="dashboard")` |
| 分页列表 | 不缓存 | 参数组合太多 |

## 特殊业务逻辑迁移

| 逻辑 | src1 位置 | src 位置 |
|------|----------|----------|
| 密码加密 | SystemController | SysUserServiceImpl.createUser |
| 菜单树构建 | SystemController.buildMenuTree | SysMenuServiceImpl.getMenuTree |
| 部门树构建 | SystemController.buildDeptTree | SysDepartmentServiceImpl.getDepartmentTree |
| 角色菜单分配 | SystemController.assignRoleMenus | SysRoleMenuServiceImpl.assignMenus |
| 设备状态变更+日志 | EquipmentController | EquipAssetServiceImpl.updateStatus |
| 维修完成+停机时间 | EquipmentController | EquipRepairServiceImpl.complete |
| 报工WIP数量更新 | ProductionController | ProdReportingServiceImpl.create |
| 工资总额计算 | WagesController | WageRecordServiceImpl.createRecord |
| 安灯生命周期 | AndonController | AndonRecordServiceImpl.updateStatus |
| 看板数据聚合 | DashboardServiceImpl | DashboardServiceImpl (同名迁移) |
| 报表数据聚合 | ReportServiceImpl | ReportServiceImpl (同名迁移) |

## 未迁移内容

| 内容 | 处理方式 |
|------|---------|
| 76 个 Thymeleaf 模板 | 保留在 `src1/` 作为参考 |
| `mock-data.js` | 保留在 `src1/` |
| `static/css|js` | 保留在 `src1/` |
| H2 数据库配置 | 移除，仅保留 MySQL |
| Druid 连接池 | 未迁移（src 使用 HikariCP） |
| Alibaba Druid 监控 | 未迁移 |
