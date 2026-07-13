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
public class BarcodeRule {

    private Long id;
    private String ruleName;
    private String ruleCode;
    private Long barcodeTypeId;
    private String prefix;
    private String dateFormat;
    private Integer serialLength;
    private String separator;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
