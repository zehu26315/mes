package com.example.mes.modules.plan.entity;

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
public class PlanMps {

    private Long id;
    private String planNo;
    private String planName;
    private String planPeriod;
    private LocalDate planDate;
    private String productCode;
    private String productName;
    private BigDecimal planQty;
    private BigDecimal completedQty;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
