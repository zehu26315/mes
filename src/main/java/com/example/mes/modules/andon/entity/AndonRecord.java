package com.example.mes.modules.andon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AndonRecord {

    private Long id;
    private Long andonConfigId;
    private Long reasonId;
    private String productionLine;
    private String workstation;
    private String exceptionType;
    private Long initiatorId;
    private Long handlerId;
    private Long operationId;
    private Long equipmentId;
    private String description;
    private String reportUser;
    private String handleUser;
    private LocalDateTime initTime;
    private LocalDateTime confirmTime;
    private LocalDateTime handleTime;
    private String handleResult;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
