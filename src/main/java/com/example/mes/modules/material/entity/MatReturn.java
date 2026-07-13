package com.example.mes.modules.material.entity;

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
public class MatReturn {

    private Long id;
    private String returnNo;
    private Long workOrderId;
    private Long reqId;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private BigDecimal returnQty;
    private String unit;
    private String returnReason;
    private String warehouse;
    private String status;
    private String applicant;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
