package com.example.mes.modules.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu {

    private Long id;
    private Long parentId;
    private String menuName;
    private String menuCode;
    private String menuType;
    private String url;
    private String icon;
    private Integer sortOrder;
    private Integer visible;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
