package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProdDispatchOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProdDispatchOrderMapper {

    int insert(ProdDispatchOrder dispatchOrder);

    int update(ProdDispatchOrder dispatchOrder);

    ProdDispatchOrder selectById(@Param("id") Long id);

    List<ProdDispatchOrder> selectList(@Param("workOrderId") Long workOrderId,
                                       @Param("status") Integer status,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    long countList(@Param("workOrderId") Long workOrderId,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
