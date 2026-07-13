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
public class PlanOrderSplit {

    private Long id;
    private String sourceOrderNo;
    private String childOrderNo;
    private BigDecimal sourceQty;
    private BigDecimal splitQty;
    private LocalDate splitDate;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
