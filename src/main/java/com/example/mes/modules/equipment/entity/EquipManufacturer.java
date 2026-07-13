package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipManufacturer {
    private Long id;
    private String manufacturerName;
    private String manufacturerCode;
    private String contactPerson;
    private String contactPhone;
    private String address;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
