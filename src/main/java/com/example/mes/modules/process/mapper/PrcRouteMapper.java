package com.example.mes.modules.process.mapper;

import com.example.mes.modules.process.entity.PrcRoute;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrcRouteMapper {
    int insert(PrcRoute entity);
    int update(PrcRoute entity);
    PrcRoute selectById(@Param("id") Long id);
    List<PrcRoute> selectList(@Param("routeCode") String routeCode, @Param("routeName") String routeName,
                               @Param("keyword") String keyword,
                               @Param("materialId") Long materialId, @Param("status") Integer status,
                               @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("routeCode") String routeCode, @Param("routeName") String routeName,
                   @Param("keyword") String keyword,
                   @Param("materialId") Long materialId, @Param("status") Integer status);
    int deleteById(@Param("id") Long id);
}
