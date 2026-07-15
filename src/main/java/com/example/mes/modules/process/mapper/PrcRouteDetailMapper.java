package com.example.mes.modules.process.mapper;

import com.example.mes.modules.process.entity.PrcRouteDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrcRouteDetailMapper {
    int insert(PrcRouteDetail entity);
    int update(PrcRouteDetail entity);
    PrcRouteDetail selectById(@Param("id") Long id);
    List<PrcRouteDetail> selectByRouteId(@Param("routeId") Long routeId);
    int deleteById(@Param("id") Long id);
    List<PrcRouteDetail> selectList(@Param("routeId") Long routeId,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);
    long countList(@Param("routeId") Long routeId);
}
