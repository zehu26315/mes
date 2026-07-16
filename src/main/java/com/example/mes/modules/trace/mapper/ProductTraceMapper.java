package com.example.mes.modules.trace.mapper;

import com.example.mes.modules.trace.entity.ProductTrace;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductTraceMapper {
    int insert(ProductTrace entity);
    int update(ProductTrace entity);
    ProductTrace selectById(@Param("id") Long id);
    List<ProductTrace> selectList(@Param("keyword") String keyword,
                                   @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("keyword") String keyword);
    int deleteById(@Param("id") Long id);
}
