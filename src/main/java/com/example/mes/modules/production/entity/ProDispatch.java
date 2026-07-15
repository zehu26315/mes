package com.example.mes.modules.production.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProDispatch {
    private Long id;
    private String dispatchNo;
    private Long workOrderId;
    private Long operationId;
    private String productionLine;
    private BigDecimal planQuantity;
    private BigDecimal actualQuantity;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private Long assigneeId;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
