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
public class QualityStandardDetail {

    private Long id;
    private Long standardId;
    private Long itemId;
    private BigDecimal standardValue;
    private BigDecimal upperLimit;
    private BigDecimal lowerLimit;
}
