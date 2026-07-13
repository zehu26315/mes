package com.example.mes.modules.material.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatSupplement;
import com.example.mes.modules.material.mapper.MatSupplementMapper;
import com.example.mes.modules.material.service.MatSupplementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatSupplementServiceImpl implements MatSupplementService {

    private final MatSupplementMapper matSupplementMapper;

    @Override
    @Transactional
    public MatSupplement create(MatSupplement supplement) {
        matSupplementMapper.insert(supplement);
        return supplement;
    }

    @Override
    public MatSupplement getById(Long id) {
        return matSupplementMapper.selectById(id);
    }

    @Override
    public PageResult<MatSupplement> list(String status, Long workOrderId, String materialCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MatSupplement> rows = matSupplementMapper.selectList(status, workOrderId, materialCode, offset, pageSize);
        long total = matSupplementMapper.countList(status, workOrderId, materialCode);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void approve(Long id) {
        matSupplementMapper.approve(id);
    }
}
