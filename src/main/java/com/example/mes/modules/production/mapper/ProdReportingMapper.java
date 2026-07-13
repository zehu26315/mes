package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProdReporting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProdReportingMapper {

    int insert(ProdReporting reporting);

    List<ProdReporting> selectList(@Param("workOrderId") Long workOrderId,
                                   @Param("processCode") String processCode,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    long countList(@Param("workOrderId") Long workOrderId,
                   @Param("processCode") String processCode);

    List<ProdReporting> selectCompletions(@Param("offset") int offset,
                                          @Param("limit") int limit);

    long countCompletions();
}
