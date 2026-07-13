package com.example.mes.modules.warehouse.mapper;

import com.example.mes.modules.warehouse.entity.WhWarehouse;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WhWarehouseMapper {
    int insert(WhWarehouse entity);
    int update(WhWarehouse entity);
    WhWarehouse selectById(@Param("id") Long id);
    List<WhWarehouse> selectAll();
    int deleteById(@Param("id") Long id);
}
