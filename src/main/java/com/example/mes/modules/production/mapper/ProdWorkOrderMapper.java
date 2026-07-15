package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProdWorkOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProdWorkOrderMapper {

    int insert(ProdWorkOrder workOrder);

    int update(ProdWorkOrder workOrder);

    ProdWorkOrder selectById(@Param("id") Long id);

    List<ProdWorkOrder> selectList(@Param("keyword") String keyword,
                                   @Param("status") String status,
                                   @Param("startDate") String startDate,
                                   @Param("endDate") String endDate,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") String status,
                   @Param("startDate") String startDate,
                   @Param("endDate") String endDate);

    int deleteById(@Param("id") Long id);

    ProdWorkOrder selectByOrderNo(@Param("orderNo") String orderNo);

    int updateStatus(@Param("id") Long id,
                     @Param("status") String status);
}
