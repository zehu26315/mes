package com.example.mes.modules.quality.mapper;

import com.example.mes.modules.quality.entity.QualityPatrol;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QualityPatrolMapper {

    int insert(QualityPatrol patrol);

    int update(QualityPatrol patrol);

    QualityPatrol selectById(@Param("id") Long id);

    List<QualityPatrol> selectList(@Param("keyword") String keyword,
                                    @Param("workOrderId") Long workOrderId,
                                    @Param("result") String result,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("workOrderId") Long workOrderId,
                   @Param("result") String result);

    int deleteById(@Param("id") Long id);
}
