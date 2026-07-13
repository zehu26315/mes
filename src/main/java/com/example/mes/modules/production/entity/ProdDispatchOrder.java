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
public class ProdDispatchOrder {

    private Long id;
    private String dispatchNo;
    private Long workOrderId;
    private String productCode;
    private BigDecimal quantity;
    private String processCode;
    private String processName;
    private String workCenter;
    private Long equipmentId;
    private String assignedTo;
    private Integer status;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
