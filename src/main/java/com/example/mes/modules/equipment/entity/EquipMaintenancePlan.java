package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipMaintenancePlan {
    private Long id;
    private String planNo;
    private Long equipId;
    private String maintenanceType;
    private LocalDate planDate;
    private String content;
    private String responsiblePerson;
    private String status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
