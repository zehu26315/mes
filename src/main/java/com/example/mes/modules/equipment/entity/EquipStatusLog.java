package com.example.mes.modules.equipment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EquipStatusLog {
    private Long id;
    private Long equipId;
    private String fromStatus;
    private String toStatus;
    private String reason;
    private String operator;
    private LocalDateTime createTime;
}
