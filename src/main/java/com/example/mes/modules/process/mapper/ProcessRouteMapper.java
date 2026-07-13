package com.example.mes.modules.process.mapper;

import com.example.mes.modules.process.entity.ProcessRoute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessRouteMapper {

    int insert(ProcessRoute route);

    int update(ProcessRoute route);

    ProcessRoute selectById(@Param("id") Long id);

    List<ProcessRoute> selectList(@Param("keyword") String keyword,
                                  @Param("status") Integer status,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
