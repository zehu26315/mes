package com.example.mes.config;

/**
 * Centralized cache name constants to avoid string typos in {@link org.springframework.cache.annotation.Cacheable}
 * and {@link org.springframework.cache.annotation.CacheEvict} annotations.
 */
public interface CacheNames {

    // ========== System: User ==========
    String SYS_USER = "sys_user";
    String SYS_USER_LIST = "sys_user_list";

    // ========== System: Role ==========
    String SYS_ROLE = "sys_role";
    String SYS_ROLE_ALL = "sys_role_all";
    String SYS_ROLE_LIST = "sys_role_list";

    // ========== System: Menu ==========
    String SYS_MENU = "sys_menu";
    String SYS_MENU_TREE = "sys_menu_tree";
    String SYS_MENU_LIST = "sys_menu_list";

    // ========== System: Department ==========
    String SYS_DEPT = "sys_dept";
    String SYS_DEPT_TREE = "sys_dept_tree";
    String SYS_DEPT_LIST = "sys_dept_list";

    // ========== System: Config ==========
    String SYS_CONFIG = "sys_config";
    String SYS_CONFIG_ALL = "sys_config_all";

    // ========== Master Data: Product ==========
    String MD_PRODUCT = "md_product";
    String MD_PRODUCT_LIST = "md_product_list";

    // ========== Master Data: Material ==========
    String MD_MATERIAL = "md_material";
    String MD_MATERIAL_LIST = "md_material_list";

    // ========== Master Data: Material Category ==========
    String MD_MATERIAL_CATEGORY = "md_material_category";
    String MD_MATERIAL_CATEGORY_LIST = "md_material_category_list";

    // ========== Process: Route ==========
    String PROCESS_ROUTE = "process_route";
    String PROCESS_ROUTE_LIST = "process_route_list";

    // ========== Process: Step ==========
    String PROCESS_STEP = "process_step";

    // ========== Warehouse ==========
    String WH_WAREHOUSE = "wh_warehouse";
    String WH_WAREHOUSE_ALL = "wh_warehouse_all";

    // ========== Equipment ==========
    String EQUIP_CATEGORY = "equip_category";
    String EQUIP_CATEGORY_TREE = "equip_category_tree";
    String EQUIP_MANUFACTURER = "equip_manufacturer";
    String EQUIP_MANUFACTURER_ALL = "equip_manufacturer_all";
    String EQUIP_ASSET = "equip_asset";
    String EQUIP_ASSET_LIST = "equip_asset_list";

    // ========== Wages ==========
    String WAGES_RECORD = "wages_record";
    String WAGES_RECORD_LIST = "wages_record_list";

    // ========== Plan ==========
    String PLAN_MPS = "plan_mps";
    String PLAN_MPS_LIST = "plan_mps_list";
    String PLAN_DAILY = "plan_daily";
    String PLAN_DAILY_LIST = "plan_daily_list";
    String PLAN_SCHEDULE = "plan_schedule";
    String PLAN_SCHEDULE_LIST = "plan_schedule_list";
    String PLAN_ORDER_SPLIT = "plan_order_split";
    String PLAN_ORDER_SPLIT_LIST = "plan_order_split_list";

    // ========== Dashboard ==========
    String DASHBOARD = "dashboard";
}
