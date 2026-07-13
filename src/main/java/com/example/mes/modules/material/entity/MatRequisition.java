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
public class MatRequisition {

    private Long id;
    private String reqNo;
    private Long workOrderId;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private BigDecimal reqQty;
    private BigDecimal actualQty;
    private String unit;
    private String warehouse;
    private String location;
    private String batchNo;
    private String status;
    private String applicant;
    private String approver;
    private LocalDateTime issueTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
