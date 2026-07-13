package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipFault {
    private Long id;
    private Long equipId;
    private String faultCode;
    private String faultName;
    private String faultDesc;
    private String faultCategory;
    private String reasonAnalysis;
    private String solution;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
