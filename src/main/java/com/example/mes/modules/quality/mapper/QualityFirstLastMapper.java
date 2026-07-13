package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityFirstLast;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityFirstLastMapper {

    int insert(QualityFirstLast record);

    int update(QualityFirstLast record);

    QualityFirstLast selectById(@Param("id") Long id);

    List<QualityFirstLast> selectList(@Param("keyword") String keyword,
                                       @Param("workOrderId") Long workOrderId,
                                       @Param("result") String result,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("workOrderId") Long workOrderId,
                   @Param("result") String result);

    int deleteById(@Param("id") Long id);
}
