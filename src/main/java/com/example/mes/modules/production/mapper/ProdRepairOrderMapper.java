package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProdRepairOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProdRepairOrderMapper {

    int insert(ProdRepairOrder repairOrder);

    int update(ProdRepairOrder repairOrder);

    ProdRepairOrder selectById(@Param("id") Long id);

    List<ProdRepairOrder> selectList(@Param("offset") int offset,
                                     @Param("limit") int limit);

    long countList();

    int deleteById(@Param("id") Long id);

    int complete(@Param("id") Long id,
                 @Param("result") String result);
}
