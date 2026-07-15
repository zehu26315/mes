package com.example.mes.modules.production.constant;

import java.util.Arrays;

/**
 * 生产工单状态。数据库 {@code prod_work_order.status} 列存储中文标签，
 * 与 init.sql 的默认值及 DEFAULT 约定保持一致。作为看板、报表、状态流转
 * 的唯一取值来源，避免各处硬编码魔法字符串导致静默漏统计。
 */
public enum WorkOrderStatus {

    PENDING("待生产"),
    IN_PROGRESS("生产中"),
    COMPLETED("已完成"),
    PAUSED("暂停");

    private final String code;

    WorkOrderStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /** 判断给定字符串是否为合法的工单状态标签。 */
    public static boolean isValid(String code) {
        return code != null && Arrays.stream(values()).anyMatch(s -> s.code.equals(code));
    }
}
