package com.example.mes.modules.quality.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspectionOrder;

public interface QualityInspectionOrderService {

    QualityInspectionOrder createOrder(QualityInspectionOrder order);

    QualityInspectionOrder updateOrder(QualityInspectionOrder order);

    void deleteOrder(Long id);

    QualityInspectionOrder getOrderById(Long id);

    PageResult<QualityInspectionOrder> listOrders(String keyword, String orderType, String result, int page, int pageSize);

    void updateOrderResult(Long id, String result, Integer defectQty);
}
