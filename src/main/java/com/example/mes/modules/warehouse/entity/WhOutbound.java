package com.example.mes.modules.warehouse.entity;

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
public class WhOutbound {
    private Long id;
    private String outboundNo;
    private String outboundType;
    private Long warehouseId;
    private String materialCode;
    private String materialName;
    private BigDecimal qty;
    private String unit;
    private String batchNo;
    private String targetOrderNo;
    private String status;
    private String operator;
    private LocalDateTime outboundTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
