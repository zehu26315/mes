package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdWip;
import com.example.mes.modules.production.mapper.ProdWipMapper;
import com.example.mes.modules.production.service.ProdWipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdWipServiceImpl implements ProdWipService {

    private final ProdWipMapper prodWipMapper;

    @Override
    public PageResult<ProdWip> list(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProdWip> rows = prodWipMapper.selectList(offset, pageSize);
        long total = prodWipMapper.countList();
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public List<ProdWip> getByProcessCode(String processCode) {
        return prodWipMapper.selectByProcessCode(processCode);
    }
}
