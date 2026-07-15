package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipInspection {
    private Long id;
    private String inspectionNo;
    private Long equipmentId;
    private LocalDateTime inspectionDate;
    private String inspector;
    private String inspectionItems;
    /** 0=正常, 1=异常 */
    private Integer result;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
