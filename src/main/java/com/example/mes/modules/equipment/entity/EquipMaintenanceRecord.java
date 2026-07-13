package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipMaintenanceRecord {
    private Long id;
    private Long planId;
    private Long equipId;
    private LocalDate executeDate;
    private String result;
    private String executor;
    private String remark;
    private BigDecimal cost;
    private LocalDateTime createTime;
}
