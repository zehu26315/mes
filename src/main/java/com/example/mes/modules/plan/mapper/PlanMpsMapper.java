package com.example.mes.modules.plan.mapper;

import com.example.mes.modules.plan.entity.PlanMps;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanMpsMapper {

    int insert(PlanMps planMps);

    int update(PlanMps planMps);

    PlanMps selectById(@Param("id") Long id);

    List<PlanMps> selectList(@Param("keyword") String keyword,
                             @Param("status") Integer status,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
