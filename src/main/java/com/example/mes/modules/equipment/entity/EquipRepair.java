package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipRepair {
    private Long id;
    private String repairNo;
    private Long equipmentId;
    private String faultType;
    private String faultReason;
    private String failureDesc;
    private String reporter;
    private String repairPerson;
    private LocalDateTime reportTime;
    private LocalDateTime repairTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String repairContent;
    private Integer status;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
