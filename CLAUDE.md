# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概况

MES（制造执行系统）后端服务，RESTful API 风格。覆盖碱性锌锰电池制造的完整业务场景。

- **136 Java 源文件** | **27 MyBatis XML Mapper** | **10 业务模块**
- 从 blog-uzi（旧版 MES）合并了 Notice、LoginLog、WebSocket 功能
- API 文档：`docs/API.md`（~145 端点，全部参数、返回值）

## 技术栈

- **Spring Boot 4.1.0** on **Java 17**（Maven 管理依赖版本）
- **Spring MVC**（`@RestController`，纯 JSON API，Thymeleaf 在 classpath 上但未使用）
- **MyBatis 4.0.1** + XML Mapper（数据访问层，无 JPA Entity）
- **MySQL** — HikariCP 连接池
- **Redis** — 缓存（Spring Cache）+ HTTP Session（Spring Session）+ RedisTemplate
- **WebSocket** — STOMP/SockJS（Andon 实时推送，端点 `/ws-andon`）
- **Log4j2** — 所有 starter 都排除了 `spring-boot-starter-logging`
- **Lombok** — `@Data`、`@Builder`、`@NoArgsConstructor`、`@AllArgsConstructor`、`@RequiredArgsConstructor`
- **BCrypt** — `spring-security-crypto`（仅密码加密，无 Spring Security 认证）
- **认证** — Redis Session + 手写 `LoginInterceptor`（全局 `/api/**` 拦截，白名单放行 `/api/login`、`/api/logout`）
- **Jackson 3.x** — 包名为 `tools.jackson`，不是 `com.fasterxml.jackson`

## 项目结构

```
src/main/java/com/example/mes/
├── MesApplication.java              # 入口，@MapperScan(9个模块mapper包)
├── config/
│   ├── SecurityConfig.java          # BCryptPasswordEncoder Bean
│   ├── RedisConfig.java             # @EnableCaching, RedisSessionRepository, RedisTemplate, CacheManager
│   ├── CacheNames.java              # 46个缓存名称常量
│   ├── LoginInterceptor.java        # 登录拦截器（Session 白名单验证）
│   ├── WebConfig.java               # 拦截器注册（/api/**）
│   └── WebSocketConfig.java         # STOMP/SockJS 配置（Andon 实时推送）
├── common/
│   ├── response/
│   │   ├── Result.java              # 统一响应 {code, message, data}
│   │   ├── PageResult.java          # 分页响应 {total, page, pageSize, rows}
│   │   ├── LoginVO.java             # 登录响应 {userId, username, realName, deptId, roles}
│   │   └── TreeNode.java            # 泛型树节点 {node, children}
│   ├── util/
│   │   └── TreeUtils.java           # 泛型 buildTree() 方法
│   └── exception/
│       └── GlobalExceptionHandler.java  # @RestControllerAdvice
└── modules/                         # 10个业务模块
    ├── system/          → /api/sys/*, /api/login, /api/logout
    ├── process/         → /api/process/*
    ├── equipment/       → /api/equipment/*
    ├── production/      → /api/production/*
    ├── quality/         → /api/quality/*
    ├── andon/           → /api/andon/*
    ├── barcode/         → /api/barcode/*
    ├── wages/           → /api/wages/*
    ├── trace/           → /api/trace/*
    └── dashboard/       → /api/dashboard/*     (仅 controller，无 entity/mapper/service)
```

每个模块内部结构（dashboard 除外）：
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
mvnw.cmd spring-boot:run          # 启动应用（端口 8080，由 .env 中 SERVER_PORT 配置）
mvnw.cmd test                     # 运行全部测试
mvnw.cmd test -Dtest=ClassName    # 单个测试类
mvnw.cmd clean package            # 打包 jar
mvnw.cmd clean compile            # 仅编译（快速验证）
```

## 运行时依赖

启动前需要以下服务可用，所有配置通过环境变量注入（`.env` 文件）：

| 服务 | 默认地址 | 环境变量 |
|------|---------|---------|
| MySQL | `localhost:3306` | `MES_DB_URL`, `MES_DB_USERNAME`, `MES_DB_PASSWORD` |
| Redis | `localhost:6379` | `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD`, `REDIS_DATABASE` |

其他关键环境变量：`SERVER_PORT`（默认 8080）、`CACHE_TTL`（默认 30min）、`SESSION_TIMEOUT`（默认 30min）、`LOG_ROOT`、`LOG_MES`。

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

## 测试规范

**本项目没有任何测试加载 Spring ApplicationContext** — 不使用 `@SpringBootTest`、`@WebMvcTest`、`@MybatisTest`。所有测试都是纯 Mockito + 手动构造对象的轻量级测试。

### Service 层测试模式

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("XxxService 单元测试")
class XxxServiceImplTest {
    @Mock
    private XxxMapper mapper;

    @InjectMocks
    private XxxServiceImpl service;

    @BeforeEach
    void setUp() {
        // 使用 Entity.builder() 构造测试数据
    }

    // 使用 when().thenReturn() + verify() + JUnit 5 Assertions
}
```

### Controller 层测试模式

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("XxxController 接口测试")
class XxxControllerTest {
    @Mock
    private XxxService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new XxxController(service))  // 手动构造注入
            .setControllerAdvice(new GlobalExceptionHandler())  // 真实异常处理器
            .build();
    }

    // 使用 @Nested 内部类按资源分组
    // 使用 MockHttpSession 模拟登录会话
    // 使用 jsonPath("$.field").value(...) 断言 JSON 响应
}
```

- 测试方法用 `@DisplayName` 中文描述（格式：`"操作 → 预期结果"`）
- 无 H2/嵌入式数据库，Mapper 始终 Mock
- 无 AssertJ/Hamcrest，仅用 JUnit 5 `Assertions` + Mockito `verify` + Spring `jsonPath`
- `src/test/resources/application-test.properties` 存在但未被当前测试使用（为将来 `@SpringBootTest` 预留）

## 缓存策略

- **单条查询**：按 ID 缓存，30 分钟 TTL，null 不缓存（`unless="#result==null"`）
- **全量列表**：整体缓存，增删改时 `@CacheEvict(allEntries=true)` 清除
- **树结构**：独立缓存，增删改时清除
- **分页查询**：不缓存（参数组合太多）
- **看板数据**：`dashboard` 缓存，60 秒 TTL（变化频繁）
- **报表数据**：不缓存（日期范围参数每次不同）
- 缓存 Key 前缀：`mes:cache:`
- Session Key 前缀：`mes:session:`
- 缓存名称定义在 `CacheNames.java`（46 个常量，按模块组织为 `{module}_{entity}` 和 `{module}_{entity}_list` 对）

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
- MyBatis 开启了下划线转驼峰映射（`mapUnderscoreToCamelCase=true`），同时开启懒加载（`lazyLoadingEnabled=true`, `aggressiveLazyLoading=false`）
- 不允许通过 update 接口修改 `code` 类字段（如 `username`、`password`、`roleCode`、`productCode`、`materialCode` 等）
- `@MapperScan` 需注册所有 9 个模块的 mapper 包（dashboard 无 mapper）
- `mybatis-config.xml` 需注册所有 9 个模块的 entity 包作为 typeAliases
- 新增模块时需同步更新 3 个文件：`CacheNames.java`, `mybatis-config.xml`, `MesApplication.java`
- `BigDecimal` 字段在 XML resultMap 中必须显式标注 `jdbcType="DECIMAL"`
- `RedisConfig` 中使用 `enableUnsafeDefaultTyping()` 以支持序列化带类型的 Java 对象到 Redis
- 登录拦截器通过 `request.getSession(false)` 检查 session，不会主动创建新 session；白名单 `/api/login` 和 `/api/logout` 在拦截器内部处理
