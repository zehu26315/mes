package com.example.mes.modules.wages.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PieceWage {
    private Long id;
    private String worker;
    private String processName;
    private BigDecimal unitPrice;
    private Integer completedQty;
    private BigDecimal totalWage;
    private LocalDate wageDate;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
