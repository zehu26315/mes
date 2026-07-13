package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityInspectionCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityInspectionCategoryMapper {

    int insert(QualityInspectionCategory category);

    int update(QualityInspectionCategory category);

    QualityInspectionCategory selectById(@Param("id") Long id);

    List<QualityInspectionCategory> selectList(@Param("keyword") String keyword,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);

    long countList(@Param("keyword") String keyword);

    int deleteById(@Param("id") Long id);
}
