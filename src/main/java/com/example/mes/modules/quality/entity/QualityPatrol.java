package com.example.mes.modules.quality.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityPatrol {

    private Long id;
    private String orderNo;
    private Long workOrderId;
    private String productionLine;
    private String processCode;
    private Integer checkInterval;
    private Integer sampleSize;
    private String result;
    private String inspector;
    private LocalDateTime inspectionTime;
    private String findDesc;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
