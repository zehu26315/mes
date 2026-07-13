package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdRepairOrder;
import com.example.mes.modules.production.mapper.ProdRepairOrderMapper;
import com.example.mes.modules.production.service.ProdRepairOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdRepairOrderServiceImpl implements ProdRepairOrderService {

    private final ProdRepairOrderMapper prodRepairOrderMapper;

    @Override
    @Transactional
    public ProdRepairOrder create(ProdRepairOrder repairOrder) {
        prodRepairOrderMapper.insert(repairOrder);
        return repairOrder;
    }

    @Override
    public ProdRepairOrder getById(Long id) {
        return prodRepairOrderMapper.selectById(id);
    }

    @Override
    public PageResult<ProdRepairOrder> list(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProdRepairOrder> rows = prodRepairOrderMapper.selectList(offset, pageSize);
        long total = prodRepairOrderMapper.countList();
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void complete(Long id, String result) {
        prodRepairOrderMapper.complete(id, result);
    }
}
