package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProductionOrder;

import java.util.Map;

public interface ProductionOrderService {
    ProductionOrder create(ProductionOrder entity);
    ProductionOrder update(ProductionOrder entity);
    void delete(Long id);
    ProductionOrder getById(Long id);
    PageResult<ProductionOrder> list(String keyword, Integer status, int page, int pageSize);
    Map<String, Object> dashboardStats();
}
