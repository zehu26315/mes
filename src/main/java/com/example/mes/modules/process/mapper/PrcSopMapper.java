package com.example.mes.modules.process.mapper;

import com.example.mes.modules.process.entity.PrcSop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrcSopMapper {
    int insert(PrcSop entity);
    int update(PrcSop entity);
    PrcSop selectById(@Param("id") Long id);
    List<PrcSop> selectByOperationId(@Param("operationId") Long operationId);
    List<PrcSop> selectList(@Param("operationId") Long operationId,
                             @Param("keyword") String keyword,
                             @Param("offset") int offset,
                             @Param("limit") int limit);
    long countList(@Param("operationId") Long operationId,
                   @Param("keyword") String keyword);
    int deleteById(@Param("id") Long id);
}
