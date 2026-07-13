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
public class BarcodeApplication {

    private Long id;
    private String applicationName;
    private Long ruleId;
    private Long templateId;
    private String productCode;
    private String triggerPoint;
    private Integer printQty;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
