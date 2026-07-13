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
public class AndonReason {

    private Long id;
    private String reasonName;
    private String reasonCode;
    private Long andonTypeId;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
