package com.example.mes.config;

/**
 * Centralized cache name constants to avoid string typos in {@link org.springframework.cache.annotation.Cacheable}
 * and {@link org.springframework.cache.annotation.CacheEvict} annotations.
 */
public interface CacheNames {

    // ========== System: User ==========
    String SYS_USER = "sys_user";
    String SYS_USER_LIST = "sys_user_list";

    // ========== System: Notice ==========
    String SYS_NOTICE = "sys_notice";
    String SYS_NOTICE_LIST = "sys_notice_list";

    // ========== System: Login Log ==========
    String SYS_LOGIN_LOG = "sys_login_log";

    // ========== Equipment: Ledger ==========
    String EQUIP_LEDGER = "equip_ledger";
    String EQUIP_LEDGER_LIST = "equip_ledger_list";

    // ========== Equipment: Maintenance ==========
    String EQUIP_MAINTENANCE = "equip_maintenance";
    String EQUIP_MAINTENANCE_LIST = "equip_maintenance_list";

    // ========== Equipment: Inspection ==========
    String EQUIP_INSPECTION = "equip_inspection";
    String EQUIP_INSPECTION_LIST = "equip_inspection_list";

    // ========== Equipment: Repair ==========
    String EQUIP_REPAIR = "equip_repair";
    String EQUIP_REPAIR_LIST = "equip_repair_list";

    // ========== Andon: Config ==========
    String ANDON_CONFIG = "andon_config";
    String ANDON_CONFIG_LIST = "andon_config_list";

    // ========== Andon: Record ==========
    String ANDON_RECORD = "andon_record";
    String ANDON_RECORD_LIST = "andon_record_list";

    // ========== Barcode: Rule ==========
    String BARCODE_RULE = "barcode_rule";
    String BARCODE_RULE_LIST = "barcode_rule_list";

    // ========== Barcode: Record ==========
    String BARCODE_RECORD = "barcode_record";
    String BARCODE_RECORD_LIST = "barcode_record_list";

    // ========== Production: Order ==========
    String PROD_ORDER = "prod_order";
    String PROD_ORDER_LIST = "prod_order_list";

    // ========== Production: WorkOrder ==========
    String PROD_WORK_ORDER = "prod_work_order";
    String PROD_WORK_ORDER_LIST = "prod_work_order_list";

    // ========== Production: Task ==========
    String PROD_TASK = "prod_task";
    String PROD_TASK_LIST = "prod_task_list";

    // ========== Production: Dispatch ==========
    String PROD_DISPATCH = "prod_dispatch";
    String PROD_DISPATCH_LIST = "prod_dispatch_list";

    // ========== Production: Completion ==========
    String PROD_COMPLETION = "prod_completion";
    String PROD_COMPLETION_LIST = "prod_completion_list";

    // ========== Production: Repair ==========
    String PROD_REPAIR = "prod_repair";
    String PROD_REPAIR_LIST = "prod_repair_list";

    // ========== Production: Report ==========
    String PROD_REPORT = "prod_report";
    String PROD_REPORT_LIST = "prod_report_list";

    // ========== Process: Operation ==========
    String PRC_OPERATION = "prc_operation";
    String PRC_OPERATION_LIST = "prc_operation_list";

    // ========== Process: Route ==========
    String PRC_ROUTE = "prc_route";
    String PRC_ROUTE_LIST = "prc_route_list";

    // ========== Process: RouteDetail ==========
    String PRC_ROUTE_DETAIL = "prc_route_detail";
    String PRC_ROUTE_DETAIL_LIST = "prc_route_detail_list";

    // ========== Process: SOP ==========
    String PRC_SOP = "prc_sop";
    String PRC_SOP_LIST = "prc_sop_list";

    // ========== Quality: Inspection ==========
    String QUALITY_INSPECTION = "quality_inspection";
    String QUALITY_INSPECTION_LIST = "quality_inspection_list";

    // ========== Wages: PieceWage ==========
    String WAGES_PIECE = "wages_piece";
    String WAGES_PIECE_LIST = "wages_piece_list";

    // ========== Trace: Material ==========
    String TRACE_MATERIAL = "trace_material";
    String TRACE_MATERIAL_LIST = "trace_material_list";

    // ========== Trace: Product ==========
    String TRACE_PRODUCT = "trace_product";
    String TRACE_PRODUCT_LIST = "trace_product_list";

}
