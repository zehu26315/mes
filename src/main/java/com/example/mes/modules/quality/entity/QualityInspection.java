package com.example.mes.modules.quality.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class QualityInspection {
    private Long id;
    private String reportNo;
    private Long orderId;
    private String productModel;
    private String batchNo;
    private String inspectionType;
    private Integer sampleQty;
    private Integer defectQty;
    private Integer result;
    private String inspector;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
