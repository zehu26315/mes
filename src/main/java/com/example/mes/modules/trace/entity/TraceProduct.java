package com.example.mes.modules.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraceProduct {

    private Long id;
    private String barcodeNo;
    private String productCode;
    private String productName;
    private Long workOrderId;
    private String productionLine;
    private Long equipmentId;
    private String processCode;
    private String worker;
    private String shift;
    private String batchNo;
    private LocalDate productionDate;
    private LocalDateTime productionTime;
    private String inspectionResult;
    private LocalDateTime createTime;
}
