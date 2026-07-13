package com.example.mes.modules.trace.mapper;

import com.example.mes.modules.trace.entity.TraceProductMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TraceProductMaterialMapper {

    int insert(TraceProductMaterial productMaterial);

    List<TraceProductMaterial> selectByBarcodeNo(@Param("barcodeNo") String barcodeNo);

    List<TraceProductMaterial> selectByMaterialBatchNo(@Param("materialBatchNo") String materialBatchNo);
}
