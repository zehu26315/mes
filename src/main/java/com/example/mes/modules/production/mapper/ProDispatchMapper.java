package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProDispatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProDispatchMapper {
    int insert(ProDispatch entity);
    int update(ProDispatch entity);
    ProDispatch selectById(@Param("id") Long id);
    List<ProDispatch> selectList(@Param("dispatchNo") String dispatchNo, @Param("workOrderId") Long workOrderId,
                                  @Param("productionLine") String productionLine,
                                  @Param("status") Integer status, @Param("assigneeId") Long assigneeId,
                                  @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("dispatchNo") String dispatchNo, @Param("workOrderId") Long workOrderId,
                   @Param("productionLine") String productionLine,
                   @Param("status") Integer status, @Param("assigneeId") Long assigneeId);
    int deleteById(@Param("id") Long id);
}
