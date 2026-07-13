package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityDefectRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityDefectRecordMapper {

    int insert(QualityDefectRecord record);

    int update(QualityDefectRecord record);

    QualityDefectRecord selectById(@Param("id") Long id);

    List<QualityDefectRecord> selectList(@Param("keyword") String keyword,
                                          @Param("workOrderId") Long workOrderId,
                                          @Param("defectType") String defectType,
                                          @Param("offset") int offset,
                                          @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("workOrderId") Long workOrderId,
                   @Param("defectType") String defectType);

    int deleteById(@Param("id") Long id);
}
