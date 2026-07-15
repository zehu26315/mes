package com.example.mes.modules.process.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrcRouteDetail {
    private Long id;
    private Long routeId;
    private Long operationId;
    private Integer seqNo;
    private BigDecimal standardTime;
    private BigDecimal price;
    private Long prevOpId;
    private Long nextOpId;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
