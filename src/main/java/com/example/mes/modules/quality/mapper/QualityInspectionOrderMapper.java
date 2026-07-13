package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityInspectionOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityInspectionOrderMapper {

    int insert(QualityInspectionOrder order);

    int update(QualityInspectionOrder order);

    QualityInspectionOrder selectById(@Param("id") Long id);

    List<QualityInspectionOrder> selectList(@Param("keyword") String keyword,
                                             @Param("orderType") String orderType,
                                             @Param("result") String result,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("orderType") String orderType,
                   @Param("result") String result);

    int deleteById(@Param("id") Long id);
}
