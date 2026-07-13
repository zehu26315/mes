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
public class SysRoleMenu {

    private Long id;
    private Long roleId;
    private Long menuId;
    private LocalDateTime createTime;
}
