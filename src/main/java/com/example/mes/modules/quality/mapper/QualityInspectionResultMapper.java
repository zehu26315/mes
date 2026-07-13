package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityInspectionResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityInspectionResultMapper {

    int insert(QualityInspectionResult result);

    int update(QualityInspectionResult result);

    QualityInspectionResult selectById(@Param("id") Long id);

    List<QualityInspectionResult> selectByOrderId(@Param("orderId") Long orderId);

    int deleteById(@Param("id") Long id);

    int deleteByOrderId(@Param("orderId") Long orderId);

    int batchInsert(@Param("list") List<QualityInspectionResult> list);
}
