package com.example.mes.modules.warehouse.mapper;

import com.example.mes.modules.warehouse.entity.WhTransfer;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WhTransferMapper {
    int insert(WhTransfer entity);
    int update(WhTransfer entity);
    WhTransfer selectById(@Param("id") Long id);
    List<WhTransfer> selectList(@Param("status") String status, @Param("fromWarehouseId") Long fromWarehouseId,
                                @Param("toWarehouseId") Long toWarehouseId, @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("status") String status, @Param("fromWarehouseId") Long fromWarehouseId, @Param("toWarehouseId") Long toWarehouseId);
    int deleteById(@Param("id") Long id);
}
