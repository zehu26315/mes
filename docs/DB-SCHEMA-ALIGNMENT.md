# 数据库脚本与后端代码一致性修复报告

> 日期：2026-07-14 | 分支：dev | 编译状态：✅ BUILD SUCCESS（282 源文件）

## 概述

对比 `src/main/resources/init.sql`（64 张表）与全部 64 个 Java Entity + MyBatis XML Mapper，发现并修复了 **5 类共 21 处不一致**。

---

## 修复清单

### P0：SQL 列类型冲突（2 表 7 列）

**问题**：SQL 中定义为 `VARCHAR(50)`，但 Entity 使用 `BigDecimal`，XML Mapper 声明 `jdbcType="DECIMAL"`，写入时可能类型冲突。

**修复文件**：`src/main/resources/init.sql`

#### `quality_standard_detail`

```diff
- standard_value VARCHAR(50) COMMENT 'Standard Value',
- upper_limit VARCHAR(50) COMMENT 'Upper Limit',
- lower_limit VARCHAR(50) COMMENT 'Lower Limit',
+ standard_value DECIMAL(10,4) COMMENT 'Standard Value',
+ upper_limit DECIMAL(10,4) COMMENT 'Upper Limit',
+ lower_limit DECIMAL(10,4) COMMENT 'Lower Limit',
```

#### `quality_inspection_result`

```diff
- standard_value VARCHAR(50) COMMENT 'Standard Value',
- upper_limit VARCHAR(50) COMMENT 'Upper Limit',
- lower_limit VARCHAR(50) COMMENT 'Lower Limit',
- actual_value VARCHAR(50) COMMENT 'Actual Value',
+ standard_value DECIMAL(10,4) COMMENT 'Standard Value',
+ upper_limit DECIMAL(10,4) COMMENT 'Upper Limit',
+ lower_limit DECIMAL(10,4) COMMENT 'Lower Limit',
+ actual_value DECIMAL(10,4) COMMENT 'Actual Value',
```

---

### P1：实体缺少时间字段（16 个 Entity）

**问题**：SQL 定义了 `create_time`/`update_time` 但 Entity 缺少对应字段，导致 Java 侧无法获取数据库自动填充的时间值。

#### 缺 `updateTime`（10 个）

| Entity 文件 | SQL 表 | 添加字段 |
|------------|--------|---------|
| `modules/system/entity/SysOperationLog.java` | `sys_operation_log` | `private LocalDateTime updateTime;` |
| `modules/equipment/entity/EquipStatusLog.java` | `equip_status_log` | `private LocalDateTime updateTime;` |
| `modules/equipment/entity/EquipInspectionRecord.java` | `equip_inspection_record` | `private LocalDateTime updateTime;` |
| `modules/equipment/entity/EquipOeeRecord.java` | `equip_oee_record` | `private LocalDateTime updateTime;` |
| `modules/equipment/entity/EquipEnergyRecord.java` | `equip_energy_record` | `private LocalDateTime updateTime;` |
| `modules/material/entity/MatInventoryLog.java` | `mat_inventory_log` | `private LocalDateTime updateTime;` |
| `modules/quality/entity/QualityDefectRecord.java` | `quality_defect_record` | `private LocalDateTime updateTime;` |
| `modules/trace/entity/TraceProduct.java` | `trace_product` | `private LocalDateTime updateTime;` |
| `modules/trace/entity/TraceMaterialBatch.java` | `trace_material_batch` | `private LocalDateTime updateTime;` |
| `modules/trace/entity/TraceProductMaterial.java` | `trace_product_material` | `private LocalDateTime updateTime;` |

#### 缺 `createTime`（2 个）

| Entity 文件 | SQL 表 | 添加字段 |
|------------|--------|---------|
| `modules/material/entity/MatInventory.java` | `mat_inventory` | `private LocalDateTime createTime;` |
| `modules/production/entity/ProdWip.java` | `prod_wip` | `private LocalDateTime createTime;` |

#### 缺 `createTime` + `updateTime`（4 个）

| Entity 文件 | SQL 表 | 添加字段 + import |
|------------|--------|-------------------|
| `modules/quality/entity/QualityStandardDetail.java` | `quality_standard_detail` | `import java.time.LocalDateTime;`<br>`private LocalDateTime createTime;`<br>`private LocalDateTime updateTime;` |
| `modules/quality/entity/QualityInspectionResult.java` | `quality_inspection_result` | `import java.time.LocalDateTime;`<br>`private LocalDateTime createTime;`<br>`private LocalDateTime updateTime;` |
| `modules/system/entity/SysUserRole.java` | `sys_user_role` | `import java.time.LocalDateTime;`<br>`private LocalDateTime createTime;`<br>`private LocalDateTime updateTime;` |
| `modules/system/entity/SysRoleMenu.java` | `sys_role_menu` | `import java.time.LocalDateTime;`<br>`private LocalDateTime createTime;`<br>`private LocalDateTime updateTime;` |

---

### P2：SysMenu 字段命名不一致

**问题**：SQL 列 `is_visible` 按驼峰映射应对应 `isVisible`，Entity 使用了 `visible`，依赖 XML 手动映射绕过。

**影响文件（3 个）**：

#### `modules/system/entity/SysMenu.java`

```diff
- private Integer visible;
+ private Integer isVisible;
```

#### `src/main/resources/mappers/SysMenuMapper.xml`

```diff
- <result property="visible" column="is_visible"/>
+ <result property="isVisible" column="is_visible"/>

- VALUES (..., #{visible}, #{status})
+ VALUES (..., #{isVisible}, #{status})

- <if test="visible != null">is_visible = #{visible},</if>
+ <if test="isVisible != null">is_visible = #{isVisible},</if>
```

#### `modules/system/service/impl/SysMenuServiceImpl.java`

```diff
- if (menu.getVisible() == null) {
-     menu.setVisible(1);
+ if (menu.getIsVisible() == null) {
+     menu.setIsVisible(1);
```

---

### P3：EquipRepair 类型不一致

**问题**：SQL 列 `downtime` 类型为 `INT`（映射 Java `Integer`），Entity 使用了 `Long`。

#### `modules/equipment/entity/EquipRepair.java`

```diff
- private Long downtime;
+ private Integer downtime;
```

---

## 改动文件汇总

| # | 文件路径 | 改动类型 |
|---|---------|---------|
| 1 | `src/main/resources/init.sql` | 7 列 VARCHAR → DECIMAL |
| 2 | `modules/system/entity/SysOperationLog.java` | +updateTime |
| 3 | `modules/system/entity/SysUserRole.java` | +createTime +updateTime +import |
| 4 | `modules/system/entity/SysRoleMenu.java` | +createTime +updateTime +import |
| 5 | `modules/system/entity/SysMenu.java` | visible → isVisible |
| 6 | `modules/system/service/impl/SysMenuServiceImpl.java` | getter/setter 同步 |
| 7 | `modules/equipment/entity/EquipStatusLog.java` | +updateTime |
| 8 | `modules/equipment/entity/EquipInspectionRecord.java` | +updateTime |
| 9 | `modules/equipment/entity/EquipOeeRecord.java` | +updateTime |
| 10 | `modules/equipment/entity/EquipEnergyRecord.java` | +updateTime |
| 11 | `modules/equipment/entity/EquipRepair.java` | Long → Integer |
| 12 | `modules/production/entity/ProdWip.java` | +createTime |
| 13 | `modules/material/entity/MatInventory.java` | +createTime |
| 14 | `modules/material/entity/MatInventoryLog.java` | +updateTime |
| 15 | `modules/quality/entity/QualityStandardDetail.java` | +createTime +updateTime +import |
| 16 | `modules/quality/entity/QualityInspectionResult.java` | +createTime +updateTime +import |
| 17 | `modules/quality/entity/QualityDefectRecord.java` | +updateTime |
| 18 | `modules/trace/entity/TraceProduct.java` | +updateTime |
| 19 | `modules/trace/entity/TraceMaterialBatch.java` | +updateTime |
| 20 | `modules/trace/entity/TraceProductMaterial.java` | +updateTime |
| 21 | `resources/mappers/SysMenuMapper.xml` | property/参数同步 |

**共 21 个文件，编译验证通过（282 源文件，BUILD SUCCESS）**

---

## 验证结果

```
[INFO] Compiling 282 source files with javac [debug parameters release 17] to target\classes
[INFO] BUILD SUCCESS
[INFO] Total time:  48.523 s
```

---

## 相关文档

- [CLAUDE.md](../CLAUDE.md) — 项目架构与约定
- [API.md](API.md) — API 端点文档
- [MIGRATION.md](MIGRATION.md) — 迁移文档
