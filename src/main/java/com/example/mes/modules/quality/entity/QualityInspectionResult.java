package com.example.mes.modules.quality.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityInspectionResult {

    private Long id;
    private Long orderId;
    private Long itemId;
    private String itemName;
    private BigDecimal standardValue;
    private BigDecimal upperLimit;
    private BigDecimal lowerLimit;
    private BigDecimal actualValue;
    private String unit;
    private Integer isPass;
}
