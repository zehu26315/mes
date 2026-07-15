package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityInspection;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QualityInspectionMapper {
    int insert(QualityInspection entity);
    int update(QualityInspection entity);
    QualityInspection selectById(@Param("id") Long id);
    List<QualityInspection> selectList(@Param("keyword") String keyword,
                                        @Param("inspectionType") String inspectionType,
                                        @Param("result") Integer result,
                                        @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("keyword") String keyword,
                   @Param("inspectionType") String inspectionType,
                   @Param("result") Integer result);
    int deleteById(@Param("id") Long id);
    List<Map<String, Object>> statsByResult();
}
