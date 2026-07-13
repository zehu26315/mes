package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityStandardDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityStandardDetailMapper {

    int insert(QualityStandardDetail detail);

    int update(QualityStandardDetail detail);

    QualityStandardDetail selectById(@Param("id") Long id);

    List<QualityStandardDetail> selectByStandardId(@Param("standardId") Long standardId);

    int deleteById(@Param("id") Long id);

    int deleteByStandardId(@Param("standardId") Long standardId);

    int batchInsert(@Param("list") List<QualityStandardDetail> list);
}
