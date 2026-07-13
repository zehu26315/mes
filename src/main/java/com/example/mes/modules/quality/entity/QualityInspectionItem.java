package com.example.mes.modules.quality.entity;

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
public class QualityInspectionItem {

    private Long id;
    private String itemCode;
    private String itemName;
    private Long categoryId;
    private String unit;
    private BigDecimal standardValue;
    private BigDecimal upperLimit;
    private BigDecimal lowerLimit;
    private String inspectionType;
    private String testMethod;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
