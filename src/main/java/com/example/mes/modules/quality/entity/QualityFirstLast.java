package com.example.mes.modules.quality.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityFirstLast {

    private Long id;
    private String orderNo;
    private Long workOrderId;
    private String inspectionType;
    private String productCode;
    private String processCode;
    private Long equipmentId;
    private String result;
    private String inspector;
    private LocalDateTime inspectionTime;
    private String measurementData;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
