package com.example.mes.modules.production.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProTask {
    private Long id;
    private String taskNo;
    private Long workOrderId;
    private Long dispatchId;
    private String productionLine;
    private BigDecimal planQuantity;
    private BigDecimal completeQuantity;
    private BigDecimal defectQuantity;
    private Integer sourceType;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
