package com.example.mes.modules.material.entity;

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
public class MatInventory {

    private Long id;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String warehouse;
    private String location;
    private String batchNo;
    private BigDecimal qty;
    private BigDecimal lockedQty;
    private String unit;
    private String supplier;
    private LocalDate productionDate;
    private LocalDate expiryDate;
    private LocalDateTime updateTime;
}
