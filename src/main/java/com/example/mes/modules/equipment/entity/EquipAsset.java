package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipAsset {
    private Long id;
    private String equipCode;
    private String equipName;
    private String equipModel;
    private String equipSpec;
    private Long categoryId;
    private Long manufacturerId;
    private Long departmentId;
    private String productionLine;
    private String workCenter;
    private String location;
    private String serialNo;
    private String status;
    private LocalDate purchaseDate;
    private LocalDate warrantyEnd;
    private Integer serviceLife;
    private String supplier;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
