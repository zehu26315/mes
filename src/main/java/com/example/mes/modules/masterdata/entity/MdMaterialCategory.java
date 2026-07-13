package com.example.mes.modules.masterdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MdMaterialCategory {

    private Long id;
    private Long parentId;
    private String categoryName;
    private String categoryCode;
    private Integer sortOrder;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
