package com.example.mes.modules.quality.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspectionOrder;
import com.example.mes.modules.quality.mapper.QualityInspectionOrderMapper;
import com.example.mes.modules.quality.service.QualityInspectionOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityInspectionOrderServiceImpl implements QualityInspectionOrderService {

    private final QualityInspectionOrderMapper orderMapper;

    @Override
    @Transactional
    public QualityInspectionOrder createOrder(QualityInspectionOrder order) {
        orderMapper.insert(order);
        return order;
    }

    @Override
    @Transactional
    public QualityInspectionOrder updateOrder(QualityInspectionOrder order) {
        orderMapper.update(order);
        return orderMapper.selectById(order.getId());
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderMapper.deleteById(id);
    }

    @Override
    public QualityInspectionOrder getOrderById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public PageResult<QualityInspectionOrder> listOrders(String keyword, String orderType, String result, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<QualityInspectionOrder> rows = orderMapper.selectList(keyword, orderType, result, offset, pageSize);
        long total = orderMapper.countList(keyword, orderType, result);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void updateOrderResult(Long id, String result, Integer defectQty) {
        QualityInspectionOrder order = orderMapper.selectById(id);
        if (order != null) {
            order.setResult(result);
            order.setDefectQty(defectQty);
            orderMapper.update(order);
        }
    }
}
