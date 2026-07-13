package com.example.mes.modules.production.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdWorkOrder {

    private Long id;
    private String orderNo;
    private String productCode;
    private String productName;
    private BigDecimal quantity;
    private BigDecimal completedQty;
    private BigDecimal defectiveQty;
    private LocalDate startDate;
    private LocalDate endDate;
    private String currentProcess;
    private Integer status;
    private String workshop;
    private String productionLine;
    private String shift;
    private Integer priority;
    private String sourceOrderNo;
    private Long planId;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
