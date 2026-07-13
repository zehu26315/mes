package com.example.mes.modules.trace.mapper;

import com.example.mes.modules.trace.entity.TraceProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TraceProductMapper {

    int insert(TraceProduct product);

    TraceProduct selectById(@Param("id") Long id);

    TraceProduct selectByBarcodeNo(@Param("barcodeNo") String barcodeNo);

    List<TraceProduct> selectList(@Param("offset") int offset,
                                   @Param("limit") int limit);

    long countList();
}
