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
    private String barcode;
    private String productModel;
    private String batchNo;
    private String orderNo;
    private String productionLine;
    private LocalDateTime printTime;
    private String printUser;
    private Long ruleId;
    private Integer isDeleted;
    private LocalDateTime createTime;
}
