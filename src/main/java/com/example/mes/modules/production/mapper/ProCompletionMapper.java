package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProCompletion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProCompletionMapper {
    int insert(ProCompletion entity);
    int update(ProCompletion entity);
    ProCompletion selectById(@Param("id") Long id);
    List<ProCompletion> selectList(@Param("completionNo") String completionNo, @Param("workOrderId") Long workOrderId,
                                    @Param("materialId") Long materialId, @Param("status") Integer status,
                                    @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("completionNo") String completionNo, @Param("workOrderId") Long workOrderId,
                   @Param("materialId") Long materialId, @Param("status") Integer status);
    int deleteById(@Param("id") Long id);
}
