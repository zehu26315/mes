package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdDispatchOrder;
import com.example.mes.modules.production.mapper.ProdDispatchOrderMapper;
import com.example.mes.modules.production.service.ProdDispatchOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdDispatchOrderServiceImpl implements ProdDispatchOrderService {

    private final ProdDispatchOrderMapper prodDispatchOrderMapper;

    @Override
    @Transactional
    public ProdDispatchOrder create(ProdDispatchOrder dispatchOrder) {
        prodDispatchOrderMapper.insert(dispatchOrder);
        return dispatchOrder;
    }

    @Override
    @Transactional
    public ProdDispatchOrder update(ProdDispatchOrder dispatchOrder) {
        prodDispatchOrderMapper.update(dispatchOrder);
        return prodDispatchOrderMapper.selectById(dispatchOrder.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        prodDispatchOrderMapper.deleteById(id);
    }

    @Override
    public ProdDispatchOrder getById(Long id) {
        return prodDispatchOrderMapper.selectById(id);
    }

    @Override
    public PageResult<ProdDispatchOrder> list(Long workOrderId, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProdDispatchOrder> rows = prodDispatchOrderMapper.selectList(workOrderId, status, offset, pageSize);
        long total = prodDispatchOrderMapper.countList(workOrderId, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
