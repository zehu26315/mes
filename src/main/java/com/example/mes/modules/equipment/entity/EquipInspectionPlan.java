package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipInspectionPlan {
    private Long id;
    private String planNo;
    private Long equipId;
    private String inspectionType;
    private LocalDate planDate;
    private String checkPoints;
    private String responsiblePerson;
    private String status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
