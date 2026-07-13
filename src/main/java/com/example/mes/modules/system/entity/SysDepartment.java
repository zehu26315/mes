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
public class SysDepartment {

    private Long id;
    private Long parentId;
    private String deptName;
    private String deptCode;
    private String leader;
    private String phone;
    private Integer sortOrder;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
