package com.example.mes.modules.masterdata.entity;

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
public class MdProduct {

    private Long id;
    private String productCode;
    private String productName;
    private String productSpec;
    private String productType;
    private String unit;
    private String category;
    private String voltage;
    private String capacity;
    private BigDecimal weight;
    private BigDecimal sizeDiameter;
    private BigDecimal sizeHeight;
    private Long routeId;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
