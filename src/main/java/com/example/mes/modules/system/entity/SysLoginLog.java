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
public class SysLoginLog {

    private Long id;
    private String loginName;
    private String ipAddress;
    private String loginLocation;
    private String browserType;
    private String os;
    /** 0=Success, 1=Failure */
    private Integer status;
    private String msg;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
