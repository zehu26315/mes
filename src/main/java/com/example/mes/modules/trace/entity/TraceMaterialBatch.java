package com.example.mes.modules.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraceMaterialBatch {

    private Long id;
    private String batchNo;
    private Long materialId;
    private String materialCode;
    private String materialName;
    private String supplier;
    private String supplierBatch;
    private LocalDate receivedDate;
    private BigDecimal qty;
    private String inspectionResult;
    private String certificateNo;
    private LocalDateTime createTime;
}
