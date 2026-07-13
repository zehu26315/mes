package com.example.mes.modules.warehouse.mapper;

import com.example.mes.modules.warehouse.entity.WhInbound;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WhInboundMapper {
    int insert(WhInbound entity);
    int update(WhInbound entity);
    WhInbound selectById(@Param("id") Long id);
    List<WhInbound> selectList(@Param("type") String type, @Param("warehouseId") Long warehouseId,
                               @Param("status") String status, @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("type") String type, @Param("warehouseId") Long warehouseId, @Param("status") String status);
    int deleteById(@Param("id") Long id);
}
