package com.example.mes.modules.production.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProCompletion {
    private Long id;
    private String completionNo;
    private Long workOrderId;
    private Long materialId;
    private BigDecimal planQuantity;
    private BigDecimal completedQuantity;
    private BigDecimal qualifiedQuantity;
    private BigDecimal defectQuantity;
    private Long warehouseId;
    private Long locationId;
    private LocalDateTime completionTime;
    private Long inspectorId;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
