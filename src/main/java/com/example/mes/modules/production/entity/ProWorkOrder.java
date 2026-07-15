package com.example.mes.modules.production.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProWorkOrder {
    private Long id;
    private String workOrderNo;
    private Long materialId;
    private BigDecimal planQuantity;
    private BigDecimal actualQuantity;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private Long routeId;
    private Integer sourceType;
    private Integer status;
    private Integer priority;
    private String remark;
    private Long createBy;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
