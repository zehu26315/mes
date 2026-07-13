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
public class SysOperationLog {

    private Long id;
    private Long userId;
    private String username;
    private String operation;
    private String method;
    private String params;
    private String ip;
    private Integer status;
    private String errorMsg;
    private Long duration;
    private LocalDateTime createTime;
}
