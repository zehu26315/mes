package com.example.mes.modules.barcode.entity;

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
public class BarcodeTemplate {

    private Long id;
    private String templateName;
    private String templateCode;
    private String templateContent;
    private BigDecimal width;
    private BigDecimal height;
    private String description;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
