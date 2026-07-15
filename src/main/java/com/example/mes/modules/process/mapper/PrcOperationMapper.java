package com.example.mes.modules.process.mapper;

import com.example.mes.modules.process.entity.PrcOperation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrcOperationMapper {
    int insert(PrcOperation entity);
    int update(PrcOperation entity);
    PrcOperation selectById(@Param("id") Long id);
    List<PrcOperation> selectList(@Param("operationCode") String operationCode,
                                   @Param("operationName") String operationName,
                                   @Param("operationType") Integer operationType,
                                   @Param("status") Integer status,
                                   @Param("keyword") String keyword,
                                   @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("operationCode") String operationCode,
                   @Param("operationName") String operationName,
                   @Param("operationType") Integer operationType,
                   @Param("status") Integer status,
                   @Param("keyword") String keyword);
    int deleteById(@Param("id") Long id);
}
