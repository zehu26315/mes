package com.example.mes.modules.trace.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.trace.entity.ProductTrace;

public interface ProductTraceService {
    ProductTrace create(ProductTrace entity);
    ProductTrace update(ProductTrace entity);
    void delete(Long id);
    ProductTrace getById(Long id);
    PageResult<ProductTrace> list(String keyword, int page, int pageSize);
}
