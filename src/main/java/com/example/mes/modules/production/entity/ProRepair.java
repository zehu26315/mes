package com.example.mes.modules.production.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProRepair {
    private Long id;
    private String repairNo;
    private Long workOrderId;
    private Long completionId;
    private Long materialId;
    private String barcode;
    private BigDecimal repairQuantity;
    private String repairReason;
    private Integer repairType;
    private Long assigneeId;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
