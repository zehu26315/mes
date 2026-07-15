package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProductionOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProductionOrderMapper {
    int insert(ProductionOrder entity);
    int update(ProductionOrder entity);
    ProductionOrder selectById(@Param("id") Long id);
    List<ProductionOrder> selectList(@Param("keyword") String keyword, @Param("status") Integer status,
                                     @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("keyword") String keyword, @Param("status") Integer status);
    int deleteById(@Param("id") Long id);

    List<Map<String, Object>> dashboardStatusStats();
    List<Map<String, Object>> dashboardDailyOutput();
    List<Map<String, Object>> dashboardLineOutput();
}
