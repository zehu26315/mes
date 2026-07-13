package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdWorkOrder;
import com.example.mes.modules.production.mapper.ProdWorkOrderMapper;
import com.example.mes.modules.production.service.ProdWorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdWorkOrderServiceImpl implements ProdWorkOrderService {

    private final ProdWorkOrderMapper prodWorkOrderMapper;

    @Override
    @Transactional
    public ProdWorkOrder create(ProdWorkOrder workOrder) {
        prodWorkOrderMapper.insert(workOrder);
        return workOrder;
    }

    @Override
    @Transactional
    public ProdWorkOrder update(ProdWorkOrder workOrder) {
        prodWorkOrderMapper.update(workOrder);
        return prodWorkOrderMapper.selectById(workOrder.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        prodWorkOrderMapper.deleteById(id);
    }

    @Override
    public ProdWorkOrder getById(Long id) {
        return prodWorkOrderMapper.selectById(id);
    }

    @Override
    public ProdWorkOrder getByOrderNo(String orderNo) {
        return prodWorkOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult<ProdWorkOrder> list(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProdWorkOrder> rows = prodWorkOrderMapper.selectList(keyword, status, offset, pageSize);
        long total = prodWorkOrderMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        prodWorkOrderMapper.updateStatus(id, status);
    }
}
