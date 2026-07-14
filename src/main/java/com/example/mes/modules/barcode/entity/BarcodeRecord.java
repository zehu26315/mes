package com.example.mes.modules.barcode.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeRecord {

    private Long id;
    private String barcodeNo;
    private Long barcodeTypeId;
    private Long ruleId;
    private String productCode;
    private Long workOrderId;
    private String batchNo;
    private String status;
    private Integer printCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
