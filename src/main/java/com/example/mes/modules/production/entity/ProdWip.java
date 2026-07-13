package com.example.mes.modules.production.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdWip {

    private Long id;
    private Long workOrderId;
    private String productCode;
    private String productName;
    private String processCode;
    private String processName;
    private String workCenter;
    private BigDecimal wipQty;
    private BigDecimal holdQty;
    private LocalDateTime updateTime;
}
