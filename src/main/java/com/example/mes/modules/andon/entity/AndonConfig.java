package com.example.mes.modules.andon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AndonConfig {

    private Long id;
    private String configName;
    private Long andonTypeId;
    private String workCenter;
    private String triggerCondition;
    private String notifyUsers;
    private Integer escalationTime;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
