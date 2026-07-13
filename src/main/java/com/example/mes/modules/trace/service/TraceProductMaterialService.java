package com.example.mes.modules.trace.service;

import com.example.mes.modules.trace.entity.TraceProductMaterial;

import java.util.List;

public interface TraceProductMaterialService {

    TraceProductMaterial createProductMaterial(TraceProductMaterial productMaterial);

    List<TraceProductMaterial> getProductMaterialsByBarcodeNo(String barcodeNo);

    List<TraceProductMaterial> getProductMaterialsByMaterialBatchNo(String materialBatchNo);
}
