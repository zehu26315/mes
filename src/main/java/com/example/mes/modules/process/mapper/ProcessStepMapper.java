package com.example.mes.modules.process.mapper;

import com.example.mes.modules.process.entity.ProcessStep;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessStepMapper {

    int insert(ProcessStep step);

    int update(ProcessStep step);

    ProcessStep selectById(@Param("id") Long id);

    List<ProcessStep> selectByRouteId(@Param("routeId") Long routeId);

    int deleteById(@Param("id") Long id);

    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);
}
