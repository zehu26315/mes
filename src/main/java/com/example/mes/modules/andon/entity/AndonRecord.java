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
    private Long andonTypeId;
    private Long reasonId;
    private String workCenter;
    private String productionLine;
    private String equipmentId;
    private String workOrderId;
    private String description;
    private String status;
    private String severity;
    private String reporter;
    private String handler;
    private LocalDateTime reportTime;
    private LocalDateTime confirmTime;
    private LocalDateTime resolveTime;
    private String resolveDesc;
    private Long duration;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
