package com.example.mes.modules.trace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MaterialTrace {
    private Long id;
    private Long orderId;
    private String materialCode;
    private String materialName;
    private String batchNo;
    private String supplier;
    private BigDecimal quantity;
    private String unit;
    private String operationName;
    private LocalDateTime operationTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
