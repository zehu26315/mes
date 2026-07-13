package com.example.mes.modules.material.entity;

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
public class MatInventoryLog {

    private Long id;
    private Long materialId;
    private String materialCode;
    private String changeType;
    private BigDecimal changeQty;
    private BigDecimal beforeQty;
    private BigDecimal afterQty;
    private String referenceNo;
    private String operator;
    private LocalDateTime createTime;
}
