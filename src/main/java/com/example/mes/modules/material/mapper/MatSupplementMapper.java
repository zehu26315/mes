package com.example.mes.modules.material.mapper;

import com.example.mes.modules.material.entity.MatSupplement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatSupplementMapper {

    int insert(MatSupplement supplement);

    int update(MatSupplement supplement);

    MatSupplement selectById(@Param("id") Long id);

    List<MatSupplement> selectList(@Param("status") String status,
                                   @Param("workOrderId") Long workOrderId,
                                   @Param("materialCode") String materialCode,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    long countList(@Param("status") String status,
                   @Param("workOrderId") Long workOrderId,
                   @Param("materialCode") String materialCode);

    int deleteById(@Param("id") Long id);

    int approve(@Param("id") Long id);
}
