-- =============================================
-- MES System Database Initialization Script
-- Alkaline Zinc-Manganese Battery Manufacturing
-- =============================================

-- MySQL Database Initialization Script
-- MySQL 5.7+ / 8.0+ | Alibaba Java Development Manual (Huangshan Edition) compliant
-- CHAR SET: utf8mb4

-- =============================================
-- System Management Tables
-- =============================================

-- Department table
CREATE TABLE IF NOT EXISTS sys_department (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0 COMMENT 'Parent Department ID',
    dept_name VARCHAR(100) NOT NULL COMMENT 'Department Name',
    dept_code VARCHAR(50) COMMENT 'Department Code',
    leader VARCHAR(50) COMMENT 'Department Leader',
    phone VARCHAR(20) COMMENT 'Phone',
    sort_order INT DEFAULT 0,
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_dept_code (dept_code)
) COMMENT 'System Department' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- User table
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL COMMENT 'Username',
    password VARCHAR(255) NOT NULL COMMENT 'Password',
    real_name VARCHAR(50) COMMENT 'Real Name',
    email VARCHAR(100) COMMENT 'Email',
    phone VARCHAR(20) COMMENT 'Phone',
    dept_id BIGINT COMMENT 'Department ID',
    status TINYINT UNSIGNED DEFAULT 1 COMMENT 'Status: 0-Disabled, 1-Enabled',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_username (username),
    INDEX idx_dept_id (dept_id)
) COMMENT 'System User' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Role table
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL COMMENT 'Role Name',
    role_code VARCHAR(50) NOT NULL COMMENT 'Role Code',
    description VARCHAR(200) COMMENT 'Description',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_role_code (role_code)
) COMMENT 'System Role' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- User-Role relationship
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT 'User ID',
    role_id BIGINT NOT NULL COMMENT 'Role ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) COMMENT 'User-Role Relationship' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Menu table
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0 COMMENT 'Parent Menu ID',
    menu_name VARCHAR(50) NOT NULL COMMENT 'Menu Name',
    menu_code VARCHAR(50) COMMENT 'Menu Code',
    menu_type CHAR(1) DEFAULT 'M' COMMENT 'M-Directory, C-Menu, F-Button',
    url VARCHAR(200) COMMENT 'URL',
    icon VARCHAR(50) COMMENT 'Icon',
    sort_order INT DEFAULT 0 COMMENT 'Sort Order',
    is_visible TINYINT UNSIGNED DEFAULT 1 COMMENT 'Visible: 0-No, 1-Yes',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_menu_code (menu_code)
) COMMENT 'System Menu' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Role-Menu relationship
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    role_id BIGINT NOT NULL COMMENT 'Role ID',
    menu_id BIGINT NOT NULL COMMENT 'Menu ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_role_id (role_id),
    INDEX idx_menu_id (menu_id)
) COMMENT 'Role-Menu Relationship' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- System configuration
CREATE TABLE IF NOT EXISTS sys_config (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT 'Config Name',
    config_key VARCHAR(100) NOT NULL COMMENT 'Config Key',
    config_value VARCHAR(500) COMMENT 'Config Value',
    config_type VARCHAR(20) DEFAULT 'STRING' COMMENT 'Value Type',
    description VARCHAR(200) COMMENT 'Description',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_config_key (config_key)
) COMMENT 'System Configuration' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Operation log
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    user_id BIGINT COMMENT 'User ID',
    username VARCHAR(50) COMMENT 'Username',
    operation VARCHAR(100) COMMENT 'Operation',
    method VARCHAR(200) COMMENT 'Request Method',
    params TEXT COMMENT 'Request Parameters',
    ip VARCHAR(50) COMMENT 'IP Address',
    status TINYINT UNSIGNED DEFAULT 1 COMMENT '0-Fail, 1-Success',
    error_msg VARCHAR(2000) COMMENT 'Error Message',
    duration BIGINT COMMENT 'Duration(ms)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) COMMENT 'Operation Log' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Product & Material Master Data
-- =============================================

-- Product/BOM table
CREATE TABLE IF NOT EXISTS md_product (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    product_code VARCHAR(50) NOT NULL COMMENT 'Product Code',
    product_name VARCHAR(100) NOT NULL COMMENT 'Product Name',
    product_spec VARCHAR(100) COMMENT 'Specification',
    product_type VARCHAR(50) COMMENT 'Product Type: SEMI, FINISHED',
    unit VARCHAR(20) COMMENT 'Unit',
    category VARCHAR(50) COMMENT 'Category',
    voltage VARCHAR(20) COMMENT 'Voltage(V)',
    capacity VARCHAR(20) COMMENT 'Capacity(mAh)',
    weight DECIMAL(10,4) COMMENT 'Weight(g)',
    size_diameter DECIMAL(10,2) COMMENT 'Diameter(mm)',
    size_height DECIMAL(10,2) COMMENT 'Height(mm)',
    route_id BIGINT COMMENT 'Default Route ID',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_product_code (product_code),
    INDEX idx_route_id (route_id),
    INDEX idx_category (category)
) COMMENT 'Product Master' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Material category
CREATE TABLE IF NOT EXISTS md_material_category (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0,
    category_name VARCHAR(50) NOT NULL COMMENT 'Category Name',
    category_code VARCHAR(50) NOT NULL COMMENT 'Category Code',
    sort_order INT DEFAULT 0,
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_category_code (category_code),
    INDEX idx_parent_id (parent_id)
) COMMENT 'Material Category' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Material master
CREATE TABLE IF NOT EXISTS md_material (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    material_code VARCHAR(50) NOT NULL COMMENT 'Material Code',
    material_name VARCHAR(100) NOT NULL COMMENT 'Material Name',
    material_spec VARCHAR(100) COMMENT 'Specification',
    category_id BIGINT COMMENT 'Category ID',
    unit VARCHAR(20) COMMENT 'Unit',
    safety_stock DECIMAL(10,2) DEFAULT 0 COMMENT 'Safety Stock',
    current_stock DECIMAL(10,2) DEFAULT 0 COMMENT 'Current Stock',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_material_code (material_code),
    INDEX idx_category_id (category_id)
) COMMENT 'Material Master' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Production Order Tables
-- =============================================

-- Production Work Order
CREATE TABLE IF NOT EXISTS prod_work_order (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL COMMENT 'Work Order No',
    product_code VARCHAR(50) COMMENT 'Product Code',
    product_name VARCHAR(100) COMMENT 'Product Name',
    quantity DECIMAL(10,2) COMMENT 'Order Quantity',
    completed_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Completed Quantity',
    defective_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Defective Quantity',
    start_date DATE COMMENT 'Start Date',
    end_date DATE COMMENT 'End Date',
    current_process VARCHAR(50) COMMENT 'Current Process',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT 'DRAFT, RELEASED, IN_PROGRESS, COMPLETED, CLOSED',
    workshop VARCHAR(50) COMMENT 'Workshop',
    production_line VARCHAR(50) COMMENT 'Production Line',
    shift VARCHAR(20) COMMENT 'Shift',
    priority INT DEFAULT 0 COMMENT 'Priority',
    source_order_no VARCHAR(50) COMMENT 'Source Order/Sales Order No',
    plan_id BIGINT COMMENT 'Plan ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_product_code (product_code),
    INDEX idx_status (status),
    INDEX idx_plan_id (plan_id),
    INDEX idx_production_line (production_line)
) COMMENT 'Production Work Order' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dispatch Order
CREATE TABLE IF NOT EXISTS prod_dispatch_order (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    dispatch_no VARCHAR(50) NOT NULL COMMENT 'Dispatch No',
    work_order_id BIGINT COMMENT 'Work Order ID',
    product_code VARCHAR(50) COMMENT 'Product Code',
    quantity DECIMAL(10,2) COMMENT 'Quantity',
    process_code VARCHAR(50) COMMENT 'Process Code',
    process_name VARCHAR(100) COMMENT 'Process Name',
    work_center VARCHAR(50) COMMENT 'Work Center',
    equipment_id BIGINT COMMENT 'Equipment ID',
    assigned_to VARCHAR(50) COMMENT 'Assigned Worker',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, IN_PROGRESS, COMPLETED',
    plan_start_time DATETIME COMMENT 'Plan Start Time',
    plan_end_time DATETIME COMMENT 'Plan End Time',
    actual_start_time DATETIME COMMENT 'Actual Start Time',
    actual_end_time DATETIME COMMENT 'Actual End Time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_dispatch_no (dispatch_no),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_equipment_id (equipment_id),
    INDEX idx_status (status)
) COMMENT 'Production Dispatch Order' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Production Reporting (工序报工)
CREATE TABLE IF NOT EXISTS prod_reporting (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    work_order_id BIGINT COMMENT 'Work Order ID',
    dispatch_id BIGINT COMMENT 'Dispatch ID',
    process_code VARCHAR(50) COMMENT 'Process Code',
    process_name VARCHAR(100) COMMENT 'Process Name',
    work_center VARCHAR(50) COMMENT 'Work Center',
    equipment_id BIGINT COMMENT 'Equipment ID',
    worker VARCHAR(50) COMMENT 'Worker',
    shift VARCHAR(20) COMMENT 'Shift',
    report_type VARCHAR(20) COMMENT 'START, PAUSE, RESUME, COMPLETE',
    total_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Total Quantity',
    good_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Good Quantity',
    defect_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Defect Quantity',
    defect_reason VARCHAR(200) COMMENT 'Defect Reason',
    start_time DATETIME COMMENT 'Start Time',
    end_time DATETIME COMMENT 'End Time',
    duration INT COMMENT 'Duration(min)',
    remark VARCHAR(500) COMMENT 'Remark',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_dispatch_id (dispatch_id),
    INDEX idx_equipment_id (equipment_id),
    INDEX idx_create_time (create_time)
) COMMENT 'Production Reporting' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- WIP - Work In Progress tracking
CREATE TABLE IF NOT EXISTS prod_wip (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    work_order_id BIGINT COMMENT 'Work Order ID',
    product_code VARCHAR(50) COMMENT 'Product Code',
    product_name VARCHAR(100) COMMENT 'Product Name',
    process_code VARCHAR(50) COMMENT 'Current Process Code',
    process_name VARCHAR(100) COMMENT 'Current Process Name',
    work_center VARCHAR(50) COMMENT 'Work Center',
    wip_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'WIP Quantity',
    hold_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Hold Quantity',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_product_code (product_code)
) COMMENT 'Work In Progress' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Repair order
CREATE TABLE IF NOT EXISTS prod_repair_order (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    repair_no VARCHAR(50) NOT NULL COMMENT 'Repair Order No',
    work_order_id BIGINT COMMENT 'Original Work Order ID',
    product_code VARCHAR(50) COMMENT 'Product Code',
    quantity DECIMAL(10,2) COMMENT 'Repair Quantity',
    defect_desc VARCHAR(500) COMMENT 'Defect Description',
    repair_process VARCHAR(50) COMMENT 'Repair Process',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, IN_PROGRESS, COMPLETED',
    result VARCHAR(20) COMMENT 'PASS, FAIL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_repair_no (repair_no),
    INDEX idx_work_order_id (work_order_id)
) COMMENT 'Repair Order' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Plan Management Tables
-- =============================================

-- Master Production Schedule
CREATE TABLE IF NOT EXISTS plan_mps (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    plan_no VARCHAR(50) NOT NULL COMMENT 'Plan No',
    plan_name VARCHAR(100) COMMENT 'Plan Name',
    plan_period VARCHAR(20) COMMENT 'Period: MONTHLY, WEEKLY',
    plan_date DATE COMMENT 'Plan Date',
    product_code VARCHAR(50) COMMENT 'Product Code',
    product_name VARCHAR(100) COMMENT 'Product Name',
    plan_qty DECIMAL(10,2) COMMENT 'Plan Quantity',
    completed_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Completed Quantity',
    start_date DATE COMMENT 'Start Date',
    end_date DATE COMMENT 'End Date',
    status VARCHAR(20) DEFAULT 'DRAFT' COMMENT 'DRAFT, CONFIRMED, RELEASED, CLOSED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_plan_no (plan_no),
    INDEX idx_product_code (product_code),
    INDEX idx_status (status)
) COMMENT 'Master Production Schedule' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Daily Production Plan
CREATE TABLE IF NOT EXISTS plan_daily (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    plan_no VARCHAR(50) NOT NULL COMMENT 'Plan No',
    mps_id BIGINT COMMENT 'MPS ID',
    plan_date DATE COMMENT 'Plan Date',
    product_code VARCHAR(50) COMMENT 'Product Code',
    product_name VARCHAR(100) COMMENT 'Product Name',
    plan_qty DECIMAL(10,2) COMMENT 'Plan Quantity',
    actual_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Actual Quantity',
    production_line VARCHAR(50) COMMENT 'Production Line',
    shift VARCHAR(20) COMMENT 'Shift',
    workshop VARCHAR(50) COMMENT 'Workshop',
    status VARCHAR(20) DEFAULT 'DRAFT',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_plan_no (plan_no),
    INDEX idx_mps_id (mps_id),
    INDEX idx_product_code (product_code),
    INDEX idx_plan_date (plan_date)
) COMMENT 'Daily Production Plan' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Production Schedule (排产)
CREATE TABLE IF NOT EXISTS plan_schedule (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    schedule_no VARCHAR(50) NOT NULL COMMENT 'Schedule No',
    work_order_id BIGINT COMMENT 'Work Order ID',
    product_code VARCHAR(50) COMMENT 'Product Code',
    process_code VARCHAR(50) COMMENT 'Process Code',
    equipment_id BIGINT COMMENT 'Equipment ID',
    production_line VARCHAR(50) COMMENT 'Production Line',
    plan_start_time DATETIME COMMENT 'Plan Start',
    plan_end_time DATETIME COMMENT 'Plan End',
    plan_qty DECIMAL(10,2) COMMENT 'Plan Quantity',
    priority INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_schedule_no (schedule_no),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_equipment_id (equipment_id),
    INDEX idx_product_code (product_code)
) COMMENT 'Production Schedule' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Order Split records
CREATE TABLE IF NOT EXISTS plan_order_split (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    source_order_no VARCHAR(50) NOT NULL COMMENT 'Source Order No',
    child_order_no VARCHAR(50) NOT NULL COMMENT 'Child Order No',
    source_qty DECIMAL(10,2) COMMENT 'Source Quantity',
    split_qty DECIMAL(10,2) COMMENT 'Split Quantity',
    split_date DATE COMMENT 'Split Date',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_source_order_no (source_order_no),
    INDEX idx_child_order_no (child_order_no)
) COMMENT 'Order Split' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Material Management Tables
-- =============================================

-- Material Requisition (领料)
CREATE TABLE IF NOT EXISTS mat_requisition (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    req_no VARCHAR(50) NOT NULL COMMENT 'Requisition No',
    work_order_id BIGINT COMMENT 'Work Order ID',
    material_id BIGINT COMMENT 'Material ID',
    material_code VARCHAR(50) COMMENT 'Material Code',
    material_name VARCHAR(100) COMMENT 'Material Name',
    req_qty DECIMAL(10,2) COMMENT 'Request Quantity',
    actual_qty DECIMAL(10,2) COMMENT 'Actual Issued Quantity',
    unit VARCHAR(20) COMMENT 'Unit',
    warehouse VARCHAR(50) COMMENT 'Warehouse',
    location VARCHAR(50) COMMENT 'Storage Location',
    batch_no VARCHAR(50) COMMENT 'Batch No',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, APPROVED, ISSUED',
    applicant VARCHAR(50) COMMENT 'Applicant',
    approver VARCHAR(50) COMMENT 'Approver',
    issue_time DATETIME COMMENT 'Issue Time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_req_no (req_no),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_material_id (material_id),
    INDEX idx_status (status)
) COMMENT 'Material Requisition' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Material Return (退料)
CREATE TABLE IF NOT EXISTS mat_return (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    return_no VARCHAR(50) NOT NULL COMMENT 'Return No',
    work_order_id BIGINT COMMENT 'Work Order ID',
    req_id BIGINT COMMENT 'Original Requisition ID',
    material_id BIGINT COMMENT 'Material ID',
    material_code VARCHAR(50) COMMENT 'Material Code',
    material_name VARCHAR(100) COMMENT 'Material Name',
    return_qty DECIMAL(10,2) COMMENT 'Return Quantity',
    unit VARCHAR(20) COMMENT 'Unit',
    return_reason VARCHAR(200) COMMENT 'Return Reason',
    warehouse VARCHAR(50) COMMENT 'Warehouse',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, CONFIRMED',
    applicant VARCHAR(50) COMMENT 'Applicant',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_return_no (return_no),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_req_id (req_id),
    INDEX idx_material_id (material_id)
) COMMENT 'Material Return' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Material Supplement (补料)
CREATE TABLE IF NOT EXISTS mat_supplement (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    supp_no VARCHAR(50) NOT NULL COMMENT 'Supplement No',
    work_order_id BIGINT COMMENT 'Work Order ID',
    material_id BIGINT COMMENT 'Material ID',
    material_code VARCHAR(50) COMMENT 'Material Code',
    material_name VARCHAR(100) COMMENT 'Material Name',
    supp_qty DECIMAL(10,2) COMMENT 'Supplement Quantity',
    unit VARCHAR(20) COMMENT 'Unit',
    supp_reason VARCHAR(500) COMMENT 'Reason',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, APPROVED, ISSUED',
    applicant VARCHAR(50) COMMENT 'Applicant',
    approver VARCHAR(50) COMMENT 'Approver',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_supp_no (supp_no),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_material_id (material_id)
) COMMENT 'Material Supplement' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Material Inventory
CREATE TABLE IF NOT EXISTS mat_inventory (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    material_id BIGINT COMMENT 'Material ID',
    material_code VARCHAR(50) COMMENT 'Material Code',
    material_name VARCHAR(100) COMMENT 'Material Name',
    warehouse VARCHAR(50) COMMENT 'Warehouse',
    location VARCHAR(50) COMMENT 'Storage Location',
    batch_no VARCHAR(50) COMMENT 'Batch No',
    qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Current Quantity',
    locked_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Locked Quantity',
    unit VARCHAR(20) COMMENT 'Unit',
    supplier VARCHAR(100) COMMENT 'Supplier',
    production_date DATE COMMENT 'Production Date',
    expiry_date DATE COMMENT 'Expiry Date',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_material_id (material_id),
    INDEX idx_material_code (material_code),
    INDEX idx_batch_no (batch_no),
    INDEX idx_warehouse (warehouse)
) COMMENT 'Material Inventory' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Material Inventory Transaction Log
CREATE TABLE IF NOT EXISTS mat_inventory_log (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    material_id BIGINT COMMENT 'Material ID',
    material_code VARCHAR(50) COMMENT 'Material Code',
    change_type VARCHAR(20) COMMENT 'INBOUND, OUTBOUND, RETURN, ADJUST',
    change_qty DECIMAL(10,2) COMMENT 'Change Quantity',
    before_qty DECIMAL(10,2) COMMENT 'Quantity Before',
    after_qty DECIMAL(10,2) COMMENT 'Quantity After',
    reference_no VARCHAR(50) COMMENT 'Reference Order No',
    operator VARCHAR(50) COMMENT 'Operator',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_material_id (material_id),
    INDEX idx_create_time (create_time),
    INDEX idx_reference_no (reference_no)
) COMMENT 'Material Inventory Transaction Log' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Warehouse Management Tables
-- =============================================

-- Warehouse definition
CREATE TABLE IF NOT EXISTS wh_warehouse (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    wh_code VARCHAR(50) NOT NULL COMMENT 'Warehouse Code',
    wh_name VARCHAR(100) NOT NULL COMMENT 'Warehouse Name',
    wh_type VARCHAR(30) COMMENT 'RAW_MATERIAL, SEMI_PRODUCT, FINISHED',
    location VARCHAR(200) COMMENT 'Location',
    manager VARCHAR(50) COMMENT 'Manager',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_wh_code (wh_code)
) COMMENT 'Warehouse' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inbound order
CREATE TABLE IF NOT EXISTS wh_inbound (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    inbound_no VARCHAR(50) NOT NULL COMMENT 'Inbound No',
    inbound_type VARCHAR(30) COMMENT 'PURCHASE, PRODUCTION, RETURN, TRANSFER',
    warehouse_id BIGINT COMMENT 'Warehouse ID',
    material_code VARCHAR(50) COMMENT 'Material/Product Code',
    material_name VARCHAR(100) COMMENT 'Material/Product Name',
    qty DECIMAL(10,2) COMMENT 'Quantity',
    unit VARCHAR(20) COMMENT 'Unit',
    batch_no VARCHAR(50) COMMENT 'Batch No',
    supplier VARCHAR(100) COMMENT 'Supplier',
    source_order_no VARCHAR(50) COMMENT 'Source Order No',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, COMPLETED',
    operator VARCHAR(50) COMMENT 'Operator',
    inbound_time DATETIME COMMENT 'Inbound Time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_inbound_no (inbound_no),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status),
    INDEX idx_material_code (material_code)
) COMMENT 'Warehouse Inbound' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Outbound order
CREATE TABLE IF NOT EXISTS wh_outbound (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    outbound_no VARCHAR(50) NOT NULL COMMENT 'Outbound No',
    outbound_type VARCHAR(30) COMMENT 'PRODUCTION, SALES, TRANSFER, SCRAP',
    warehouse_id BIGINT COMMENT 'Warehouse ID',
    material_code VARCHAR(50) COMMENT 'Material/Product Code',
    material_name VARCHAR(100) COMMENT 'Material/Product Name',
    qty DECIMAL(10,2) COMMENT 'Quantity',
    unit VARCHAR(20) COMMENT 'Unit',
    batch_no VARCHAR(50) COMMENT 'Batch No',
    target_order_no VARCHAR(50) COMMENT 'Target Order No',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, COMPLETED',
    operator VARCHAR(50) COMMENT 'Operator',
    outbound_time DATETIME COMMENT 'Outbound Time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_outbound_no (outbound_no),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status),
    INDEX idx_material_code (material_code)
) COMMENT 'Warehouse Outbound' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Transfer order
CREATE TABLE IF NOT EXISTS wh_transfer (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    transfer_no VARCHAR(50) NOT NULL COMMENT 'Transfer No',
    from_warehouse_id BIGINT COMMENT 'From Warehouse ID',
    to_warehouse_id BIGINT COMMENT 'To Warehouse ID',
    material_code VARCHAR(50) COMMENT 'Material Code',
    material_name VARCHAR(100) COMMENT 'Material Name',
    qty DECIMAL(10,2) COMMENT 'Quantity',
    unit VARCHAR(20) COMMENT 'Unit',
    batch_no VARCHAR(50) COMMENT 'Batch No',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, COMPLETED',
    operator VARCHAR(50) COMMENT 'Operator',
    transfer_time DATETIME COMMENT 'Transfer Time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_transfer_no (transfer_no),
    INDEX idx_from_warehouse_id (from_warehouse_id),
    INDEX idx_to_warehouse_id (to_warehouse_id),
    INDEX idx_status (status)
) COMMENT 'Warehouse Transfer' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inventory Check (盘点)
CREATE TABLE IF NOT EXISTS wh_check (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    check_no VARCHAR(50) NOT NULL COMMENT 'Check No',
    warehouse_id BIGINT COMMENT 'Warehouse ID',
    check_type VARCHAR(30) COMMENT 'FULL, PARTIAL, CYCLE',
    check_date DATE COMMENT 'Check Date',
    material_code VARCHAR(50) COMMENT 'Material Code',
    book_qty DECIMAL(10,2) COMMENT 'Book Quantity',
    actual_qty DECIMAL(10,2) COMMENT 'Actual Quantity',
    diff_qty DECIMAL(10,2) COMMENT 'Difference',
    diff_reason VARCHAR(500) COMMENT 'Difference Reason',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, CONFIRMED',
    checker VARCHAR(50) COMMENT 'Checker',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_check_no (check_no),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status)
) COMMENT 'Inventory Check' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Equipment Management Tables
-- =============================================

-- Equipment Category
CREATE TABLE IF NOT EXISTS equip_category (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL COMMENT 'Category Name',
    category_code VARCHAR(50) NOT NULL COMMENT 'Category Code',
    parent_id BIGINT DEFAULT 0,
    description VARCHAR(200),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_category_code (category_code),
    INDEX idx_parent_id (parent_id)
) COMMENT 'Equipment Category' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Equipment Manufacturer
CREATE TABLE IF NOT EXISTS equip_manufacturer (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    manufacturer_name VARCHAR(100) NOT NULL COMMENT 'Manufacturer Name',
    manufacturer_code VARCHAR(50) NOT NULL COMMENT 'Manufacturer Code',
    contact_person VARCHAR(50) COMMENT 'Contact Person',
    contact_phone VARCHAR(20) COMMENT 'Contact Phone',
    address VARCHAR(200) COMMENT 'Address',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_manufacturer_code (manufacturer_code)
) COMMENT 'Equipment Manufacturer' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Equipment Asset
CREATE TABLE IF NOT EXISTS equip_asset (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    equip_code VARCHAR(50) NOT NULL COMMENT 'Equipment Code',
    equip_name VARCHAR(100) NOT NULL COMMENT 'Equipment Name',
    equip_model VARCHAR(100) COMMENT 'Model',
    equip_spec VARCHAR(200) COMMENT 'Specification',
    category_id BIGINT COMMENT 'Category ID',
    manufacturer_id BIGINT COMMENT 'Manufacturer ID',
    department_id BIGINT COMMENT 'Department ID',
    production_line VARCHAR(50) COMMENT 'Production Line',
    work_center VARCHAR(50) COMMENT 'Work Center',
    location VARCHAR(100) COMMENT 'Location',
    serial_no VARCHAR(100) COMMENT 'Serial Number',
    status VARCHAR(20) DEFAULT 'IDLE' COMMENT 'IDLE, RUNNING, STANDBY, MAINTENANCE, BROKEN',
    purchase_date DATE COMMENT 'Purchase Date',
    warranty_end DATE COMMENT 'Warranty End Date',
    service_life INT COMMENT 'Service Life(months)',
    supplier VARCHAR(100) COMMENT 'Supplier',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_equip_code (equip_code),
    INDEX idx_category_id (category_id),
    INDEX idx_manufacturer_id (manufacturer_id),
    INDEX idx_department_id (department_id),
    INDEX idx_status (status),
    INDEX idx_production_line (production_line),
    INDEX idx_work_center (work_center)
) COMMENT 'Equipment Asset' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Equipment Fault Dictionary
CREATE TABLE IF NOT EXISTS equip_fault (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    equip_id BIGINT COMMENT 'Equipment ID',
    fault_code VARCHAR(50) COMMENT 'Fault Code',
    fault_name VARCHAR(100) COMMENT 'Fault Name',
    fault_desc VARCHAR(500) COMMENT 'Fault Description',
    fault_category VARCHAR(50) COMMENT 'Category: MECHANICAL, ELECTRICAL, SOFTWARE, OTHER',
    reason_analysis VARCHAR(500) COMMENT 'Reason Analysis',
    solution VARCHAR(500) COMMENT 'Solution',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_equip_id (equip_id),
    INDEX idx_fault_code (fault_code)
) COMMENT 'Equipment Fault Dictionary' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Maintenance Plan (保养计划)
CREATE TABLE IF NOT EXISTS equip_maintenance_plan (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    plan_no VARCHAR(50) NOT NULL COMMENT 'Plan No',
    equip_id BIGINT COMMENT 'Equipment ID',
    maintenance_type VARCHAR(30) COMMENT 'DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY',
    plan_date DATE COMMENT 'Plan Date',
    content VARCHAR(1000) COMMENT 'Maintenance Content',
    responsible_person VARCHAR(50) COMMENT 'Responsible Person',
    status VARCHAR(20) DEFAULT 'SCHEDULED' COMMENT 'SCHEDULED, EXECUTING, COMPLETED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_plan_no (plan_no),
    INDEX idx_equip_id (equip_id),
    INDEX idx_status (status)
) COMMENT 'Maintenance Plan' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Maintenance Record
CREATE TABLE IF NOT EXISTS equip_maintenance_record (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    plan_id BIGINT COMMENT 'Plan ID',
    equip_id BIGINT COMMENT 'Equipment ID',
    execute_date DATE COMMENT 'Execute Date',
    result VARCHAR(500) COMMENT 'Result',
    executor VARCHAR(50) COMMENT 'Executor',
    remark VARCHAR(500) COMMENT 'Remark',
    cost DECIMAL(10,2) COMMENT 'Cost',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_plan_id (plan_id),
    INDEX idx_equip_id (equip_id)
) COMMENT 'Maintenance Record' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inspection Plan (点检计划)
CREATE TABLE IF NOT EXISTS equip_inspection_plan (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    plan_no VARCHAR(50) NOT NULL COMMENT 'Plan No',
    equip_id BIGINT COMMENT 'Equipment ID',
    inspection_type VARCHAR(30) COMMENT 'DAILY, WEEKLY, MONTHLY',
    plan_date DATE COMMENT 'Plan Date',
    check_points VARCHAR(1000) COMMENT 'Check Points',
    responsible_person VARCHAR(50) COMMENT 'Responsible Person',
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_plan_no (plan_no),
    INDEX idx_equip_id (equip_id),
    INDEX idx_status (status)
) COMMENT 'Inspection Plan' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inspection Record
CREATE TABLE IF NOT EXISTS equip_inspection_record (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    plan_id BIGINT COMMENT 'Plan ID',
    equip_id BIGINT COMMENT 'Equipment ID',
    execute_date DATE COMMENT 'Execute Date',
    check_result TEXT COMMENT 'Check Results',
    is_normal TINYINT UNSIGNED DEFAULT 1 COMMENT '0-Abnormal, 1-Normal',
    inspector VARCHAR(50) COMMENT 'Inspector',
    remark VARCHAR(500) COMMENT 'Remark',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_plan_id (plan_id),
    INDEX idx_equip_id (equip_id)
) COMMENT 'Inspection Record' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Repair Task (维修任务)
CREATE TABLE IF NOT EXISTS equip_repair (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    repair_no VARCHAR(50) NOT NULL COMMENT 'Repair No',
    equip_id BIGINT COMMENT 'Equipment ID',
    fault_id BIGINT COMMENT 'Fault ID',
    fault_desc VARCHAR(500) COMMENT 'Fault Description',
    repair_type VARCHAR(30) COMMENT 'EMERGENCY, PLANNED',
    reporter VARCHAR(50) COMMENT 'Reporter',
    repair_person VARCHAR(50) COMMENT 'Repair Person',
    start_time DATETIME COMMENT 'Start Time',
    end_time DATETIME COMMENT 'End Time',
    downtime INT COMMENT 'Downtime(min)',
    repair_content VARCHAR(1000) COMMENT 'Repair Content',
    parts_used VARCHAR(500) COMMENT 'Parts Used',
    cost DECIMAL(10,2) COMMENT 'Repair Cost',
    status VARCHAR(20) DEFAULT 'REPORTED' COMMENT 'REPORTED, ASSIGNED, IN_PROGRESS, COMPLETED, VERIFIED',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_repair_no (repair_no),
    INDEX idx_equip_id (equip_id),
    INDEX idx_fault_id (fault_id),
    INDEX idx_status (status)
) COMMENT 'Equipment Repair' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Equipment Status Log
CREATE TABLE IF NOT EXISTS equip_status_log (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    equip_id BIGINT COMMENT 'Equipment ID',
    from_status VARCHAR(20) COMMENT 'From Status',
    to_status VARCHAR(20) COMMENT 'To Status',
    reason VARCHAR(500) COMMENT 'Reason',
    operator VARCHAR(50) COMMENT 'Operator',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_equip_id (equip_id),
    INDEX idx_create_time (create_time)
) COMMENT 'Equipment Status Log' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- OEE Record
CREATE TABLE IF NOT EXISTS equip_oee_record (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    equip_id BIGINT COMMENT 'Equipment ID',
    record_date DATE COMMENT 'Record Date',
    shift VARCHAR(20) COMMENT 'Shift',
    plan_runtime INT DEFAULT 0 COMMENT 'Plan Runtime(min)',
    actual_runtime INT DEFAULT 0 COMMENT 'Actual Runtime(min)',
    downtime INT DEFAULT 0 COMMENT 'Downtime(min)',
    output_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Output Quantity',
    defect_qty DECIMAL(10,2) DEFAULT 0 COMMENT 'Defect Quantity',
    availability DECIMAL(5,2) COMMENT 'Availability(%)',
    performance DECIMAL(5,2) COMMENT 'Performance(%)',
    quality DECIMAL(5,2) COMMENT 'Quality(%)',
    oee DECIMAL(5,2) COMMENT 'OEE(%)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_equip_id (equip_id),
    INDEX idx_record_date (record_date)
) COMMENT 'OEE Record' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Equipment Energy Record
CREATE TABLE IF NOT EXISTS equip_energy_record (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    equip_id BIGINT COMMENT 'Equipment ID',
    record_date DATE COMMENT 'Record Date',
    record_hour INT COMMENT 'Record Hour',
    electricity DECIMAL(10,2) DEFAULT 0 COMMENT 'Electricity(kWh)',
    water DECIMAL(10,2) DEFAULT 0 COMMENT 'Water(m3)',
    gas DECIMAL(10,2) DEFAULT 0 COMMENT 'Gas(m3)',
    compressed_air DECIMAL(10,2) DEFAULT 0 COMMENT 'Compressed Air(m3)',
    steam DECIMAL(10,2) DEFAULT 0 COMMENT 'Steam(ton)',
    output_qty DECIMAL(10,2) COMMENT 'Output Qty this period',
    unit_energy DECIMAL(10,4) COMMENT 'Unit Energy per product',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_equip_id (equip_id),
    INDEX idx_record_date (record_date)
) COMMENT 'Equipment Energy Record' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Quality Management Tables
-- =============================================

-- Inspection Item Category
CREATE TABLE IF NOT EXISTS quality_inspection_category (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL COMMENT 'Category Name',
    category_code VARCHAR(50) NOT NULL COMMENT 'Category Code',
    description VARCHAR(200),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_category_code (category_code)
) COMMENT 'Inspection Item Category' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inspection Item
CREATE TABLE IF NOT EXISTS quality_inspection_item (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    item_code VARCHAR(50) NOT NULL COMMENT 'Item Code',
    item_name VARCHAR(100) NOT NULL COMMENT 'Item Name',
    category_id BIGINT COMMENT 'Category ID',
    unit VARCHAR(20) COMMENT 'Unit',
    standard_value VARCHAR(50) COMMENT 'Standard Value',
    upper_limit VARCHAR(50) COMMENT 'Upper Limit',
    lower_limit VARCHAR(50) COMMENT 'Lower Limit',
    inspection_type VARCHAR(30) COMMENT 'IQC, IPQC, FQC',
    test_method VARCHAR(200) COMMENT 'Test Method',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_item_code (item_code),
    INDEX idx_category_id (category_id)
) COMMENT 'Quality Inspection Item' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inspection Standard (检验标准方案)
CREATE TABLE IF NOT EXISTS quality_inspection_standard (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    standard_no VARCHAR(50) NOT NULL COMMENT 'Standard No',
    standard_name VARCHAR(100) NOT NULL COMMENT 'Standard Name',
    product_code VARCHAR(50) COMMENT 'Product Code',
    customer_name VARCHAR(100) COMMENT 'Customer Name',
    sample_size INT COMMENT 'Sample Size',
    aql_level VARCHAR(20) COMMENT 'AQL Level',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_standard_no (standard_no),
    INDEX idx_product_code (product_code)
) COMMENT 'Inspection Standard' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inspection Standard Detail (检验标准明细)
CREATE TABLE IF NOT EXISTS quality_standard_detail (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    standard_id BIGINT COMMENT 'Standard ID',
    item_id BIGINT COMMENT 'Inspection Item ID',
    standard_value VARCHAR(50) COMMENT 'Standard Value',
    upper_limit VARCHAR(50) COMMENT 'Upper Limit',
    lower_limit VARCHAR(50) COMMENT 'Lower Limit',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_standard_id (standard_id),
    INDEX idx_item_id (item_id)
) COMMENT 'Inspection Standard Detail' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inspection Order
CREATE TABLE IF NOT EXISTS quality_inspection_order (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL COMMENT 'Inspection Order No',
    order_type VARCHAR(30) COMMENT 'IQC, IPQC, FQC, FIRST_LAST, PATROL, WAREHOUSE_IN, SHIPMENT',
    source_order_no VARCHAR(50) COMMENT 'Source Order No (Work Order / Inbound No)',
    product_code VARCHAR(50) COMMENT 'Product Code',
    product_name VARCHAR(100) COMMENT 'Product Name',
    batch_no VARCHAR(50) COMMENT 'Batch No / Lot No',
    sample_qty INT COMMENT 'Sample Quantity',
    defect_qty INT DEFAULT 0 COMMENT 'Defect Quantity',
    result VARCHAR(20) COMMENT 'PASS, FAIL, PENDING',
    inspector VARCHAR(50) COMMENT 'Inspector',
    inspection_time DATETIME COMMENT 'Inspection Time',
    remark VARCHAR(500) COMMENT 'Remark',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_source_order_no (source_order_no),
    INDEX idx_product_code (product_code),
    INDEX idx_result (result)
) COMMENT 'Quality Inspection Order' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Inspection Result Detail
CREATE TABLE IF NOT EXISTS quality_inspection_result (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    order_id BIGINT COMMENT 'Inspection Order ID',
    item_id BIGINT COMMENT 'Inspection Item ID',
    item_name VARCHAR(100) COMMENT 'Item Name',
    standard_value VARCHAR(50) COMMENT 'Standard Value',
    upper_limit VARCHAR(50) COMMENT 'Upper Limit',
    lower_limit VARCHAR(50) COMMENT 'Lower Limit',
    actual_value VARCHAR(50) COMMENT 'Actual Value',
    unit VARCHAR(20) COMMENT 'Unit',
    is_pass TINYINT UNSIGNED DEFAULT 1 COMMENT '0-Fail, 1-Pass',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_order_id (order_id),
    INDEX idx_item_id (item_id)
) COMMENT 'Inspection Result Detail' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- First/Last Piece Inspection
CREATE TABLE IF NOT EXISTS quality_first_last (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL COMMENT 'Order No',
    work_order_id BIGINT COMMENT 'Work Order ID',
    inspection_type VARCHAR(10) COMMENT 'FIRST, LAST',
    product_code VARCHAR(50) COMMENT 'Product Code',
    process_code VARCHAR(50) COMMENT 'Process Code',
    equipment_id BIGINT COMMENT 'Equipment ID',
    result VARCHAR(20) COMMENT 'PASS, FAIL',
    inspector VARCHAR(50) COMMENT 'Inspector',
    inspection_time DATETIME COMMENT 'Inspection Time',
    measurement_data TEXT COMMENT 'Measurement Data(JSON)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_equipment_id (equipment_id)
) COMMENT 'First/Last Piece Inspection' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Patrol Inspection
CREATE TABLE IF NOT EXISTS quality_patrol (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL COMMENT 'Order No',
    work_order_id BIGINT COMMENT 'Work Order ID',
    production_line VARCHAR(50) COMMENT 'Production Line',
    process_code VARCHAR(50) COMMENT 'Process Code',
    check_interval INT COMMENT 'Check Interval(min)',
    sample_size INT COMMENT 'Sample Size',
    result VARCHAR(20) COMMENT 'PASS, FAIL',
    inspector VARCHAR(50) COMMENT 'Inspector',
    inspection_time DATETIME COMMENT 'Inspection Time',
    find_desc VARCHAR(500) COMMENT 'Findings Description',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_order_no (order_no),
    INDEX idx_work_order_id (work_order_id)
) COMMENT 'Patrol Inspection' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Defect Analysis
CREATE TABLE IF NOT EXISTS quality_defect_record (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    work_order_id BIGINT COMMENT 'Work Order ID',
    product_code VARCHAR(50) COMMENT 'Product Code',
    process_code VARCHAR(50) COMMENT 'Process Code',
    defect_type VARCHAR(50) COMMENT 'Defect Type',
    defect_location VARCHAR(50) COMMENT 'Defect Location',
    defect_desc VARCHAR(500) COMMENT 'Defect Description',
    defect_qty INT COMMENT 'Defect Quantity',
    root_cause VARCHAR(500) COMMENT 'Root Cause',
    corrective_action VARCHAR(500) COMMENT 'Corrective Action',
    reporter VARCHAR(50) COMMENT 'Reporter',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_product_code (product_code),
    INDEX idx_defect_type (defect_type)
) COMMENT 'Defect Record' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Andon Management Tables
-- =============================================

-- Andon Type
CREATE TABLE IF NOT EXISTS andon_type (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL COMMENT 'Type Name',
    type_code VARCHAR(50) NOT NULL COMMENT 'Type Code',
    handle_mode VARCHAR(20) COMMENT 'NONE, SELF, ASSIST',
    light_color VARCHAR(20) COMMENT 'Light Color',
    description VARCHAR(200),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_type_code (type_code)
) COMMENT 'Andon Type' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Andon Configuration (异常配置)
CREATE TABLE IF NOT EXISTS andon_config (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT 'Config Name',
    andon_type_id BIGINT COMMENT 'Andon Type ID',
    work_center VARCHAR(50) COMMENT 'Work Center',
    trigger_condition VARCHAR(200) COMMENT 'Trigger Condition',
    notify_users VARCHAR(500) COMMENT 'Notify Users(comma-separated)',
    escalation_time INT COMMENT 'Escalation Time(min)',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_andon_type_id (andon_type_id)
) COMMENT 'Andon Configuration' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Andon Reason (异常原因)
CREATE TABLE IF NOT EXISTS andon_reason (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    reason_name VARCHAR(100) NOT NULL COMMENT 'Reason Name',
    reason_code VARCHAR(50) NOT NULL COMMENT 'Reason Code',
    andon_type_id BIGINT COMMENT 'Andon Type ID',
    parent_id BIGINT DEFAULT 0 COMMENT 'Parent Reason ID',
    sort_order INT DEFAULT 0,
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_reason_code (reason_code),
    INDEX idx_andon_type_id (andon_type_id),
    INDEX idx_parent_id (parent_id)
) COMMENT 'Andon Reason' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Andon Record
CREATE TABLE IF NOT EXISTS andon_record (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    andon_type_id BIGINT COMMENT 'Andon Type ID',
    reason_id BIGINT COMMENT 'Reason ID',
    work_center VARCHAR(50) COMMENT 'Work Center',
    production_line VARCHAR(50) COMMENT 'Production Line',
    equipment_id BIGINT COMMENT 'Equipment ID',
    work_order_id BIGINT COMMENT 'Work Order ID',
    description VARCHAR(500) COMMENT 'Description',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, CONFIRMED, PROCESSING, RESOLVED',
    severity VARCHAR(20) DEFAULT 'NORMAL' COMMENT 'NORMAL, URGENT, CRITICAL',
    reporter VARCHAR(50) COMMENT 'Reporter',
    handler VARCHAR(50) COMMENT 'Handler',
    report_time DATETIME COMMENT 'Report Time',
    confirm_time DATETIME COMMENT 'Confirm Time',
    resolve_time DATETIME COMMENT 'Resolve Time',
    resolve_desc VARCHAR(500) COMMENT 'Resolution Description',
    duration INT COMMENT 'Duration(min)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_andon_type_id (andon_type_id),
    INDEX idx_reason_id (reason_id),
    INDEX idx_equipment_id (equipment_id),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_status (status)
) COMMENT 'Andon Record' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Process Management Tables
-- =============================================

-- Process Route
CREATE TABLE IF NOT EXISTS process_route (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    route_code VARCHAR(50) NOT NULL COMMENT 'Route Code',
    route_name VARCHAR(100) NOT NULL COMMENT 'Route Name',
    product_code VARCHAR(50) COMMENT 'Product Code',
    description VARCHAR(200),
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_route_code (route_code),
    INDEX idx_product_code (product_code)
) COMMENT 'Process Route' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Process Step
CREATE TABLE IF NOT EXISTS process_step (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    route_id BIGINT COMMENT 'Route ID',
    step_code VARCHAR(50) NOT NULL COMMENT 'Step Code',
    step_name VARCHAR(100) NOT NULL COMMENT 'Step Name',
    step_type VARCHAR(30) COMMENT 'NORMAL, INSPECTION, REPAIR',
    sort_order INT DEFAULT 0 COMMENT 'Sort Order',
    work_center VARCHAR(50) COMMENT 'Work Center',
    standard_time DECIMAL(10,2) COMMENT 'Standard Time(min)',
    yield_rate DECIMAL(5,2) DEFAULT 100 COMMENT 'Expected Yield Rate(%)',
    sop_file VARCHAR(200) COMMENT 'SOP File Path',
    prev_step_id BIGINT COMMENT 'Previous Step ID',
    next_step_id BIGINT COMMENT 'Next Step ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_route_id (route_id),
    INDEX idx_step_code (step_code),
    INDEX idx_work_center (work_center)
) COMMENT 'Process Step' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Barcode Management Tables
-- =============================================

-- Barcode Type
CREATE TABLE IF NOT EXISTS barcode_type (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL COMMENT 'Type Name',
    type_code VARCHAR(50) NOT NULL COMMENT 'Type Code',
    description VARCHAR(200),
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_type_code (type_code)
) COMMENT 'Barcode Type' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Barcode Rule
CREATE TABLE IF NOT EXISTS barcode_rule (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    rule_name VARCHAR(50) NOT NULL COMMENT 'Rule Name',
    rule_code VARCHAR(50) NOT NULL COMMENT 'Rule Code',
    barcode_type_id BIGINT COMMENT 'Barcode Type ID',
    prefix VARCHAR(20) COMMENT 'Prefix',
    date_format VARCHAR(20) COMMENT 'Date Format',
    serial_length INT DEFAULT 4 COMMENT 'Serial Number Length',
    `separator` VARCHAR(5) DEFAULT '-' COMMENT 'Separator',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_rule_code (rule_code),
    INDEX idx_barcode_type_id (barcode_type_id)
) COMMENT 'Barcode Rule' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Barcode Template
CREATE TABLE IF NOT EXISTS barcode_template (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    template_name VARCHAR(50) NOT NULL COMMENT 'Template Name',
    template_code VARCHAR(50) NOT NULL COMMENT 'Template Code',
    template_content TEXT COMMENT 'Template Content',
    width DECIMAL(10,2) COMMENT 'Label Width(mm)',
    height DECIMAL(10,2) COMMENT 'Label Height(mm)',
    description VARCHAR(200) COMMENT 'Description',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_template_code (template_code)
) COMMENT 'Barcode Template' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Barcode Application Rule
CREATE TABLE IF NOT EXISTS barcode_application (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    application_name VARCHAR(50) NOT NULL COMMENT 'Application Name',
    rule_id BIGINT COMMENT 'Barcode Rule ID',
    template_id BIGINT COMMENT 'Template ID',
    product_code VARCHAR(50) COMMENT 'Product Code',
    trigger_point VARCHAR(50) COMMENT 'Trigger Point (process step)',
    print_qty INT DEFAULT 1 COMMENT 'Print Quantity per trigger',
    status TINYINT UNSIGNED DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_rule_id (rule_id),
    INDEX idx_template_id (template_id),
    INDEX idx_product_code (product_code)
) COMMENT 'Barcode Application Rule' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Generated Barcode Records
CREATE TABLE IF NOT EXISTS barcode_record (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    barcode_no VARCHAR(100) NOT NULL COMMENT 'Barcode Content',
    barcode_type_id BIGINT COMMENT 'Barcode Type ID',
    rule_id BIGINT COMMENT 'Rule ID',
    product_code VARCHAR(50) COMMENT 'Product Code',
    work_order_id BIGINT COMMENT 'Work Order ID',
    batch_no VARCHAR(50) COMMENT 'Batch No',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, USED, VOID',
    print_count INT DEFAULT 0 COMMENT 'Print Count',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    UNIQUE KEY uk_barcode_no (barcode_no),
    INDEX idx_barcode_type_id (barcode_type_id),
    INDEX idx_rule_id (rule_id),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_product_code (product_code)
) COMMENT 'Barcode Record' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Traceability Tables
-- =============================================

-- Product Trace Record
CREATE TABLE IF NOT EXISTS trace_product (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    barcode_no VARCHAR(100) NOT NULL COMMENT 'Barcode/Serial No',
    product_code VARCHAR(50) COMMENT 'Product Code',
    product_name VARCHAR(100) COMMENT 'Product Name',
    work_order_id BIGINT COMMENT 'Work Order ID',
    production_line VARCHAR(50) COMMENT 'Production Line',
    equipment_id BIGINT COMMENT 'Equipment ID',
    process_code VARCHAR(50) COMMENT 'Process Code',
    worker VARCHAR(50) COMMENT 'Worker',
    shift VARCHAR(20) COMMENT 'Shift',
    batch_no VARCHAR(50) COMMENT 'Batch No',
    production_date DATE COMMENT 'Production Date',
    production_time DATETIME COMMENT 'Production Time',
    inspection_result VARCHAR(20) COMMENT 'PASS, FAIL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_barcode_no (barcode_no),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_equipment_id (equipment_id),
    INDEX idx_product_code (product_code),
    INDEX idx_batch_no (batch_no)
) COMMENT 'Product Trace Record' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Material Batch Trace (物料批次追溯)
CREATE TABLE IF NOT EXISTS trace_material_batch (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    batch_no VARCHAR(50) NOT NULL COMMENT 'Batch No',
    material_id BIGINT COMMENT 'Material ID',
    material_code VARCHAR(50) COMMENT 'Material Code',
    material_name VARCHAR(100) COMMENT 'Material Name',
    supplier VARCHAR(100) COMMENT 'Supplier',
    supplier_batch VARCHAR(100) COMMENT 'Supplier Batch No',
    received_date DATE COMMENT 'Received Date',
    qty DECIMAL(10,2) COMMENT 'Received Quantity',
    inspection_result VARCHAR(20) COMMENT 'IQC Result',
    certificate_no VARCHAR(50) COMMENT 'COA Certificate No',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_batch_no (batch_no),
    INDEX idx_material_id (material_id),
    INDEX idx_material_code (material_code)
) COMMENT 'Material Batch Trace' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Wages Record
CREATE TABLE IF NOT EXISTS wages_record (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    employee_name VARCHAR(50) COMMENT '员工姓名',
    employee_code VARCHAR(50) COMMENT '员工编码',
    work_order_id BIGINT COMMENT '工单ID',
    process_code VARCHAR(50) COMMENT '工序编码',
    quantity DECIMAL(10,2) COMMENT '计件数量',
    unit_price DECIMAL(10,2) COMMENT '单价',
    total_amount DECIMAL(10,2) COMMENT '金额',
    record_date DATE COMMENT '日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT UNSIGNED DEFAULT 0,
    PRIMARY KEY pk_id (id),
    INDEX idx_work_order_id (work_order_id),
    INDEX idx_employee_code (employee_code),
    INDEX idx_record_date (record_date)
) COMMENT '计件工资记录' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Product-Material Trace Link
CREATE TABLE IF NOT EXISTS trace_product_material (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    barcode_no VARCHAR(100) COMMENT 'Product Barcode',
    material_batch_no VARCHAR(50) COMMENT 'Material Batch No',
    material_code VARCHAR(50) COMMENT 'Material Code',
    used_qty DECIMAL(10,4) COMMENT 'Used Quantity',
    process_code VARCHAR(50) COMMENT 'Consumed At Process',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_barcode_no (barcode_no),
    INDEX idx_material_batch_no (material_batch_no),
    INDEX idx_material_code (material_code)
) COMMENT 'Product-Material Trace Link' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- ERP Interface Tables
-- =============================================

-- ERP Sync Log
CREATE TABLE IF NOT EXISTS erp_sync_log (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    sync_type VARCHAR(30) COMMENT 'IMPORT, EXPORT',
    data_type VARCHAR(50) COMMENT 'Data Type',
    source_system VARCHAR(50) COMMENT 'Source System',
    record_count INT COMMENT 'Record Count',
    success_count INT DEFAULT 0 COMMENT 'Success Count',
    fail_count INT DEFAULT 0 COMMENT 'Fail Count',
    status VARCHAR(20) COMMENT 'RUNNING, SUCCESS, FAILED',
    error_msg TEXT COMMENT 'Error Message',
    start_time DATETIME COMMENT 'Start Time',
    end_time DATETIME COMMENT 'End Time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY pk_id (id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) COMMENT 'ERP Sync Log' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Insert Initial Data
-- =============================================

-- Clear existing seed data for idempotent re-initialization
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM sys_user_role;
DELETE FROM sys_role_menu;
DELETE FROM sys_menu;
DELETE FROM sys_role;
DELETE FROM sys_user;
DELETE FROM sys_department;
DELETE FROM sys_config;
DELETE FROM sys_operation_log;
DELETE FROM md_product;
DELETE FROM md_material_category;
DELETE FROM md_material;
DELETE FROM wh_inbound;
DELETE FROM wh_outbound;
DELETE FROM wh_transfer;
DELETE FROM wh_check;
DELETE FROM wh_warehouse;
DELETE FROM process_step;
DELETE FROM process_route;
DELETE FROM equip_category;
DELETE FROM equip_manufacturer;
DELETE FROM equip_asset;
DELETE FROM equip_fault;
DELETE FROM equip_maintenance_plan;
DELETE FROM equip_maintenance_record;
DELETE FROM equip_inspection_plan;
DELETE FROM equip_inspection_record;
DELETE FROM equip_repair;
DELETE FROM equip_status_log;
DELETE FROM equip_oee_record;
DELETE FROM equip_energy_record;
DELETE FROM quality_inspection_category;
DELETE FROM quality_inspection_item;
DELETE FROM quality_inspection_standard;
DELETE FROM quality_standard_detail;
DELETE FROM quality_inspection_order;
DELETE FROM quality_inspection_result;
DELETE FROM quality_first_last;
DELETE FROM quality_patrol;
DELETE FROM quality_defect_record;
DELETE FROM andon_type;
DELETE FROM andon_config;
DELETE FROM andon_reason;
DELETE FROM andon_record;
DELETE FROM prod_work_order;
DELETE FROM prod_dispatch_order;
DELETE FROM prod_reporting;
DELETE FROM prod_wip;
DELETE FROM prod_repair_order;
DELETE FROM plan_mps;
DELETE FROM plan_daily;
DELETE FROM plan_schedule;
DELETE FROM plan_order_split;
DELETE FROM mat_requisition;
DELETE FROM mat_return;
DELETE FROM mat_supplement;
DELETE FROM mat_inventory;
DELETE FROM mat_inventory_log;
DELETE FROM barcode_type;
DELETE FROM barcode_rule;
DELETE FROM barcode_template;
DELETE FROM barcode_application;
DELETE FROM barcode_record;
DELETE FROM trace_product;
DELETE FROM trace_material_batch;
DELETE FROM trace_product_material;
DELETE FROM wages_record;
DELETE FROM erp_sync_log;
SET FOREIGN_KEY_CHECKS = 1;

-- Insert default departments
INSERT INTO sys_department (id, parent_id, dept_name, dept_code, sort_order) VALUES
(1, 0, '总经办', 'HQ', 1),
(2, 0, '生产部', 'PROD', 2),
(3, 0, '质量部', 'QC', 3),
(4, 0, '设备部', 'EQUIP', 4),
(5, 0, '仓储部', 'WH', 5),
(6, 0, '计划部', 'PMC', 6);

-- Insert default admin user (password: admin123)
INSERT INTO sys_user (id, username, password, real_name, email, dept_id, status) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', 'Administrator', 'admin@mes.com', 1, 1);

-- Insert roles
INSERT INTO sys_role (id, role_name, role_code, description) VALUES
(1, '超级管理员', 'SUPER_ADMIN', 'System Super Administrator'),
(2, '生产经理', 'PROD_MANAGER', 'Production Manager'),
(3, '质检员', 'QUALITY_INSPECTOR', 'Quality Inspector'),
(4, '设备工程师', 'EQUIP_ENGINEER', 'Equipment Engineer'),
(5, '操作工', 'OPERATOR', 'Production Operator'),
(6, '仓库管理员', 'WH_MANAGER', 'Warehouse Manager'),
(7, '计划员', 'PLANNER', 'Production Planner');

-- Assign roles to admin
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- Insert menus (MES System Navigation)
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, url, icon, sort_order) VALUES
-- Top level menus
(1, 0, '首页看板', 'dashboard', 'M', '/', 'fa-gauge-high', 1),
(2, 0, '生产工单', 'prod_order', 'M', '/production/order', 'fa-clipboard-list', 2),
(3, 0, '条码管理', 'barcode', 'M', '/barcode/type', 'fa-barcode', 3),
(4, 0, '生产管理', 'prod_mgmt', 'M', '/production/mgmt/task', 'fa-industry', 4),
(5, 0, '计划管理', 'plan_mgmt', 'M', '/plan/mps', 'fa-calendar-check', 5),
(6, 0, '物料管理', 'material_mgmt', 'M', '/material/requisition', 'fa-boxes-stacked', 6),
(7, 0, '仓储管理', 'warehouse_mgmt', 'M', '/warehouse/inbound', 'fa-warehouse', 7),
(8, 0, '设备管理', 'equip_mgmt', 'M', '/equipment/asset', 'fa-cogs', 8),
(9, 0, '质量管理', 'quality_mgmt', 'M', '/quality/inspection-item', 'fa-check-circle', 9),
(10, 0, '安灯管理', 'andon_mgmt', 'M', '/andon/type', 'fa-lightbulb', 10),
(11, 0, '工艺管理', 'process_mgmt', 'M', '/process/step', 'fa-project-diagram', 11),
(12, 0, '计件工资', 'wages', 'M', '/wages', 'fa-calculator', 12),
(13, 0, '报表分析', 'report', 'M', '/report/output', 'fa-chart-bar', 13),
(14, 0, '电子看板', 'display_board', 'M', '/dashboard/production-line', 'fa-tachometer-alt', 14),
(15, 0, '系统管理', 'system', 'M', '/system/user', 'fa-cog', 15),
(16, 0, 'ERP接口', 'erp', 'M', '/erp/task-read', 'fa-plug', 16),
(17, 0, '标准API', 'api', 'M', '/api/unit', 'fa-code', 17),
-- Sub menus: Production Orders
(201, 2, '工单管理', 'prod_order_mgmt', 'C', '/production/order', 'fa-list', 1),
(202, 2, '齐套分析', 'kit_analysis', 'C', '/production/order/kit-analysis', 'fa-clipboard-check', 2),
(203, 2, '派工单管理', 'dispatch_order', 'C', '/production/dispatch', 'fa-tasks', 3),
-- Sub menus: Barcode
(301, 3, '条码类型', 'barcode_type', 'C', '/barcode/type', 'fa-tag', 1),
(302, 3, '条码规则', 'barcode_rule', 'C', '/barcode/rule', 'fa-ruler', 2),
(303, 3, '条码模板', 'barcode_template', 'C', '/barcode/template', 'fa-file-code', 3),
(304, 3, '条码应用规则', 'barcode_app', 'C', '/barcode/application', 'fa-gear', 4),
(305, 3, '条码生成', 'barcode_gen', 'C', '/barcode/generate', 'fa-qrcode', 5),
-- Sub menus: Production Management
(401, 4, '参数配置', 'prod_config', 'C', '/production/mgmt/config', 'fa-sliders', 1),
(402, 4, '生产任务', 'prod_task', 'C', '/production/mgmt/task', 'fa-list-ol', 2),
(403, 4, '生产派工', 'prod_dispatch', 'C', '/production/mgmt/dispatch', 'fa-user-check', 3),
(404, 4, '生产状态', 'prod_status', 'C', '/production/mgmt/status', 'fa-signal', 4),
(405, 4, '生产报工', 'prod_reporting', 'C', '/production/mgmt/reporting', 'fa-pen-to-square', 5),
(406, 4, '生产完工', 'prod_completion', 'C', '/production/mgmt/completion', 'fa-check-double', 6),
(407, 4, '返修工单', 'prod_repair', 'C', '/production/mgmt/repair', 'fa-wrench', 7),
-- Sub menus: Plan Management
(501, 5, '主生产计划', 'plan_mps', 'C', '/plan/mps', 'fa-calendar', 1),
(502, 5, '日生产计划', 'plan_daily', 'C', '/plan/daily', 'fa-calendar-day', 2),
(503, 5, '生产排产', 'plan_schedule', 'C', '/plan/schedule', 'fa-timeline', 3),
(504, 5, '工单拆分', 'plan_split', 'C', '/plan/split', 'fa-scissors', 4),
-- Sub menus: Material
(601, 6, '领料管理', 'mat_req', 'C', '/material/requisition', 'fa-arrow-right-to-bracket', 1),
(602, 6, '退料管理', 'mat_return', 'C', '/material/return', 'fa-arrow-left-to-bracket', 2),
(603, 6, '补料管理', 'mat_supp', 'C', '/material/supplement', 'fa-plus-circle', 3),
(604, 6, '物料库存', 'mat_inventory', 'C', '/material/inventory', 'fa-box-open', 4),
-- Sub menus: Warehouse
(701, 7, '入库管理', 'wh_inbound', 'C', '/warehouse/inbound', 'fa-arrow-down', 1),
(702, 7, '出库管理', 'wh_outbound', 'C', '/warehouse/outbound', 'fa-arrow-up', 2),
(703, 7, '移库管理', 'wh_transfer', 'C', '/warehouse/transfer', 'fa-right-left', 3),
(704, 7, '盘点管理', 'wh_check', 'C', '/warehouse/check', 'fa-calculator', 4),
(705, 7, '库存查询', 'wh_stock', 'C', '/warehouse/stock', 'fa-search', 5),
-- Sub menus: Equipment
(801, 8, '设备分类', 'equip_category', 'C', '/equipment/category', 'fa-sitemap', 1),
(802, 8, '设备厂商', 'equip_manu', 'C', '/equipment/manufacturer', 'fa-building', 2),
(803, 8, '故障字典', 'equip_fault', 'C', '/equipment/fault', 'fa-bug', 3),
(804, 8, '设备台账', 'equip_asset', 'C', '/equipment/asset', 'fa-server', 4),
(805, 8, '保养任务', 'equip_maintain', 'C', '/equipment/maintenance', 'fa-oil-can', 5),
(806, 8, '设备点检', 'equip_inspection', 'C', '/equipment/inspection', 'fa-clipboard-check', 6),
(807, 8, '维修任务', 'equip_repair', 'C', '/equipment/repair', 'fa-toolbox', 7),
(808, 8, '设备OEE', 'equip_oee', 'C', '/equipment/oee', 'fa-gauge', 8),
(809, 8, '能耗分析', 'equip_energy', 'C', '/equipment/energy', 'fa-bolt', 9),
-- Sub menus: Quality
(901, 9, '检验项目', 'qc_item', 'C', '/quality/inspection-item', 'fa-flask', 1),
(902, 9, '检验标准', 'qc_standard', 'C', '/quality/inspection-standard', 'fa-file-signature', 2),
(903, 9, '首末件检验', 'qc_first_last', 'C', '/quality/first-last', 'fa-1', 3),
(904, 9, '巡检管理', 'qc_patrol', 'C', '/quality/patrol', 'fa-magnifying-glass', 4),
(905, 9, '入库检验', 'qc_wh_in', 'C', '/quality/warehouse-in', 'fa-box', 5),
(906, 9, '出货检验', 'qc_shipment', 'C', '/quality/shipment', 'fa-truck-fast', 6),
-- Sub menus: Andon
(1001, 10, '安灯类型', 'andon_type', 'C', '/andon/type', 'fa-lightbulb', 1),
(1002, 10, '异常配置', 'andon_config', 'C', '/andon/config', 'fa-gear', 2),
(1003, 10, '异常原因', 'andon_reason', 'C', '/andon/reason', 'fa-exclamation-triangle', 3),
(1004, 10, '异常记录', 'andon_record', 'C', '/andon/record', 'fa-clock-rotate-left', 4),
-- Sub menus: Process
(1101, 11, '工序管理', 'process_step', 'C', '/process/step', 'fa-list-ol', 1),
(1102, 11, '工艺路线', 'process_route', 'C', '/process/route', 'fa-diagram-project', 2),
-- Sub menus: Report
(1301, 13, '产量报表', 'rpt_output', 'C', '/report/output', 'fa-chart-line', 1),
(1302, 13, '实时生产信息', 'rpt_realtime', 'C', '/report/realtime', 'fa-clock', 2),
(1303, 13, '不良查询', 'rpt_defect', 'C', '/report/defect', 'fa-circle-xmark', 3),
(1304, 13, '关键物料追溯', 'rpt_mat_trace', 'C', '/report/material-trace', 'fa-magnifying-glass-chart', 4),
(1305, 13, '产品追溯查询', 'rpt_product_trace', 'C', '/report/product-trace', 'fa-barcode', 5),
(1306, 13, '车间周期报表', 'rpt_workshop', 'C', '/report/workshop-period', 'fa-table', 6),
-- Sub menus: Dashboard
(1401, 14, '产线看板', 'dash_line', 'C', '/dashboard/production-line', 'fa-desktop', 1),
(1402, 14, '车间看板', 'dash_workshop', 'C', '/dashboard/workshop', 'fa-display', 2),
(1403, 14, '中央控制看板', 'dash_central', 'C', '/dashboard/central', 'fa-tv', 3),
-- Sub menus: System
(1501, 15, '组织管理', 'sys_dept', 'C', '/system/organization', 'fa-building', 1),
(1502, 15, '用户管理', 'sys_user', 'C', '/system/user', 'fa-users', 2),
(1503, 15, '角色管理', 'sys_role', 'C', '/system/role', 'fa-user-tag', 3),
(1504, 15, '菜单管理', 'sys_menu', 'C', '/system/menu', 'fa-bars', 4),
(1505, 15, '系统监控', 'sys_monitor', 'C', '/system/monitor', 'fa-heart-pulse', 5),
(1506, 15, '基础配置', 'sys_config', 'C', '/system/config', 'fa-wrench', 6),
-- Sub menus: ERP
(1601, 16, '生产任务导入', 'erp_task', 'C', '/erp/task-read', 'fa-file-import', 1),
(1602, 16, '工序数据导入', 'erp_process', 'C', '/erp/process-read', 'fa-file-import', 2),
-- Sub menus: API
(1701, 17, '计量单位(写)', 'api_unit', 'C', '/api/unit', 'fa-pen', 1),
(1702, 17, '工单(写)', 'api_wo', 'C', '/api/work-order', 'fa-pen', 2),
(1703, 17, '生产任务(写)', 'api_task', 'C', '/api/task', 'fa-pen', 3),
(1704, 17, '设备产量上报(写)', 'api_equip', 'C', '/api/equip-reporting', 'fa-pen', 4),
(1705, 17, '生产完工(读)', 'api_completion', 'C', '/api/completion', 'fa-eye', 5);

-- Insert products (Battery types)
INSERT INTO md_product (product_code, product_name, product_spec, product_type, unit, voltage, capacity) VALUES
('LR6', 'LR6 AA碱性电池', 'AA 1.5V', 'FINISHED', '只', '1.5V', '2800mAh'),
('LR03', 'LR03 AAA碱性电池', 'AAA 1.5V', 'FINISHED', '只', '1.5V', '1200mAh'),
('6LR61', '6LR61 9V碱性电池', '9V', 'FINISHED', '只', '9V', '600mAh'),
('LR14', 'LR14 C碱性电池', 'C 1.5V', 'FINISHED', '只', '1.5V', '8000mAh'),
('LR20', 'LR20 D碱性电池', 'D 1.5V', 'FINISHED', '只', '1.5V', '18000mAh');

-- Insert material categories
INSERT INTO md_material_category (id, parent_id, category_name, category_code, sort_order) VALUES
(1, 0, '正极材料', 'CATHODE', 1),
(2, 0, '负极材料', 'ANODE', 2),
(3, 0, '电解液', 'ELECTROLYTE', 3),
(4, 0, '隔膜', 'SEPARATOR', 4),
(5, 0, '壳体材料', 'SHELL', 5),
(6, 0, '密封材料', 'SEAL', 6),
(7, 0, '包装材料', 'PACKAGING', 7);

-- Insert materials
INSERT INTO md_material (material_code, material_name, category_id, unit, safety_stock, current_stock) VALUES
('M001', '电解二氧化锰', 1, 'kg', 1000, 5000),
('M002', '石墨粉', 1, 'kg', 500, 2000),
('M003', '锌粉', 2, 'kg', 2000, 8000),
('M004', '氢氧化钾溶液', 3, 'L', 500, 3000),
('M005', '隔膜纸', 4, 'm2', 1000, 5000),
('M006', '钢壳', 5, '只', 100000, 500000),
('M007', '铜钉', 5, '只', 100000, 500000),
('M008', '密封圈', 6, '只', 100000, 500000),
('M009', '标签', 7, '张', 100000, 800000),
('M010', 'PVC膜', 7, 'm2', 500, 3000),
('M011', '包装盒', 7, '只', 50000, 200000);

-- Insert warehouses
INSERT INTO wh_warehouse (id, wh_code, wh_name, wh_type) VALUES
(1, 'WH-RAW', '原料仓', 'RAW_MATERIAL'),
(2, 'WH-SEMI', '半成品仓', 'SEMI_PRODUCT'),
(3, 'WH-FIN', '成品仓', 'FINISHED');

-- Insert process route for LR6
INSERT INTO process_route (id, route_code, route_name, product_code, description) VALUES
(1, 'RT-LR6-001', 'LR6 AA标准工艺路线', 'LR6', '碱性锌锰电池LR6 AA标准生产工艺路线');

-- Insert process steps for LR6
INSERT INTO process_step (route_id, step_code, step_name, step_type, sort_order, work_center, standard_time) VALUES
(1, 'STAMP', '锌筒冲压', 'NORMAL', 1, 'WC-STAMP', 0.5),
(1, 'MIX', '配料搅拌', 'NORMAL', 2, 'WC-MIX', 2.0),
(1, 'GLUE', '注胶', 'NORMAL', 3, 'WC-GLUE', 0.3),
(1, 'ASSEMBLE', '装芯', 'NORMAL', 4, 'WC-ASSEMBLE', 0.8),
(1, 'SEAL', '封口', 'NORMAL', 5, 'WC-SEAL', 0.5),
(1, 'INSPECT', '检测', 'INSPECTION', 6, 'WC-INSPECT', 0.2),
(1, 'PACK', '包装', 'NORMAL', 7, 'WC-PACK', 1.0);

-- Insert equipment categories
INSERT INTO equip_category (id, category_name, category_code, parent_id) VALUES
(1, '冲压设备', 'STAMP', 0),
(2, '配料设备', 'MIX', 0),
(3, '注胶设备', 'GLUE', 0),
(4, '装配设备', 'ASSEMBLE', 0),
(5, '封口设备', 'SEAL', 0),
(6, '检测设备', 'INSPECTION', 0),
(7, '包装设备', 'PACK', 0);

-- Insert inspection items
INSERT INTO quality_inspection_item (item_code, item_name, category_id, unit, standard_value, upper_limit, lower_limit) VALUES
('QC-VOLTAGE', '开路电压', NULL, 'V', '1.50', '1.65', '1.45'),
('QC-CURRENT', '短路电流', NULL, 'A', '2.0', '3.0', '1.5'),
('QC-CAPACITY', '容量', NULL, 'mAh', '2800', '3000', '2600'),
('QC-RESISTANCE', '内阻', NULL, 'mΩ', '150', '200', '100'),
('QC-DIAMETER', '直径', NULL, 'mm', '14.5', '14.7', '14.3'),
('QC-HEIGHT', '高度', NULL, 'mm', '50.5', '50.8', '50.2'),
('QC-WEIGHT', '重量', NULL, 'g', '23.5', '24.0', '23.0'),
('QC-LEAKAGE', '漏液检测', NULL, '-', '无', '-', '-'),
('QC-APPEARANCE', '外观', NULL, '-', '无缺陷', '-', '-');

-- Insert andon types
INSERT INTO andon_type (type_name, type_code, handle_mode, light_color) VALUES
('设备故障', 'EQUIP_FAULT', 'ASSIST', 'RED'),
('质量异常', 'QUALITY_ABNORMAL', 'ASSIST', 'RED'),
('物料短缺', 'MATERIAL_SHORT', 'ASSIST', 'YELLOW'),
('生产延误', 'PROD_DELAY', 'SELF', 'YELLOW'),
('安全事故', 'SAFETY_INCIDENT', 'ASSIST', 'RED'),
('呼叫班组长', 'CALL_LEADER', 'SELF', 'BLUE');

-- Insert andon reasons
INSERT INTO andon_reason (reason_name, reason_code, andon_type_id, sort_order) VALUES
('机械故障', 'FAULT_MECH', 1, 1),
('电气故障', 'FAULT_ELEC', 1, 2),
('软件故障', 'FAULT_SOFT', 1, 3),
('电压超标', 'QA_VOLTAGE', 2, 1),
('尺寸超差', 'QA_DIMENSION', 2, 2),
('外观不良', 'QA_APPEARANCE', 2, 3),
('钢壳不足', 'MAT_SHELL', 3, 1),
('锌粉不足', 'MAT_ZINC', 3, 2),
('等待上工序', 'DELAY_PREV', 4, 1),
('换模等待', 'DELAY_MOLD', 4, 2);

-- Insert system config
INSERT INTO sys_config (config_name, config_key, config_value, config_type) VALUES
('公司名称', 'company.name', '某某电池制造有限公司', 'STRING'),
('默认班次', 'default.shift', '白班', 'STRING'),
('OEE目标值', 'oee.target', '85', 'NUMBER'),
('良率目标值', 'yield.target', '99.5', 'NUMBER'),
('安灯升级时间(分)', 'andon.escalation', '15', 'NUMBER'),
('条码前缀', 'barcode.prefix', 'MES', 'STRING');

-- =============================================
-- Insert Equipment Manufacturers
-- =============================================
INSERT INTO equip_manufacturer (id, manufacturer_name, manufacturer_code, contact_person, contact_phone, address, status) VALUES
(1, '深圳科力自动化', 'KL-AUTO', '陈经理', '13800001001', '深圳市宝安区', 1),
(2, '东莞精密机械', 'DG-PRECISION', '李工', '13800001002', '东莞市长安镇', 1),
(3, '广州华亿封口科技', 'HY-SEAL', '王总', '13800001003', '广州市番禺区', 1),
(4, '北京中科检测仪器', 'ZK-TEST', '赵博士', '13800001004', '北京市海淀区', 1),
(5, '苏州瑞博包装机械', 'RB-PACK', '刘经理', '13800001005', '苏州市工业园区', 1);

-- =============================================
-- Insert Equipment Assets (10 devices)
-- =============================================
INSERT INTO equip_asset (id, equip_code, equip_name, equip_model, category_id, manufacturer_id, department_id, production_line, work_center, location, status, purchase_date) VALUES
(1, 'EQ-MX-001', '双行星搅拌机A(正极)', 'SXJ-500L', 2, 1, 2, 'A线-正极装配', 'WC-MIX', '一车间A区', '运行中', '2024-03-15'),
(2, 'EQ-MX-002', '真空搅拌机B(负极)', 'ZKJ-300L', 2, 1, 2, 'B线-负极装配', 'WC-MIX', '一车间B区', '运行中', '2024-09-01'),
(3, 'EQ-AS-001', '钢壳装配线A', 'GZ-2000', 4, 2, 2, 'A线-正极装配', 'WC-ASSEMBLE', '一车间B区', '运行中', '2024-05-20'),
(4, 'EQ-AS-002', '钢壳装配线B', 'GZ-2000', 4, 2, 2, 'B线-负极装配', 'WC-ASSEMBLE', '一车间C区', '运行中', '2024-06-10'),
(5, 'EQ-SL-001', '自动封口机A', 'FK-1500B', 5, 3, 2, 'C线-封口包装', 'WC-SEAL', '一车间C区', '保养中', '2024-06-10'),
(6, 'EQ-SL-002', '自动封口机B', 'FK-1500B', 5, 3, 2, 'C线-封口包装', 'WC-SEAL', '一车间D区', '运行中', '2024-07-01'),
(7, 'EQ-TS-001', '电性能测试仪A', 'DX-8000', 6, 4, 2, 'D线-检测分选', 'WC-INSPECT', '品质实验室一楼', '运行中', '2024-07-05'),
(8, 'EQ-TS-002', '电池分选机B', 'FX-2000', 6, 4, 2, 'D线-检测分选', 'WC-INSPECT', '品质实验室二楼', '空闲', '2024-08-10'),
(9, 'EQ-PK-001', '热缩包装线A', 'RS-1200', 7, 5, 2, 'C线-封口包装', 'WC-PACK', '二车间A区', '运行中', '2024-08-12'),
(10, 'EQ-PK-002', '自动装箱线B', 'ZX-800', 7, 5, 2, 'C线-封口包装', 'WC-PACK', '二车间B区', '故障', '2024-10-01');

-- =============================================
-- Insert Work Orders (10 orders)
-- =============================================
-- Using CURRENT_DATE() and DATE_ADD for dynamic dates so seed data always works regardless of current date
INSERT INTO prod_work_order (id, order_no, product_code, product_name, quantity, completed_qty, defective_qty, start_date, end_date, production_line, status, workshop, priority) VALUES
(1, 'WO-CUR-001', 'LR6', 'LR6 AA碱性电池', 50000, 42500, 120, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 6 DAY), 'A线-正极装配', '生产中', '一车间', 1),
(2, 'WO-CUR-002', 'LR03', 'LR03 AAA碱性电池', 80000, 80000, 85, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 7 DAY), 'B线-负极装配', '已完成', '一车间', 1),
(3, 'WO-D01-003', '6LR61', '6LR61 9V碱性电池', 20000, 12800, 45, DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 9 DAY), 'A线-正极装配', '生产中', '一车间', 2),
(4, 'WO-D01-004', 'LR14', 'LR14 C碱性电池', 30000, 30000, 30, DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 5 DAY), 'B线-负极装配', '已完成', '一车间', 2),
(5, 'WO-D02-005', 'LR20', 'LR20 D碱性电池', 15000, 0, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 3 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 11 DAY), 'C线-封口包装', '待生产', '二车间', 3),
(6, 'WO-D02-006', 'LR6', 'LR6 AA碱性电池(高性能)', 100000, 65300, 200, DATE_SUB(CURRENT_DATE(), INTERVAL 2 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 13 DAY), 'A线-正极装配', '生产中', '一车间', 1),
(7, 'WO-D03-007', 'LR03', 'LR03 AAA碱性电池(散装)', 120000, 120000, 150, DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 4 DAY), 'B线-负极装配', '已完成', '一车间', 2),
(8, 'WO-D03-008', '6LR61', '6LR61 9V碱性电池(工业装)', 25000, 0, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 6 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 16 DAY), 'C线-封口包装', '暂停', '二车间', 3),
(9, 'WO-D04-009', 'LR14', 'LR14 C碱性电池(高容量)', 40000, 28900, 80, DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 10 DAY), 'A线-正极装配', '生产中', '一车间', 2),
(10, 'WO-D04-010', 'LR20', 'LR20 D碱性电池', 18000, 18000, 40, DATE_SUB(CURRENT_DATE(), INTERVAL 4 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 2 DAY), 'D线-检测分选', '已完成', '一车间', 1);

-- =============================================
-- Insert Production Reporting (报工记录)
-- =============================================
INSERT INTO prod_reporting (id, work_order_id, process_code, process_name, work_center, report_type, total_qty, good_qty, defect_qty, worker, create_time) VALUES
(1, 1, 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 'start', 500, 498, 2, '张伟', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(2, 1, 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 'defect', 3, 0, 3, '李明', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(3, 1, 'PRC-005', '封口成型', 'WC-SEAL', 'complete', 200, 198, 2, '王芳', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(4, 2, 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 'complete', 800, 798, 2, '赵强', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(5, 2, 'PRC-008', '热缩包装', 'WC-PACK', 'complete', 1200, 1195, 5, '孙丽', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(6, 3, 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 'start', 100, 100, 0, '周杰', CURRENT_TIMESTAMP()),
(7, 4, 'PRC-008', '热缩包装', 'WC-PACK', 'complete', 600, 598, 2, '吴敏', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(8, 5, 'PRC-001', '正极粉料搅拌', 'WC-MIX', 'defect', 5, 0, 5, '陈宇', CURRENT_TIMESTAMP()),
(9, 6, 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 'start', 1000, 995, 5, '张伟', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(10, 7, 'PRC-005', '封口成型', 'WC-SEAL', 'complete', 1500, 1495, 5, '王芳', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(11, 1, 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 'complete', 400, 398, 2, '张伟', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(12, 3, 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 'complete', 90, 88, 2, '周杰', CURRENT_TIMESTAMP());

-- =============================================
-- Insert WIP records
-- =============================================
INSERT INTO prod_wip (id, work_order_id, product_code, product_name, process_code, process_name, work_center, wip_qty, hold_qty) VALUES
(1, 1, 'LR6', 'LR6 AA碱性电池', 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 7500, 0),
(2, 1, 'LR6', 'LR6 AA碱性电池', 'PRC-005', '封口成型', 'WC-SEAL', 5000, 200),
(3, 3, '6LR61', '6LR61 9V碱性电池', 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 7200, 0),
(4, 6, 'LR6', 'LR6 AA碱性电池(高性能)', 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 34700, 0),
(5, 9, 'LR14', 'LR14 C碱性电池(高容量)', 'PRC-004', '钢壳装配', 'WC-ASSEMBLE', 11100, 300);

-- =============================================
-- Insert Andon Records (alarms)
-- =============================================
INSERT INTO andon_record (id, andon_type_id, reason_id, production_line, work_center, equipment_id, work_order_id, description, status, severity, reporter, report_time) VALUES
(1, 3, 7, 'A线-正极装配', 'WC-ASSEMBLE', 3, 1, '物料短缺 - 钢壳不足，仅剩2000个', '未处理', 'URGENT', '张伟', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(2, 1, 1, 'B线-负极装配', 'WC-SEAL', 6, 2, '封口机卡料，需停机清理', '处理中', 'NORMAL', '赵强', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(3, 2, 5, 'A线-正极装配', 'WC-INSPECT', 7, 1, '质量偏差报警 - 开路电压检测超标0.02V', '已解决', 'CRITICAL', '王芳', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(4, 1, 2, 'B线-负极装配', 'WC-PACK', 9, 2, '标签打印机故障 - 打印模糊', '已解决', 'NORMAL', '孙丽', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(5, 4, 10, 'B线-负极装配', 'WC-MIX', 2, 5, '换模等待 - 模具调试中', '未处理', 'NORMAL', '陈宇', CURRENT_TIMESTAMP()),
(6, 1, 3, 'A线-正极装配', 'WC-PACK', 10, 6, '包装线B PLC通讯故障', '未处理', 'CRITICAL', '周杰', CURRENT_TIMESTAMP());

-- =============================================
-- Insert Plan MPS records
-- =============================================
INSERT INTO plan_mps (id, plan_no, plan_name, plan_period, plan_date, product_code, product_name, plan_qty, completed_qty, start_date, end_date, status) VALUES
(1, 'MPS-CUR-M01', '当月LR6 AA生产计划', 'MONTHLY', DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'LR6', 'LR6 AA碱性电池', 500000, 350000, DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 20 DAY), '已确认'),
(2, 'MPS-CUR-M02', '当月LR03 AAA生产计划', 'MONTHLY', DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), 'LR03', 'LR03 AAA碱性电池', 800000, 500000, DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 20 DAY), '已确认'),
(3, 'MPS-CUR-M03', '当月6LR61 9V生产计划', 'MONTHLY', DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), '6LR61', '6LR61 9V碱性电池', 150000, 80000, DATE_SUB(CURRENT_DATE(), INTERVAL 10 DAY), DATE_ADD(CURRENT_DATE(), INTERVAL 20 DAY), '已确认'),
(4, 'MPS-NXT-M01', '下月LR14 C生产计划', 'MONTHLY', DATE_ADD(CURRENT_DATE(), INTERVAL 1 MONTH), 'LR14', 'LR14 C碱性电池', 200000, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 1 MONTH), DATE_ADD(DATE_ADD(CURRENT_DATE(), INTERVAL 1 MONTH), INTERVAL 20 DAY), '草稿'),
(5, 'MPS-NXT-M02', '下月LR20 D生产计划', 'MONTHLY', DATE_ADD(CURRENT_DATE(), INTERVAL 1 MONTH), 'LR20', 'LR20 D碱性电池', 100000, 0, DATE_ADD(CURRENT_DATE(), INTERVAL 1 MONTH), DATE_ADD(DATE_ADD(CURRENT_DATE(), INTERVAL 1 MONTH), INTERVAL 20 DAY), '草稿');

-- =============================================
-- Insert Daily Plans
-- =============================================
INSERT INTO plan_daily (id, plan_no, mps_id, plan_date, product_code, product_name, plan_qty, actual_qty, production_line, shift, status) VALUES
(1, 'DP-CUR-001', 1, CURRENT_DATE(), 'LR6', 'LR6 AA碱性电池', 25000, 18000, 'A线-正极装配', '白班', '生产中'),
(2, 'DP-CUR-002', 2, CURRENT_DATE(), 'LR03', 'LR03 AAA碱性电池', 40000, 40000, 'B线-负极装配', '白班', '已完成'),
(3, 'DP-CUR-003', 3, CURRENT_DATE(), '6LR61', '6LR61 9V碱性电池', 10000, 6500, 'A线-正极装配', '白班', '生产中');

-- =============================================
-- Insert Inspection Orders
-- =============================================
INSERT INTO quality_inspection_order (id, order_no, order_type, source_order_no, product_code, product_name, batch_no, sample_qty, defect_qty, result, inspector, inspection_time) VALUES
(1, 'QC-CUR-001', 'FQC', 'WO-CUR-001', 'LR6', 'LR6 AA碱性电池', 'BAT-CUR-A', 100, 2, '合格', '王芳', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(2, 'QC-CUR-002', 'FQC', 'WO-CUR-002', 'LR03', 'LR03 AAA碱性电池', 'BAT-CUR-B', 100, 0, '合格', '王芳', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(3, 'QC-CUR-003', 'IPQC', 'WO-CUR-001', 'LR6', 'LR6 AA碱性电池', 'BAT-CUR-A', 50, 5, '合格', '李明', CURRENT_TIMESTAMP());

-- =============================================
-- Insert Inbound & Outbound records
-- =============================================
INSERT INTO wh_inbound (id, inbound_no, inbound_type, warehouse_id, material_code, material_name, qty, unit, batch_no, source_order_no, status, operator, inbound_time) VALUES
(1, 'IB-CUR-001', 'PRODUCTION', 3, 'LR6', 'LR6 AA碱性电池', 42500, '只', 'BAT-CUR-A', 'WO-CUR-001', 'COMPLETED', '吴敏', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(2, 'IB-CUR-002', 'PRODUCTION', 3, 'LR03', 'LR03 AAA碱性电池', 80000, '只', 'BAT-CUR-B', 'WO-CUR-002', 'COMPLETED', '吴敏', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(3, 'IB-CUR-003', 'PURCHASE', 1, 'MAT-002', '锌粉(高纯)', 5000, 'kg', 'BAT-ZN-CUR', 'PO-CUR-012', 'COMPLETED', '吴敏', DATE_SUB(NOW(), INTERVAL 1 HOUR));

INSERT INTO wh_outbound (id, outbound_no, outbound_type, warehouse_id, material_code, material_name, qty, unit, batch_no, target_order_no, status, operator, outbound_time) VALUES
(1, 'OB-CUR-001', 'PRODUCTION', 1, 'MAT-005', '钢壳(LR6)', 60000, 'pcs', 'BAT-SS-CUR', 'WO-CUR-001', 'COMPLETED', '孙丽', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(2, 'OB-CUR-002', 'PRODUCTION', 1, 'MAT-001', '电解二氧化锰(EMD)', 500, 'kg', 'BAT-EMD-CUR', 'WO-CUR-001', 'COMPLETED', '孙丽', DATE_SUB(NOW(), INTERVAL 3 HOUR));

-- =============================================
-- Insert OEE Records
-- =============================================
INSERT INTO equip_oee_record (id, equip_id, record_date, shift, plan_runtime, actual_runtime, downtime, output_qty, defect_qty, availability, performance, quality, oee) VALUES
(1, 3, CURRENT_DATE(), '白班', 480, 450, 30, 18000, 120, 93.75, 88.50, 99.33, 82.50),
(2, 4, CURRENT_DATE(), '白班', 480, 460, 20, 40000, 85, 95.83, 92.00, 99.79, 88.00),
(3, 3, DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '白班', 480, 440, 40, 17500, 100, 91.67, 87.00, 99.43, 79.30),
(4, 4, DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), '白班', 480, 455, 25, 38000, 90, 94.79, 90.50, 99.76, 85.50);

-- =============================================
-- Insert Energy Records
-- =============================================
INSERT INTO equip_energy_record (id, equip_id, record_date, electricity, water, gas, compressed_air, output_qty, unit_energy) VALUES
(1, 3, CURRENT_DATE(), 1250.50, 15.2, 80.5, 320.0, 18000, 0.092),
(2, 4, CURRENT_DATE(), 980.30, 12.8, 65.0, 280.0, 40000, 0.033),
(3, 3, DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 1180.00, 14.5, 78.0, 310.0, 17500, 0.090),
(4, 4, DATE_SUB(CURRENT_DATE(), INTERVAL 1 DAY), 950.00, 12.0, 62.5, 270.0, 38000, 0.034);

-- =============================================
-- Insert Wage Records
-- =============================================
INSERT INTO wages_record (employee_name, employee_code, work_order_id, process_code, quantity, unit_price, total_amount, record_date) VALUES
('张伟', 'EMP001', 1, 'PRC-004', 500, 0.15, 75.00, CURRENT_DATE()),
('李明', 'EMP002', 1, 'PRC-004', 450, 0.15, 67.50, CURRENT_DATE()),
('王芳', 'EMP003', 1, 'PRC-005', 200, 0.12, 24.00, CURRENT_DATE()),
('赵强', 'EMP004', 2, 'PRC-004', 800, 0.15, 120.00, CURRENT_DATE()),
('孙丽', 'EMP005', 2, 'PRC-008', 1200, 0.10, 120.00, CURRENT_DATE()),
('周杰', 'EMP006', 3, 'PRC-004', 100, 0.20, 20.00, CURRENT_DATE()),
('吴敏', 'EMP007', 4, 'PRC-008', 600, 0.10, 60.00, CURRENT_DATE()),
('陈宇', 'EMP008', 5, 'PRC-001', 50, 0.18, 9.00, CURRENT_DATE());
