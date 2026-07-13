package com.example.mes.modules.plan.entity;

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
public class PlanSchedule {

    private Long id;
    private String scheduleNo;
    private Long workOrderId;
    private String productCode;
    private String processCode;
    private Long equipmentId;
    private String productionLine;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private BigDecimal planQty;
    private Integer priority;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
