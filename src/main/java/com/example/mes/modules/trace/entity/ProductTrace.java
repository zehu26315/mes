package com.example.mes.modules.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductTrace {
    private Long id;
    private Long orderId;
    private String barcode;
    private String productModel;
    private String batchNo;
    private String productionLine;
    private Integer inspectionResult;
    private String operator;
    private LocalDateTime productionTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
