# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概况

MES（制造执行系统）后端服务，RESTful API 风格。

## 技术栈

- **Spring Boot 4.1.0** on **Java 17**（Maven 管理依赖版本）
- **Spring MVC**（`@RestController`，纯 JSON API，Thymeleaf 在 classpath 上但未使用）
- **MyBatis 4.0.1** + XML Mapper（数据访问层，无 JPA Entity）
- **MySQL** — HikariCP 连接池
- **Redis** — 缓存（Spring Cache）+ HTTP Session（Spring Session）+ RedisTemplate
- **Log4j2** — 所有 starter 都排除了 `spring-boot-starter-logging`
- **Lombok** — `@Data`、`@Builder`、`@RequiredArgsConstructor` 等。无手写 getter/setter
- **BCrypt** — `spring-security-crypto`（仅密码加密，无 Spring Security 认证）

## 项目结构

```
src/main/java/com/example/mes/
├── MesApplication.java          # 入口，@MapperScan
├── config/
│   ├── SecurityConfig.java      # BCryptPasswordEncoder Bean
│   ├── RedisConfig.java         # @EnableCaching, @EnableRedisHttpSession, RedisTemplate, CacheManager
│   └── CacheNames.java          # 缓存名称常量
├── common/response/
│   ├── Result.java              # 统一响应 {code, message, data}
│   └── PageResult.java          # 分页响应 {total, page, pageSize, rows}
└── modules/system/
    ├── entity/                  # SysUser, SysRole, SysUserRole（纯 POJO，MyBatis 映射）
    ├── mapper/                  # MyBatis Mapper 接口 + XML（src/main/resources/mappers/*.xml）
    ├── service/                 # 服务接口
    │   └── impl/                # 服务实现（@Service, @Transactional, @Cacheable/@CacheEvict）
    └── controller/              # REST Controller（/api/sys/users, /api/sys/roles）
```

## 命令

使用 Maven Wrapper（Windows：`mvnw.cmd`）：

```powershell
mvnw.cmd spring-boot:run          # 启动应用（端口 8000）
mvnw.cmd test                     # 运行测试
mvnw.cmd test -Dtest=ClassName    # 单个测试类
mvnw.cmd clean package            # 打包 jar
mvnw.cmd verify                   # 完整构建 + 测试
```

## 运行时依赖

启动前需要以下服务可用：

| 服务 | 默认地址 | 配置位置 |
|------|---------|---------|
| MySQL | `localhost:3306` | `application.properties` / `.env` |
| Redis | `localhost:6379` | `application.properties` / `.env` |

本地开发环境变量在 `.env` 文件中（IDE 插件自动加载）。

## 缓存策略

- **单条查询**（`getUserById`、`getRoleById`）：按 ID 缓存，30 分钟 TTL，null 不缓存
- **角色全量列表**（`listAll`）：整体缓存，任何角色变更时清空
- **分页查询**（`listUsers`、`listRoles`）：不缓存（参数组合太多）
- **增删改操作**：清除相关缓存，保证一致性
- 缓存 Key 前缀：`mes:cache:`
- Session Key 前缀：`mes:session:`

## 注意事项

- Jackson 3.x 包名为 `tools.jackson`，不是 `com.fasterxml.jackson`
- Redis 序列化使用 `GenericJacksonJsonRedisSerializer`（需传入 `tools.jackson.databind.ObjectMapper`）
- 所有 `DELETE` 操作都是软删除（`SET is_deleted = 1`）
- MyBatis 开启了下划线转驼峰映射（`mapUnderscoreToCamelCase=true`）
- 不允许通过 update 接口修改 `username`、`password`、`roleCode` 字段
