package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipInspectionRecord {
    private Long id;
    private Long planId;
    private Long equipId;
    private LocalDate executeDate;
    private String checkResult;
    private Integer isNormal;
    private String inspector;
    private String remark;
    private LocalDateTime createTime;
}
