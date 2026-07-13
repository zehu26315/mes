package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipRepair {
    private Long id;
    private String repairNo;
    private Long equipId;
    private Long faultId;
    private String faultDesc;
    private String repairType;
    private String reporter;
    private String repairPerson;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long downtime;
    private String repairContent;
    private String partsUsed;
    private BigDecimal cost;
    private String status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
