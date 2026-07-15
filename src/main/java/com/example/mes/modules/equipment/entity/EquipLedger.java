package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipLedger {
    private Long id;
    private String equipmentCode;
    private String equipmentName;
    private String category;
    private String equipmentModel;
    private String manufacturer;
    private String workshop;
    private String serialNo;
    private LocalDate purchaseDate;
    private LocalDate warrantyDate;
    private String location;
    private String productionLine;
    private BigDecimal ratedPower;
    /** 0=正常, 1=停用, 2=维修中, 3=报废 */
    private Integer status;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
