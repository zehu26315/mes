package com.example.mes.modules.trace.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.trace.entity.TraceProduct;
import com.example.mes.modules.trace.mapper.TraceProductMapper;
import com.example.mes.modules.trace.service.TraceProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TraceProductServiceImpl implements TraceProductService {

    private final TraceProductMapper traceProductMapper;

    @Override
    @Transactional
    public TraceProduct createProduct(TraceProduct product) {
        traceProductMapper.insert(product);
        return product;
    }

    @Override
    public TraceProduct getProductById(Long id) {
        return traceProductMapper.selectById(id);
    }

    @Override
    public TraceProduct getProductByBarcodeNo(String barcodeNo) {
        return traceProductMapper.selectByBarcodeNo(barcodeNo);
    }

    @Override
    public PageResult<TraceProduct> listProducts(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<TraceProduct> rows = traceProductMapper.selectList(offset, pageSize);
        long total = traceProductMapper.countList();
        return PageResult.of(total, page, pageSize, rows);
    }
}
