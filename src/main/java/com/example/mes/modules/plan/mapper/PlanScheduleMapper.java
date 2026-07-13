package com.example.mes.modules.plan.mapper;

import com.example.mes.modules.plan.entity.PlanSchedule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanScheduleMapper {

    int insert(PlanSchedule planSchedule);

    int update(PlanSchedule planSchedule);

    PlanSchedule selectById(@Param("id") Long id);

    List<PlanSchedule> selectList(@Param("offset") int offset,
                                  @Param("limit") int limit);

    long countList();

    int deleteById(@Param("id") Long id);
}
