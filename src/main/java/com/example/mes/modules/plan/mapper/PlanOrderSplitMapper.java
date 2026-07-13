package com.example.mes.modules.plan.mapper;

import com.example.mes.modules.plan.entity.PlanOrderSplit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanOrderSplitMapper {

    int insert(PlanOrderSplit planOrderSplit);

    int update(PlanOrderSplit planOrderSplit);

    PlanOrderSplit selectById(@Param("id") Long id);

    List<PlanOrderSplit> selectList(@Param("offset") int offset,
                                    @Param("limit") int limit);

    long countList();

    int deleteById(@Param("id") Long id);
}
