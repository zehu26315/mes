package com.example.mes.modules.production.entity;

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
public class ProdRepairOrder {

    private Long id;
    private String repairNo;
    private Long workOrderId;
    private String productCode;
    private BigDecimal quantity;
    private String defectDesc;
    private String repairProcess;
    private Integer status;
    private String result;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
