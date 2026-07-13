package com.example.mes.modules.warehouse.mapper;

import com.example.mes.modules.warehouse.entity.WhOutbound;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WhOutboundMapper {
    int insert(WhOutbound entity);
    int update(WhOutbound entity);
    WhOutbound selectById(@Param("id") Long id);
    List<WhOutbound> selectList(@Param("type") String type, @Param("warehouseId") Long warehouseId,
                                @Param("status") String status, @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("type") String type, @Param("warehouseId") Long warehouseId, @Param("status") String status);
    int deleteById(@Param("id") Long id);
}
