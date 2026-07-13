package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityInspectionItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityInspectionItemMapper {

    int insert(QualityInspectionItem item);

    int update(QualityInspectionItem item);

    QualityInspectionItem selectById(@Param("id") Long id);

    List<QualityInspectionItem> selectList(@Param("keyword") String keyword,
                                            @Param("categoryId") Long categoryId,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("categoryId") Long categoryId);

    int deleteById(@Param("id") Long id);
}
