package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipMaintenance {
    private Long id;
    private String taskNo;
    private Long equipmentId;
    /** 日常保养/一级保养/二级保养 */
    private String maintenanceType;
    private LocalDate planDate;
    private LocalDate actualDate;
    private String maintenanceContent;
    private String operator;
    private String result;
    /** 0=待保养, 1=已完成, 2=逾期 */
    private Integer status;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
