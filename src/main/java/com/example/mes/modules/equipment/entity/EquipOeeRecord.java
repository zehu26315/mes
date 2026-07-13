package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipOeeRecord {
    private Long id;
    private Long equipId;
    private LocalDate recordDate;
    private String shift;
    private Integer planRuntime;
    private Integer actualRuntime;
    private Integer downtime;
    private BigDecimal outputQty;
    private BigDecimal defectQty;
    private BigDecimal availability;
    private BigDecimal performance;
    private BigDecimal quality;
    private BigDecimal oee;
    private LocalDateTime createTime;
}
