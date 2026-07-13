package com.example.mes.modules.material.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatReturn;
import com.example.mes.modules.material.mapper.MatReturnMapper;
import com.example.mes.modules.material.service.MatReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatReturnServiceImpl implements MatReturnService {

    private final MatReturnMapper matReturnMapper;

    @Override
    @Transactional
    public MatReturn create(MatReturn matReturn) {
        matReturnMapper.insert(matReturn);
        return matReturn;
    }

    @Override
    public MatReturn getById(Long id) {
        return matReturnMapper.selectById(id);
    }

    @Override
    public PageResult<MatReturn> list(String status, Long workOrderId, String materialCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MatReturn> rows = matReturnMapper.selectList(status, workOrderId, materialCode, offset, pageSize);
        long total = matReturnMapper.countList(status, workOrderId, materialCode);
        return PageResult.of(total, page, pageSize, rows);
    }
}
