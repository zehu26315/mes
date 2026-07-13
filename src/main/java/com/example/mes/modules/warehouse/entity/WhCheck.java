package com.example.mes.modules.warehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhCheck {
    private Long id;
    private String checkNo;
    private Long warehouseId;
    private String checkType;
    private LocalDate checkDate;
    private String materialCode;
    private BigDecimal bookQty;
    private BigDecimal actualQty;
    private BigDecimal diffQty;
    private String diffReason;
    private String status;
    private String checker;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
