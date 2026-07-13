package com.example.mes.modules.material.mapper;

import com.example.mes.modules.material.entity.MatReturn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatReturnMapper {

    int insert(MatReturn matReturn);

    int update(MatReturn matReturn);

    MatReturn selectById(@Param("id") Long id);

    List<MatReturn> selectList(@Param("status") String status,
                               @Param("workOrderId") Long workOrderId,
                               @Param("materialCode") String materialCode,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    long countList(@Param("status") String status,
                   @Param("workOrderId") Long workOrderId,
                   @Param("materialCode") String materialCode);

    int deleteById(@Param("id") Long id);
}
