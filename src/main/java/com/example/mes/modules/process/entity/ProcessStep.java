package com.example.mes.modules.process.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessStep {

    private Long id;
    private Long routeId;
    private String stepCode;
    private String stepName;
    private String stepType;
    private Integer sortOrder;
    private String workCenter;
    private BigDecimal standardTime;
    private BigDecimal yieldRate;
    private String sopFile;
    private Long prevStepId;
    private Long nextStepId;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
