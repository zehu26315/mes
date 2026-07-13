package com.example.mes.modules.material.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatRequisition;
import com.example.mes.modules.material.mapper.MatRequisitionMapper;
import com.example.mes.modules.material.service.MatRequisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatRequisitionServiceImpl implements MatRequisitionService {

    private final MatRequisitionMapper matRequisitionMapper;

    @Override
    @Transactional
    public MatRequisition create(MatRequisition requisition) {
        matRequisitionMapper.insert(requisition);
        return requisition;
    }

    @Override
    public MatRequisition getById(Long id) {
        return matRequisitionMapper.selectById(id);
    }

    @Override
    public PageResult<MatRequisition> list(String status, Long workOrderId, String materialCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MatRequisition> rows = matRequisitionMapper.selectList(status, workOrderId, materialCode, offset, pageSize);
        long total = matRequisitionMapper.countList(status, workOrderId, materialCode);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void approve(Long id) {
        matRequisitionMapper.approve(id);
    }
}
