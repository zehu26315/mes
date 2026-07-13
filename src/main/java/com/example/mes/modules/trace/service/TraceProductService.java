package com.example.mes.modules.trace.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.trace.entity.TraceProduct;

public interface TraceProductService {

    TraceProduct createProduct(TraceProduct product);

    TraceProduct getProductById(Long id);

    TraceProduct getProductByBarcodeNo(String barcodeNo);

    PageResult<TraceProduct> listProducts(int page, int pageSize);
}
