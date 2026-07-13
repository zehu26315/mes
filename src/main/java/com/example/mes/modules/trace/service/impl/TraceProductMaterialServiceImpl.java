package com.example.mes.modules.trace.service.impl;

import com.example.mes.modules.trace.entity.TraceProductMaterial;
import com.example.mes.modules.trace.mapper.TraceProductMaterialMapper;
import com.example.mes.modules.trace.service.TraceProductMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TraceProductMaterialServiceImpl implements TraceProductMaterialService {

    private final TraceProductMaterialMapper traceProductMaterialMapper;

    @Override
    @Transactional
    public TraceProductMaterial createProductMaterial(TraceProductMaterial productMaterial) {
        traceProductMaterialMapper.insert(productMaterial);
        return productMaterial;
    }

    @Override
    public List<TraceProductMaterial> getProductMaterialsByBarcodeNo(String barcodeNo) {
        return traceProductMaterialMapper.selectByBarcodeNo(barcodeNo);
    }

    @Override
    public List<TraceProductMaterial> getProductMaterialsByMaterialBatchNo(String materialBatchNo) {
        return traceProductMaterialMapper.selectByMaterialBatchNo(materialBatchNo);
    }
}
