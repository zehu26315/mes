package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProWorkOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProWorkOrderMapper {
    int insert(ProWorkOrder entity);
    int update(ProWorkOrder entity);
    ProWorkOrder selectById(@Param("id") Long id);
    List<ProWorkOrder> selectList(@Param("workOrderNo") String workOrderNo, @Param("materialId") Long materialId,
                                   @Param("status") Integer status, @Param("sourceType") Integer sourceType,
                                   @Param("priority") Integer priority,
                                   @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("workOrderNo") String workOrderNo, @Param("materialId") Long materialId,
                   @Param("status") Integer status, @Param("sourceType") Integer sourceType,
                   @Param("priority") Integer priority);
    int deleteById(@Param("id") Long id);
}
