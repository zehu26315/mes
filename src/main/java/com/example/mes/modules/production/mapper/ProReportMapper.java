package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProReportMapper {
    int insert(ProReport entity);
    int update(ProReport entity);
    ProReport selectById(@Param("id") Long id);
    List<ProReport> selectList(@Param("keyword") String keyword,
                                @Param("orderId") Long orderId,
                                @Param("reportDate") String reportDate,
                                @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("keyword") String keyword,
                   @Param("orderId") Long orderId,
                   @Param("reportDate") String reportDate);
    int deleteById(@Param("id") Long id);
}
