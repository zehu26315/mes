package com.example.mes.modules.warehouse.mapper;

import com.example.mes.modules.warehouse.entity.WhCheck;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WhCheckMapper {
    int insert(WhCheck entity);
    int update(WhCheck entity);
    WhCheck selectById(@Param("id") Long id);
    List<WhCheck> selectList(@Param("warehouseId") Long warehouseId, @Param("status") String status,
                             @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("warehouseId") Long warehouseId, @Param("status") String status);
    int deleteById(@Param("id") Long id);
    int confirm(@Param("id") Long id);
}
