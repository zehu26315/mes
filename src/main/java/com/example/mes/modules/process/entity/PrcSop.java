package com.example.mes.modules.process.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrcSop {
    private Long id;
    private String sopName;
    private Long operationId;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long fileSize;
    private Integer autoPlay;
    private Integer sortOrder;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
