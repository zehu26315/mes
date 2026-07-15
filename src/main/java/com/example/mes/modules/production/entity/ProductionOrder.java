package com.example.mes.modules.production.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductionOrder {
    private Long id;
    private String orderNo;
    private String productModel;
    private String batchNo;
    private Integer plannedQty;
    private Integer completedQty;
    private String productionLine;
    private Integer status;
    private LocalDate deliveryDate;
    private String createBy;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
