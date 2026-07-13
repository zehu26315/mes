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
public class MatSupplement {

    private Long id;
    private String suppNo;
    private Long workOrderId;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private BigDecimal suppQty;
    private String unit;
    private String suppReason;
    private String status;
    private String applicant;
    private String approver;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
