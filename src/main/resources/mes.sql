-- ============================================================
-- MES 制造执行系统 - 数据库建表脚本
-- 基于 blog-uzi schema 并适配 MES 项目实体定义
-- Engine: InnoDB | Charset: utf8mb4 | Collation: utf8mb4_bin
-- ============================================================

-- ==================== 系统管理模块 ====================

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    username VARCHAR(255) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(64) COMMENT '昵称',
    phone VARCHAR(32) COMMENT '手机号',
    email VARCHAR(64) COMMENT '邮箱',
    last_login_time DATETIME COMMENT '上次登录时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0=禁用, 1=启用',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 通知公告表
CREATE TABLE IF NOT EXISTS sys_notice (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    notice_title VARCHAR(255) NOT NULL COMMENT '公告标题',
    notice_type TINYINT NOT NULL DEFAULT 0 COMMENT '公告类型: 0=公告, 1=通知, 2=提醒',
    notice_content TEXT COMMENT '公告内容',
    create_by VARCHAR(128) NOT NULL COMMENT '创建者',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0=正常, 1=关闭',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 登录日志表
CREATE TABLE IF NOT EXISTS sys_login_log (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '登录访问ID',
    login_name VARCHAR(50) DEFAULT '' COMMENT '登录账号',
    ip_address VARCHAR(128) DEFAULT '' COMMENT '登录IP地址',
    login_location VARCHAR(255) DEFAULT '' COMMENT '登录地点',
    browser_type VARCHAR(500) DEFAULT '' COMMENT '浏览器类型',
    os VARCHAR(50) DEFAULT '' COMMENT '操作系统',
    status TINYINT DEFAULT 0 COMMENT '登录状态: 0=成功, 1=失败',
    msg TEXT COMMENT '提示消息',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 操作日志表
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    operation_ip VARCHAR(128) DEFAULT '0' COMMENT '主机地址',
    oper_location VARCHAR(255) DEFAULT '' COMMENT '操作地点',
    method TEXT COMMENT '方法名',
    args TEXT COMMENT '参数',
    operation_name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '操作人',
    operation_type VARCHAR(50) NOT NULL DEFAULT '' COMMENT '操作类型',
    return_value TEXT COMMENT '返回参数',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- ==================== 生产管理模块 ====================

-- 生产订单表
CREATE TABLE IF NOT EXISTS mes_production_order (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    order_no VARCHAR(64) NOT NULL COMMENT '订单编号',
    product_model VARCHAR(64) NOT NULL COMMENT '产品型号',
    batch_no VARCHAR(64) NOT NULL COMMENT '批次号',
    planned_qty INT NOT NULL DEFAULT 0 COMMENT '计划数量',
    completed_qty INT NOT NULL DEFAULT 0 COMMENT '已完成数量',
    production_line VARCHAR(64) NOT NULL COMMENT '生产线',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0=待排产, 1=生产中, 2=已完成',
    delivery_date DATE COMMENT '交付日期',
    create_by VARCHAR(64) COMMENT '创建人',
    remark VARCHAR(500) COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 工单表
CREATE TABLE IF NOT EXISTS pro_work_order (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    work_order_no VARCHAR(64) NOT NULL COMMENT '工单编号',
    material_id BIGINT COMMENT '物料ID',
    plan_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '计划数量',
    actual_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '实际数量',
    plan_start_time DATETIME COMMENT '计划开始时间',
    plan_end_time DATETIME COMMENT '计划结束时间',
    actual_start_time DATETIME COMMENT '实际开始时间',
    actual_end_time DATETIME COMMENT '实际结束时间',
    route_id BIGINT COMMENT '工艺路线ID',
    source_type TINYINT COMMENT '来源类型: 1=手动, 2=Excel, 3=ERP, 4=排产下发',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=待生产, 2=进行中, 3=已完成, 4=已关闭',
    priority TINYINT DEFAULT 0 COMMENT '优先级: 0=普通, 1=紧急, 2=关键',
    remark VARCHAR(500) COMMENT '备注',
    create_by BIGINT COMMENT '创建人ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 生产任务表
CREATE TABLE IF NOT EXISTS pro_task (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    task_no VARCHAR(64) NOT NULL COMMENT '任务编号',
    work_order_id BIGINT COMMENT '工单ID',
    dispatch_id BIGINT COMMENT '派工ID',
    production_line VARCHAR(64) COMMENT '生产线',
    plan_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '计划数量',
    complete_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '已完成数量',
    defect_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '不良数量',
    source_type TINYINT COMMENT '来源类型: 1=手动, 2=Excel, 3=ERP, 4=排产下发',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=待生产, 2=进行中, 3=已完工, 4=暂停',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 派工表
CREATE TABLE IF NOT EXISTS pro_dispatch (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    dispatch_no VARCHAR(64) NOT NULL COMMENT '派工编号',
    work_order_id BIGINT COMMENT '工单ID',
    operation_id BIGINT COMMENT '工序ID',
    production_line VARCHAR(64) COMMENT '生产线',
    plan_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '计划数量',
    actual_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '实际数量',
    plan_start_time DATETIME COMMENT '计划开始时间',
    plan_end_time DATETIME COMMENT '计划结束时间',
    assignee_id BIGINT COMMENT '指派人ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=待执行, 2=执行中, 3=已完成, 4=暂停',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 完工记录表
CREATE TABLE IF NOT EXISTS pro_completion (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    completion_no VARCHAR(64) NOT NULL COMMENT '完工单号',
    work_order_id BIGINT COMMENT '工单ID',
    material_id BIGINT COMMENT '物料ID',
    plan_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '计划数量',
    completed_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '已完成数量',
    qualified_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '合格数量',
    defect_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '不良数量',
    warehouse_id BIGINT COMMENT '入库仓库ID',
    location_id BIGINT COMMENT '库位ID',
    completion_time DATETIME COMMENT '完工时间',
    inspector_id BIGINT COMMENT '检验员ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=待检验, 2=已检验, 3=已关闭',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 返修表
CREATE TABLE IF NOT EXISTS pro_repair (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    repair_no VARCHAR(64) NOT NULL COMMENT '返修单号',
    work_order_id BIGINT COMMENT '工单ID',
    completion_id BIGINT COMMENT '完工记录ID',
    material_id BIGINT COMMENT '物料ID',
    barcode VARCHAR(128) COMMENT '产品条码',
    repair_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '返修数量',
    repair_reason VARCHAR(500) COMMENT '返修原因',
    repair_type TINYINT COMMENT '返修类型: 1=内部返修, 2=外部返修',
    assignee_id BIGINT COMMENT '指派人ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=待返修, 2=返修中, 3=返修完成, 4=已报废',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 生产报工表
CREATE TABLE IF NOT EXISTS pro_report (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    report_no VARCHAR(50) NOT NULL COMMENT '报工单号',
    order_id BIGINT COMMENT '工单ID',
    task_id BIGINT COMMENT '关联任务ID',
    operation_id BIGINT COMMENT '关联工序ID',
    process_name VARCHAR(128) COMMENT '工序名称',
    worker VARCHAR(64) COMMENT '工人姓名',
    workstation VARCHAR(64) COMMENT '工位',
    worker_id BIGINT COMMENT '操作工人ID',
    report_type TINYINT DEFAULT 1 COMMENT '报工类型: 1=正常报工',
    report_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '本次报工数量',
    qualified_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '合格品数量',
    defect_quantity DECIMAL(12,3) DEFAULT 0 COMMENT '不良品数量',
    defect_reason VARCHAR(255) COMMENT '不良原因描述',
    equipment_id BIGINT COMMENT '关联设备ID',
    barcode VARCHAR(100) COMMENT '产品条码',
    report_date DATE COMMENT '报工日期',
    shift VARCHAR(20) COMMENT '班次',
    report_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报工时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- ==================== 工艺管理模块 ====================

-- 工序表
CREATE TABLE IF NOT EXISTS prc_operation (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    operation_code VARCHAR(64) NOT NULL COMMENT '工序编码',
    operation_name VARCHAR(128) NOT NULL COMMENT '工序名称',
    operation_type TINYINT COMMENT '工序类型: 1=加工, 2=检验, 3=包装, 4=组装',
    sop_id BIGINT COMMENT 'SOP文件ID',
    description VARCHAR(500) COMMENT '描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0=禁用, 1=启用',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 工艺路线表
CREATE TABLE IF NOT EXISTS prc_route (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    route_code VARCHAR(64) NOT NULL COMMENT '路线编码',
    route_name VARCHAR(128) NOT NULL COMMENT '路线名称',
    material_id BIGINT COMMENT '物料ID',
    version VARCHAR(32) COMMENT '版本',
    description VARCHAR(500) COMMENT '描述',
    product_model VARCHAR(64) COMMENT '产品型号',
    process_ids TEXT COMMENT '工序ID序列(逗号分隔)',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0=禁用, 1=启用',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 工艺路线明细表
CREATE TABLE IF NOT EXISTS prc_route_detail (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    route_id BIGINT NOT NULL COMMENT '路线ID',
    operation_id BIGINT NOT NULL COMMENT '工序ID',
    seq_no INT COMMENT '顺序号',
    standard_time DECIMAL(10,4) COMMENT '标准工时',
    price DECIMAL(10,4) COMMENT '单价',
    prev_op_id BIGINT COMMENT '前工序ID',
    next_op_id BIGINT COMMENT '后工序ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- SOP文件表
CREATE TABLE IF NOT EXISTS prc_sop (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    sop_name VARCHAR(128) NOT NULL COMMENT 'SOP名称',
    operation_id BIGINT COMMENT '工序ID',
    file_name VARCHAR(255) COMMENT '文件名',
    file_type VARCHAR(32) COMMENT '文件类型',
    file_url VARCHAR(500) COMMENT '文件URL',
    file_size BIGINT COMMENT '文件大小',
    auto_play TINYINT DEFAULT 0 COMMENT '是否自动播放: 0=否, 1=是',
    sort_order INT DEFAULT 0 COMMENT '排序',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- ==================== 设备管理模块 ====================

-- 设备台账表
CREATE TABLE IF NOT EXISTS eqp_ledger (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    equipment_code VARCHAR(64) NOT NULL COMMENT '设备编码',
    equipment_name VARCHAR(128) NOT NULL COMMENT '设备名称',
    category VARCHAR(64) COMMENT '设备分类',
    equipment_model VARCHAR(64) COMMENT '设备型号',
    manufacturer VARCHAR(128) COMMENT '制造商',
    workshop VARCHAR(64) COMMENT '所属车间',
    serial_no VARCHAR(64) COMMENT '序列号',
    purchase_date DATE COMMENT '采购日期',
    warranty_date DATE COMMENT '保修截止日期',
    location VARCHAR(128) COMMENT '位置',
    production_line VARCHAR(64) COMMENT '生产线',
    rated_power DECIMAL(10,2) COMMENT '额定功率',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0=正常, 1=停用, 2=维修中, 3=报废',
    remark VARCHAR(500) COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 设备保养任务表
CREATE TABLE IF NOT EXISTS eqp_maintenance (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    task_no VARCHAR(64) COMMENT '任务编号',
    equipment_id BIGINT NOT NULL COMMENT '设备ID',
    maintenance_type VARCHAR(32) COMMENT '保养类型: 日常保养/一级保养/二级保养',
    plan_date DATE COMMENT '计划日期',
    actual_date DATE COMMENT '实际日期',
    maintenance_content TEXT COMMENT '保养内容',
    operator VARCHAR(64) COMMENT '保养人',
    result VARCHAR(500) COMMENT '保养结果',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0=待保养, 1=已完成, 2=逾期',
    remark VARCHAR(500) COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 设备点检表
CREATE TABLE IF NOT EXISTS eqp_inspection (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    inspection_no VARCHAR(64) COMMENT '点检编号',
    equipment_id BIGINT NOT NULL COMMENT '设备ID',
    inspection_date DATETIME COMMENT '点检日期',
    inspector VARCHAR(64) COMMENT '点检人',
    inspection_items TEXT COMMENT '点检项目',
    result TINYINT NOT NULL DEFAULT 0 COMMENT '结果: 0=正常, 1=异常',
    remark VARCHAR(500) COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 设备维修表
CREATE TABLE IF NOT EXISTS eqp_repair (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    repair_no VARCHAR(64) COMMENT '维修编号',
    equipment_id BIGINT NOT NULL COMMENT '设备ID',
    fault_type VARCHAR(64) COMMENT '故障类型',
    fault_reason VARCHAR(255) COMMENT '故障原因',
    failure_desc TEXT COMMENT '故障描述',
    reporter VARCHAR(64) COMMENT '报修人',
    repair_person VARCHAR(64) COMMENT '维修人',
    report_time DATETIME COMMENT '报修时间',
    repair_time DATETIME COMMENT '维修时间',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    repair_content TEXT COMMENT '维修内容',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0=待维修, 1=维修中, 2=已修复, 3=无法修复',
    remark VARCHAR(500) COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- ==================== 质量管理模块 ====================

-- 质量检验表
CREATE TABLE IF NOT EXISTS mes_quality_inspection (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    report_no VARCHAR(64) NOT NULL COMMENT '检验报告编号',
    order_id BIGINT COMMENT '生产订单ID',
    product_model VARCHAR(64) NOT NULL COMMENT '产品型号',
    batch_no VARCHAR(64) NOT NULL COMMENT '批次号',
    inspection_type VARCHAR(32) NOT NULL COMMENT '检验类型: 来料检验/过程检验/成品检验/首件检验',
    sample_qty INT NOT NULL DEFAULT 0 COMMENT '抽样数量',
    defect_qty INT NOT NULL DEFAULT 0 COMMENT '不良数量',
    result TINYINT NOT NULL DEFAULT 0 COMMENT '结果: 0=合格, 1=不合格',
    inspector VARCHAR(64) NOT NULL COMMENT '检验员',
    remark VARCHAR(500) COMMENT '备注',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- ==================== 安灯(Andon)管理模块 ====================

-- 安灯配置表
CREATE TABLE IF NOT EXISTS andon_config (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    andon_type VARCHAR(64) NOT NULL COMMENT '安灯类型',
    andon_name VARCHAR(128) NOT NULL COMMENT '安灯名称',
    handle_type TINYINT DEFAULT 1 COMMENT '处理类型: 1=仅记录, 2=自行处理, 3=协助处理',
    notify_users TEXT COMMENT '通知用户(逗号分隔用户ID)',
    notify_method TINYINT DEFAULT 1 COMMENT '通知方式: 1=系统消息, 2=看板显示, 3=消息+看板',
    production_line VARCHAR(64) COMMENT '生产线',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0=禁用, 1=启用',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 安灯异常记录表
CREATE TABLE IF NOT EXISTS andon_record (
    id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    andon_config_id BIGINT COMMENT '安灯配置ID',
    reason_id BIGINT COMMENT '异常原因ID',
    production_line VARCHAR(50) COMMENT '生产线',
    workstation VARCHAR(64) COMMENT '工位',
    exception_type VARCHAR(64) COMMENT '异常类型',
    initiator_id BIGINT NOT NULL COMMENT '安灯发起人ID',
    handler_id BIGINT COMMENT '实际处理人ID',
    operation_id BIGINT COMMENT '工序ID',
    equipment_id BIGINT COMMENT '设备ID',
    description VARCHAR(500) COMMENT '异常描述',
    report_user VARCHAR(64) COMMENT '上报人',
    handle_user VARCHAR(64) COMMENT '处理人',
    init_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发起时间',
    confirm_time DATETIME COMMENT '确认时间',
    handle_time DATETIME COMMENT '处理完成时间',
    handle_result VARCHAR(500) COMMENT '处理结果',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1=未确认, 2=已确认处理中, 3=已处理, 4=已关闭',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- ==================== 条码管理模块 ====================

-- 条码规则表
CREATE TABLE IF NOT EXISTS mes_barcode_rule (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(128) NOT NULL COMMENT '规则名称',
    rule_type VARCHAR(32) COMMENT '规则类型',
    prefix VARCHAR(32) COMMENT '前缀',
    date_format VARCHAR(32) COMMENT '日期格式',
    serial_length INT DEFAULT 6 COMMENT '流水号长度',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0=启用, 1=停用',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 条码记录表
CREATE TABLE IF NOT EXISTS mes_barcode_record (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    barcode VARCHAR(128) NOT NULL COMMENT '条码',
    product_model VARCHAR(64) COMMENT '产品型号',
    batch_no VARCHAR(64) COMMENT '批次号',
    order_no VARCHAR(64) COMMENT '订单编号',
    production_line VARCHAR(64) COMMENT '生产线',
    print_time DATETIME COMMENT '打印时间',
    print_user VARCHAR(64) COMMENT '打印人',
    rule_id BIGINT COMMENT '条码规则ID',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- ==================== 工资管理模块 ====================

-- 计件工资表
CREATE TABLE IF NOT EXISTS mes_piece_wage (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    worker VARCHAR(64) NOT NULL COMMENT '工人',
    process_name VARCHAR(128) NOT NULL COMMENT '工序名称',
    unit_price DECIMAL(10,3) NOT NULL DEFAULT 0 COMMENT '单价',
    completed_qty INT NOT NULL DEFAULT 0 COMMENT '完成数量',
    total_wage DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '总工资',
    wage_date DATE COMMENT '工资日期',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0=待结算, 1=已结算',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- ==================== 追溯管理模块 ====================

-- 物料追溯表
CREATE TABLE IF NOT EXISTS mes_material_trace (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    order_id BIGINT COMMENT '生产订单ID',
    material_code VARCHAR(64) NOT NULL COMMENT '物料编码',
    material_name VARCHAR(128) COMMENT '物料名称',
    batch_no VARCHAR(64) COMMENT '批次号',
    supplier VARCHAR(128) COMMENT '供应商',
    quantity DECIMAL(12,3) COMMENT '用量',
    unit VARCHAR(16) COMMENT '单位',
    operation_name VARCHAR(128) COMMENT '工序名称',
    operation_time DATETIME COMMENT '投料时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- 产品追溯表
CREATE TABLE IF NOT EXISTS mes_product_trace (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    order_id BIGINT COMMENT '生产订单ID',
    barcode VARCHAR(128) NOT NULL COMMENT '产品条码',
    product_model VARCHAR(64) COMMENT '产品型号',
    batch_no VARCHAR(64) COMMENT '批次号',
    production_line VARCHAR(64) COMMENT '生产线',
    inspection_result TINYINT DEFAULT 0 COMMENT '检验结果: 0=合格, 1=不合格',
    operator VARCHAR(64) COMMENT '操作人',
    production_time DATETIME COMMENT '生产时间',
    is_deleted TINYINT NOT NULL DEFAULT 0 COMMENT '软删除: 0=未删除, 1=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_bin;

-- ============================================================
-- 初始化种子数据 (idempotent: 先删后插)
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM sys_user WHERE id <= 100;
DELETE FROM sys_notice WHERE id <= 100;
DELETE FROM sys_login_log;
DELETE FROM sys_operation_log;
DELETE FROM mes_production_order WHERE id <= 100;
DELETE FROM pro_work_order WHERE id <= 100;
DELETE FROM pro_task WHERE id <= 100;
DELETE FROM pro_dispatch WHERE id <= 100;
DELETE FROM pro_completion WHERE id <= 100;
DELETE FROM pro_repair WHERE id <= 100;
DELETE FROM pro_report WHERE id <= 100;
DELETE FROM prc_route_detail WHERE id <= 100;
DELETE FROM prc_route WHERE id <= 100;
DELETE FROM prc_operation WHERE id <= 100;
DELETE FROM prc_sop WHERE id <= 100;
DELETE FROM eqp_ledger WHERE id <= 100;
DELETE FROM eqp_maintenance WHERE id <= 100;
DELETE FROM eqp_inspection WHERE id <= 100;
DELETE FROM eqp_repair WHERE id <= 100;
DELETE FROM mes_quality_inspection WHERE id <= 100;
DELETE FROM andon_config WHERE id <= 100;
DELETE FROM andon_record WHERE id <= 100;
DELETE FROM mes_barcode_rule WHERE id <= 100;
DELETE FROM mes_barcode_record WHERE id <= 100;
DELETE FROM mes_piece_wage WHERE id <= 100;

SET FOREIGN_KEY_CHECKS = 1;

-- ==================== 系统管理 - 用户 ====================

-- 默认管理员 (密码: admin123)
INSERT INTO sys_user (id, username, password, nickname, phone, email, status) VALUES
(1, 'admin', '$2a$10$57nGJ4jGN4C/.4VwJJJtmOVl3aCV1wTLqQoC0ITRsJTpbxkV8G8n.', '系统管理员', '13800000000', 'admin@mes.com', 1);

-- ==================== 系统管理 - 通知公告 ====================

INSERT INTO sys_notice (id, notice_title, notice_type, notice_content, create_by, status) VALUES
(1, '系统上线通知', 1, 'MES制造执行系统 V2.0 已正式上线，请各部门熟悉新系统操作流程。', 'admin', 0),
(2, '季度设备保养计划', 0, '2026年Q3设备保养计划已发布，请各车间按期完成保养任务并提交记录。', 'admin', 0),
(3, '安全生产提醒', 2, '高温天气请注意车间通风和防暑降温，严格遵守安全操作规程。', 'admin', 0);

-- ==================== 工艺管理 - 工序 ====================

INSERT INTO prc_operation (id, operation_code, operation_name, operation_type, description, status) VALUES
(1, 'OP-MIX', '配料搅拌', 1, '正负极粉料混合搅拌', 1),
(2, 'OP-STAMP', '锌筒冲压', 1, '锌筒成型冲压', 1),
(3, 'OP-GLUE', '注胶封口', 1, '注入密封胶', 1),
(4, 'OP-ASSEMBLE', '装芯装配', 4, '将电芯装入钢壳', 1),
(5, 'OP-SEAL', '封口成型', 1, '电池封口成型', 1),
(6, 'OP-INSPECT', '电性能检测', 2, '开路电压、负载电压检测', 1),
(7, 'OP-PACK', '包装入库', 3, '热缩包装、装箱', 1);

-- ==================== 工艺管理 - 工艺路线 ====================

INSERT INTO prc_route (id, route_code, route_name, product_model, process_ids, version, description, status) VALUES
(1, 'RT-LR6-001', 'LR6 AA标准工艺路线', 'LR6', '1,2,3,4,5,6,7', 'V1.0', '碱性锌锰电池LR6 AA标准生产工艺路线', 1),
(2, 'RT-LR03-001', 'LR03 AAA标准工艺路线', 'LR03', '1,2,3,4,5,6,7', 'V1.0', '碱性锌锰电池LR03 AAA标准生产工艺路线', 1);

-- ==================== 工艺管理 - 工艺路线明细 ====================

INSERT INTO prc_route_detail (id, route_id, operation_id, seq_no, standard_time, price) VALUES
(1, 1, 1, 1, 2.0000, 0.0180),
(2, 1, 2, 2, 0.5000, 0.0120),
(3, 1, 3, 3, 0.3000, 0.0100),
(4, 1, 4, 4, 0.8000, 0.0150),
(5, 1, 5, 5, 0.5000, 0.0120),
(6, 1, 6, 6, 0.2000, 0.0080),
(7, 1, 7, 7, 1.0000, 0.0100);

-- ==================== 设备管理 - 设备台账 ====================

INSERT INTO eqp_ledger (id, equipment_code, equipment_name, category, equipment_model, manufacturer, workshop, serial_no, production_line, status) VALUES
(1, 'EQ-MX-001', '双行星搅拌机A', '搅拌设备', 'SXJ-500L', '鑫达机械', '一车间', 'SN-MX-2024001', 'A线-正极装配', 0),
(2, 'EQ-MX-002', '真空搅拌机B', '搅拌设备', 'ZKJ-300L', '鑫达机械', '一车间', 'SN-MX-2024002', 'B线-负极装配', 0),
(3, 'EQ-AS-001', '钢壳装配线A', '装配设备', 'GZ-2000', '华工智能', '一车间', 'SN-AS-2024001', 'A线-正极装配', 0),
(4, 'EQ-AS-002', '钢壳装配线B', '装配设备', 'GZ-2000', '华工智能', '一车间', 'SN-AS-2024002', 'B线-负极装配', 0),
(5, 'EQ-SL-001', '自动封口机A', '封口设备', 'FK-1500B', '精工科技', '一车间', 'SN-SL-2024001', 'C线-封口包装', 1),
(6, 'EQ-SL-002', '自动封口机B', '封口设备', 'FK-1500B', '精工科技', '一车间', 'SN-SL-2024002', 'C线-封口包装', 0),
(7, 'EQ-TS-001', '电性能测试仪A', '检测设备', 'DX-8000', '赛普仪器', '品质实验室', 'SN-TS-2024001', 'D线-检测分选', 0),
(8, 'EQ-TS-002', '电池分选机B', '检测设备', 'FX-2000', '赛普仪器', '品质实验室', 'SN-TS-2024002', 'D线-检测分选', 0),
(9, 'EQ-PK-001', '热缩包装线A', '包装设备', 'RS-1200', '达盛包装', '二车间', 'SN-PK-2024001', 'C线-封口包装', 0),
(10, 'EQ-PK-002', '自动装箱线B', '包装设备', 'ZX-800', '达盛包装', '二车间', 'SN-PK-2024002', 'C线-封口包装', 2);

-- ==================== 生产管理 - 生产订单 ====================

INSERT INTO mes_production_order (id, order_no, product_model, batch_no, planned_qty, completed_qty, production_line, status, delivery_date, create_by, remark) VALUES
(1, 'MO-20260715-001', 'LR6', 'BAT-2026Q3-A', 50000, 42500, 'A线-正极装配', 1, '2026-07-21', 'admin', 'Q3季度常规订单'),
(2, 'MO-20260715-002', 'LR03', 'BAT-2026Q3-B', 80000, 80000, 'B线-负极装配', 2, '2026-07-22', 'admin', 'Q3季度常规订单'),
(3, 'MO-20260715-003', '6LR61', 'BAT-2026Q3-C', 20000, 12800, 'A线-正极装配', 1, '2026-07-28', 'admin', '9V电池紧急订单'),
(4, 'MO-20260715-004', 'LR14', 'BAT-2026Q3-D', 30000, 30000, 'B线-负极装配', 2, '2026-07-25', 'admin', 'C型电池订单'),
(5, 'MO-20260715-005', 'LR20', 'BAT-2026Q3-E', 15000, 0, 'C线-封口包装', 0, '2026-08-01', 'admin', 'D型电池预排产');

-- ==================== 生产管理 - 工单 ====================

INSERT INTO pro_work_order (id, work_order_no, plan_quantity, actual_quantity, plan_start_time, plan_end_time, route_id, source_type, status, priority, create_by) VALUES
(1, 'WO-20260715-001', 50000.000, 42500.000, '2026-07-15 08:00:00', '2026-07-21 18:00:00', 1, 1, 2, 1, 1),
(2, 'WO-20260715-002', 80000.000, 80000.000, '2026-07-15 08:00:00', '2026-07-22 18:00:00', 2, 1, 3, 1, 1),
(3, 'WO-20260715-003', 20000.000, 12800.000, '2026-07-15 08:00:00', '2026-07-28 18:00:00', 1, 1, 2, 2, 1),
(4, 'WO-20260715-004', 30000.000, 30000.000, '2026-07-15 08:00:00', '2026-07-25 18:00:00', 2, 1, 3, 1, 1),
(5, 'WO-20260715-005', 15000.000, 0.000, '2026-07-22 08:00:00', '2026-08-01 18:00:00', 1, 1, 1, 0, 1);

-- ==================== 安灯管理 - 安灯配置 ====================

INSERT INTO andon_config (id, andon_type, andon_name, handle_type, notify_method, production_line, status) VALUES
(1, '设备故障', '设备停机报警', 3, 3, 'A线-正极装配', 1),
(2, '质量异常', '外观缺陷报警', 2, 2, 'B线-负极装配', 1),
(3, '物料短缺', '物料不足报警', 3, 1, 'A线-正极装配', 1),
(4, '模具异常', '模具更换提醒', 1, 2, 'C线-封口包装', 1);

-- ==================== 安灯管理 - 安灯异常记录 ====================

INSERT INTO andon_record (id, andon_config_id, production_line, workstation, exception_type, initiator_id, equipment_id, description, report_user, init_time, status) VALUES
(1, 3, 'A线-正极装配', 'WC-ASSEMBLE', '物料短缺', 2, 3, '钢壳库存不足，仅剩2000个，需紧急补料', '张三', '2026-07-15 14:00:00', 1),
(2, 1, 'B线-负极装配', 'WC-SEAL', '设备故障', 2, 6, '封口机卡料，需停机清理', '张三', '2026-07-15 15:00:00', 2),
(3, 2, 'A线-正极装配', 'WC-INSPECT', '质量异常', 2, 7, '开路电压检测超标0.02V，需排查原因', '张三', '2026-07-15 16:00:00', 3);

-- ==================== 条码管理 - 条码规则 ====================

INSERT INTO mes_barcode_rule (id, rule_code, rule_name, rule_type, prefix, date_format, serial_length, status) VALUES
(1, 'BC-LR6', 'LR6电池条码规则', 'PRODUCT', 'LR6', 'yyMMdd', 6, 0),
(2, 'BC-LR03', 'LR03电池条码规则', 'PRODUCT', 'L03', 'yyMMdd', 6, 0),
(3, 'BC-MAT', '物料条码规则', 'MATERIAL', 'MAT', 'yyMMdd', 5, 0);

-- ==================== 条码管理 - 条码记录 ====================

INSERT INTO mes_barcode_record (id, barcode, product_model, batch_no, order_no, production_line, print_user, rule_id, print_time) VALUES
(1, 'LR6240715000001', 'LR6', 'BAT-2026Q3-A', 'MO-20260715-001', 'A线-正极装配', '张三', 1, '2026-07-15 08:30:00'),
(2, 'LR6240715000002', 'LR6', 'BAT-2026Q3-A', 'MO-20260715-001', 'A线-正极装配', '张三', 1, '2026-07-15 08:30:00'),
(3, 'L03240715000001', 'LR03', 'BAT-2026Q3-B', 'MO-20260715-002', 'B线-负极装配', '张三', 2, '2026-07-15 09:00:00');

-- ==================== 工资管理 - 计件工资 ====================

INSERT INTO mes_piece_wage (id, worker, process_name, unit_price, completed_qty, total_wage, wage_date, status) VALUES
(1, '张三', '配料搅拌', 0.018, 500, 9.00, '2026-07-15', 1),
(2, '张三', '装芯装配', 0.015, 800, 12.00, '2026-07-15', 1),
(3, '李四', '封口成型', 0.012, 1200, 14.40, '2026-07-15', 1),
(4, '王五', '包装入库', 0.010, 2000, 20.00, '2026-07-15', 0);

-- ==================== 操作日志 - 样例数据 ====================

INSERT INTO sys_operation_log (operation_ip, oper_location, method, operation_name, operation_type, create_time) VALUES
('127.0.0.1', '内网', 'com.example.mes.modules.system.controller.AuthController.login', 'admin', '登录', '2026-07-15 08:00:00'),
('192.168.1.100', '一车间', 'com.example.mes.modules.production.controller.ProductionOrderController.create', 'admin', '新增生产订单', '2026-07-15 09:00:00'),
('192.168.1.100', '一车间', 'com.example.mes.modules.production.controller.ProWorkOrderController.create', 'admin', '新增工单', '2026-07-15 09:30:00'),
('192.168.1.101', '品质实验室', 'com.example.mes.modules.quality.controller.QualityInspectionController.create', 'admin', '新增质检报告', '2026-07-15 10:00:00');
