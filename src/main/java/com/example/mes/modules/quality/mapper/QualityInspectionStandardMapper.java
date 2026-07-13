package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityInspectionStandard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityInspectionStandardMapper {

    int insert(QualityInspectionStandard standard);

    int update(QualityInspectionStandard standard);

    QualityInspectionStandard selectById(@Param("id") Long id);

    List<QualityInspectionStandard> selectList(@Param("keyword") String keyword,
                                                @Param("status") Integer status,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
