# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概况

MES（制造执行系统）后端服务，RESTful API 风格。覆盖碱性锌锰电池制造的完整业务场景。

- **290 Java 源文件** | **69 MyBatis XML Mapper** | **16 业务模块**
- 从 MyBatis-Plus + Thymeleaf（`src1/`，375 文件）迁移到原生 MyBatis + 纯 REST API
- 迁移文档：`docs/MIGRATION.md`（架构对比、转换规则、模块清单）
- API 文档：`docs/API.md`（全部端点、参数、返回值）

## 技术栈

- **Spring Boot 4.1.0** on **Java 17**（Maven 管理依赖版本）
- **Spring MVC**（`@RestController`，纯 JSON API，Thymeleaf 在 classpath 上但未使用）
- **MyBatis 4.0.1** + XML Mapper（数据访问层，无 JPA Entity）
- **MySQL** — HikariCP 连接池
- **Redis** — 缓存（Spring Cache）+ HTTP Session（Spring Session）+ RedisTemplate
- **Log4j2** — 所有 starter 都排除了 `spring-boot-starter-logging`
- **Lombok** — `@Data`、`@Builder`、`@NoArgsConstructor`、`@AllArgsConstructor`、`@RequiredArgsConstructor`
- **BCrypt** — `spring-security-crypto`（仅密码加密，无 Spring Security 认证）

## 项目结构

```
src/main/java/com/example/mes/
├── MesApplication.java              # 入口，@MapperScan(16模块mapper包)
├── config/
│   ├── SecurityConfig.java          # BCryptPasswordEncoder Bean
│   ├── RedisConfig.java             # @EnableCaching, @EnableRedisHttpSession, RedisTemplate, CacheManager
│   └── CacheNames.java              # 42个缓存名称常量
├── common/
│   ├── response/
│   │   ├── Result.java              # 统一响应 {code, message, data}
│   │   ├── PageResult.java          # 分页响应 {total, page, pageSize, rows}
│   │   └── TreeNode.java            # 泛型树节点 {node, children}
│   ├── util/
│   │   └── TreeUtils.java           # 泛型 buildTree() 方法
│   └── exception/
│       └── GlobalExceptionHandler.java  # @RestControllerAdvice
└── modules/                         # 16个业务模块
    ├── system/          → /api/sys/*            (8实体)
    ├── masterdata/      → /api/masterdata/*     (3实体)
    ├── process/         → /api/process/*        (2实体)
    ├── warehouse/       → /api/warehouse/*      (5实体)
    ├── equipment/       → /api/equipment/*      (12实体)
    ├── plan/            → /api/plan/*           (4实体)
    ├── production/      → /api/production/*     (5实体)
    ├── material/        → /api/*               (5实体)
    ├── quality/         → /api/quality/*        (9实体)
    ├── andon/           → /api/andon/*          (4实体)
    ├── barcode/         → /api/barcode/*        (5实体)
    ├── wages/           → /api/wages/*          (1实体)
    ├── trace/           → /api/trace/*          (3实体)
    ├── erp/             → /api/erp/*            (1实体)
    ├── dashboard/       → /api/dashboard/*      (0实体: 跨模块聚合)
    └── report/          → /api/report/*         (0实体: 跨模块报表)
```

每个模块内部结构：
```
modules/{name}/
├── entity/          # 实体类 (@Data @Builder @NoArgsConstructor @AllArgsConstructor)
├── mapper/          # Mapper 接口 (手写，@Param 标注参数)
├── service/         # 服务接口
│   └── impl/        # 服务实现 (@Service @RequiredArgsConstructor)
└── controller/      # REST Controller (@RestController)
```

XML Mapper 位于 `src/main/resources/mappers/*.xml`

## 命令

使用 Maven Wrapper（Windows：`mvnw.cmd`）：

```powershell
mvnw.cmd spring-boot:run          # 启动应用（端口 8000）
mvnw.cmd test                     # 运行测试
mvnw.cmd test -Dtest=ClassName    # 单个测试类
mvnw.cmd clean package            # 打包 jar
mvnw.cmd verify                   # 完整构建 + 测试
mvnw.cmd clean compile            # 仅编译（快速验证，290源文件约5秒）
```

## 运行时依赖

启动前需要以下服务可用：

| 服务 | 默认地址 | 配置位置 |
|------|---------|---------|
| MySQL | `localhost:3306` | `application.properties` / `.env` |
| Redis | `localhost:6379` | `application.properties` / `.env` |

本地开发环境变量在 `.env` 文件中（IDE 插件自动加载）。

## 架构模式（新增模块必须遵循）

### 实体层 (Entity)

```java
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class XxxEntity {
    private Long id;
    // ... business fields ...
    private Integer status;          // 必选: 1=enabled, 0=disabled
    private Integer isDeleted;       // 必选: 0=active, 1=soft-deleted
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

- 必须有 `@Data @Builder @NoArgsConstructor @AllArgsConstructor`
- 标准字段：`id(Long)`, `status(Integer)`, `isDeleted(Integer)`, `createTime(LocalDateTime)`, `updateTime(LocalDateTime)`
- 无 JPA/MyBatis-Plus 注解，无 `implements Serializable`
- 无 `children` 字段（树结构通过 `TreeNode<T>` 在 Service 层构建）
- 字段命名 camelCase，数据库列 snake_case

### Mapper 接口 + XML

```java
public interface XxxMapper {
    int insert(Xxx entity);
    int update(Xxx entity);
    Xxx selectById(@Param("id") Long id);
    List<Xxx> selectList(@Param("keyword") String keyword, @Param("status") Integer status,
                         @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("keyword") String keyword, @Param("status") Integer status);
    int deleteById(@Param("id") Long id);
}
```

XML 模板（参考 `src/main/resources/mappers/SysUserMapper.xml`）：
- `<resultMap id="BaseResultMap">` — 显式映射所有列
- `<sql id="BaseColumns">` — 所有列名（snake_case）
- `insert`：`useGeneratedKeys="true" keyProperty="id"`
- `update`：动态 `<set><if test>`，`WHERE id=#{id} AND is_deleted=0`
- `deleteById`：`UPDATE table SET is_deleted = 1 WHERE id = #{id}`
- 查询必须 `AND is_deleted = 0`
- 分页：`ORDER BY id DESC LIMIT #{offset}, #{limit}`
- BigDecimal：`jdbcType="DECIMAL"`

### Service 层

```java
@Service @RequiredArgsConstructor
public class XxxServiceImpl implements XxxService {
    private final XxxMapper mapper;
    // create: @Transactional + @CacheEvict(allEntries=true) on X + X_LIST caches
    // update: set immutable fields (code) to null, @Transactional + @CacheEvict
    // delete: @Transactional + @CacheEvict
    // getById: @Cacheable(key="#id", unless="#result==null")
    // list(paginated): no caching, manual offset = (page-1)*pageSize → PageResult.of()
}
```

### Controller 层

```java
@RestController @RequestMapping("/api/{module}/{plural}") @RequiredArgsConstructor
public class XxxController {
    // POST /           → Result<Entity> create(@RequestBody)
    // PUT /{id}        → Result<Entity> update(@PathVariable @RequestBody)
    // DELETE /{id}     → Result<Void> delete(@PathVariable)
    // GET /{id}        → Result<Entity> getById(@PathVariable)
    // GET /            → Result<PageResult<Entity>> list(keyword,status,page,pageSize)
    // GET /tree        → Result<List<TreeNode<Entity>>> tree()  (树形结构)
}
```

## 缓存策略

- **单条查询**：按 ID 缓存，30 分钟 TTL，null 不缓存（`unless="#result==null"`）
- **全量列表**：整体缓存（如 `sys_role_all`、`sys_config_all`），增删改时清除
- **树结构**：独立缓存（如 `sys_menu_tree`、`sys_dept_tree`），增删改时清除
- **分页查询**：不缓存（参数组合太多）
- **看板数据**：`dashboard` 缓存，60 秒 TTL（变化频繁）
- **报表数据**：不缓存（日期范围参数每次不同）
- 缓存 Key 前缀：`mes:cache:`
- Session Key 前缀：`mes:session:`
- 缓存名称定义在 `CacheNames.java`（42 个常量）

## 软删除策略

| 类型 | 实现 | 适用表 |
|------|------|--------|
| 有 `isDeleted` | `UPDATE SET is_deleted = 1` + 查询 `AND is_deleted = 0` | 大多数业务表 |
| 无 `isDeleted` | 物理删除或仅追加 | 日志/流转记录/关联中间表 |

## 树结构处理

使用 `TreeNode<T>` + `TreeUtils.buildTree()`：
- 实体不包含 `children` 字段
- Service 层调用 `TreeUtils.buildTree(allNodes, 0L, Entity::getId, Entity::getParentId)`
- Controller 返回 `Result<List<TreeNode<Entity>>>`
- 适用于：`SysMenu`, `SysDepartment`, `EquipCategory`, `AndonReason`

## 注意事项

- Jackson 3.x 包名为 `tools.jackson`，不是 `com.fasterxml.jackson`
- Redis 序列化使用 `GenericJacksonJsonRedisSerializer`（需传入 `tools.jackson.databind.ObjectMapper`）
- 所有 `DELETE` 操作都是软删除（`SET is_deleted = 1`），日志类和中间表除外
- MyBatis 开启了下划线转驼峰映射（`mapUnderscoreToCamelCase=true`）
- 不允许通过 update 接口修改 `code` 类字段（如 `username`、`password`、`roleCode`、`productCode`、`materialCode` 等）
- `@MapperScan` 需注册所有 16 个模块的 mapper 包
- `mybatis-config.xml` 需注册所有 16 个模块的 entity 包作为 typeAliases
- 新增模块时需同步更新 3 个文件：`CacheNames.java`, `mybatis-config.xml`, `MesApplication.java`
- `BigDecimal` 字段在 XML resultMap 中必须显式标注 `jdbcType="DECIMAL"`
- `mvnw.cmd clean compile` 编译 290 源文件约 5 秒，作为快速验证手段
