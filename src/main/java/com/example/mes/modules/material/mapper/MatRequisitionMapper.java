package com.example.mes.modules.material.mapper;

import com.example.mes.modules.material.entity.MatRequisition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatRequisitionMapper {

    int insert(MatRequisition requisition);

    int update(MatRequisition requisition);

    MatRequisition selectById(@Param("id") Long id);

    List<MatRequisition> selectList(@Param("status") String status,
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
