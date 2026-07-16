package com.example.mes.modules.trace.mapper;

import com.example.mes.modules.trace.entity.MaterialTrace;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialTraceMapper {
    int insert(MaterialTrace entity);
    int update(MaterialTrace entity);
    MaterialTrace selectById(@Param("id") Long id);
    List<MaterialTrace> selectList(@Param("orderId") Long orderId,
                                    @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("orderId") Long orderId);
    int deleteById(@Param("id") Long id);
}
