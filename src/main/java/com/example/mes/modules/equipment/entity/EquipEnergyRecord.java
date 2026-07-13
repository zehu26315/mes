package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipEnergyRecord {
    private Long id;
    private Long equipId;
    private LocalDate recordDate;
    private Integer recordHour;
    private BigDecimal electricity;
    private BigDecimal water;
    private BigDecimal gas;
    private BigDecimal compressedAir;
    private BigDecimal steam;
    private BigDecimal outputQty;
    private BigDecimal unitEnergy;
    private LocalDateTime createTime;
}
