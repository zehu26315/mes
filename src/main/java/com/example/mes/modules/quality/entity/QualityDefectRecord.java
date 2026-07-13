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
public class QualityDefectRecord {

    private Long id;
    private Long workOrderId;
    private String productCode;
    private String processCode;
    private String defectType;
    private String defectLocation;
    private String defectDesc;
    private Integer defectQty;
    private String rootCause;
    private String correctiveAction;
    private String reporter;
    private LocalDateTime createTime;
}
