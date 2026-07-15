package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProTaskMapper {
    int insert(ProTask entity);
    int update(ProTask entity);
    ProTask selectById(@Param("id") Long id);
    List<ProTask> selectList(@Param("taskNo") String taskNo, @Param("workOrderId") Long workOrderId,
                             @Param("productionLine") String productionLine,
                             @Param("status") Integer status, @Param("sourceType") Integer sourceType,
                             @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("taskNo") String taskNo, @Param("workOrderId") Long workOrderId,
                   @Param("productionLine") String productionLine,
                   @Param("status") Integer status, @Param("sourceType") Integer sourceType);
    int deleteById(@Param("id") Long id);
}
