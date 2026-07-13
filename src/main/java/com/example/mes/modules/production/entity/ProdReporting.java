package com.example.mes.modules.production.entity;

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
public class ProdReporting {

    private Long id;
    private Long workOrderId;
    private Long dispatchId;
    private String processCode;
    private String processName;
    private String workCenter;
    private Long equipmentId;
    private String worker;
    private String shift;
    private String reportType;
    private BigDecimal totalQty;
    private BigDecimal goodQty;
    private BigDecimal defectQty;
    private String defectReason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
