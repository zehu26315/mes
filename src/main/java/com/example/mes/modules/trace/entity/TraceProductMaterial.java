package com.example.mes.modules.trace.entity;

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
public class TraceProductMaterial {

    private Long id;
    private String barcodeNo;
    private String materialBatchNo;
    private String materialCode;
    private BigDecimal usedQty;
    private String processCode;
    private LocalDateTime createTime;
}
