package com.example.mes.modules.plan.mapper;

import com.example.mes.modules.plan.entity.PlanDaily;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface PlanDailyMapper {

    int insert(PlanDaily planDaily);

    int update(PlanDaily planDaily);

    PlanDaily selectById(@Param("id") Long id);

    List<PlanDaily> selectList(@Param("keyword") String keyword,
                               @Param("status") Integer status,
                               @Param("planDate") LocalDate planDate,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status,
                   @Param("planDate") LocalDate planDate);

    int deleteById(@Param("id") Long id);
}
