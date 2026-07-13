package com.example.mes.modules.masterdata.entity;

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
public class MdMaterial {

    private Long id;
    private String materialCode;
    private String materialName;
    private String materialSpec;
    private Long categoryId;
    private String unit;
    private BigDecimal safetyStock;
    private BigDecimal currentStock;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
