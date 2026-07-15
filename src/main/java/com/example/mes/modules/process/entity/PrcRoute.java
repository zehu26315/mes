package com.example.mes.modules.process.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrcRoute {
    private Long id;
    private String routeCode;
    private String routeName;
    private Long materialId;
    private String version;
    private String description;
    private String productModel;
    private String processIds;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
