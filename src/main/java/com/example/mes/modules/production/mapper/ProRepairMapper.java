package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProRepair;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProRepairMapper {
    int insert(ProRepair entity);
    int update(ProRepair entity);
    int deleteById(@Param("id") Long id);
    ProRepair selectById(@Param("id") Long id);
    List<ProRepair> selectList(@Param("repairNo") String repairNo,
                               @Param("workOrderId") Long workOrderId,
                               @Param("status") Integer status,
                               @Param("repairType") Integer repairType,
                               @Param("offset") int offset,
                               @Param("limit") int limit);
    long countList(@Param("repairNo") String repairNo,
                   @Param("workOrderId") Long workOrderId,
                   @Param("status") Integer status,
                   @Param("repairType") Integer repairType);
}
