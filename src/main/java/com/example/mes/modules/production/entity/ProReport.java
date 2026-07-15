package com.example.mes.modules.production.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProReport {
    private Long id;
    private String reportNo;
    private Long orderId;
    private Long taskId;
    private Long operationId;
    private String processName;
    private String worker;
    private String workstation;
    private Long workerId;
    private Integer reportType;
    private BigDecimal reportQuantity;
    private BigDecimal qualifiedQuantity;
    private BigDecimal defectQuantity;
    private String defectReason;
    private Long equipmentId;
    private String barcode;
    private LocalDate reportDate;
    private String shift;
    private LocalDateTime reportTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
