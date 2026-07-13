package com.example.mes.modules.quality.entity;

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
public class QualityInspectionOrder {

    private Long id;
    private String orderNo;
    private String orderType;
    private String sourceOrderNo;
    private String productCode;
    private String productName;
    private String batchNo;
    private Integer sampleQty;
    private Integer defectQty;
    private String result;
    private String inspector;
    private LocalDateTime inspectionTime;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
