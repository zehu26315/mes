package com.example.mes.modules.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpSyncLog {

    private Long id;
    private String syncType;
    private String dataType;
    private String sourceSystem;
    private Integer recordCount;
    private Integer successCount;
    private Integer failCount;
    private String status;
    private String errorMsg;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
}
