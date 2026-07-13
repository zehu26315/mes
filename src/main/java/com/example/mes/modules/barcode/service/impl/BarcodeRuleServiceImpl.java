package com.example.mes.modules.barcode.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeRule;
import com.example.mes.modules.barcode.mapper.BarcodeRuleMapper;
import com.example.mes.modules.barcode.service.BarcodeRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarcodeRuleServiceImpl implements BarcodeRuleService {

    private final BarcodeRuleMapper barcodeRuleMapper;

    @Override
    @Transactional
    public BarcodeRule create(BarcodeRule barcodeRule) {
        if (barcodeRule.getStatus() == null) {
            barcodeRule.setStatus(1);
        }
        barcodeRuleMapper.insert(barcodeRule);
        return barcodeRule;
    }

    @Override
    @Transactional
    public BarcodeRule update(BarcodeRule barcodeRule) {
        barcodeRuleMapper.update(barcodeRule);
        return barcodeRuleMapper.selectById(barcodeRule.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        barcodeRuleMapper.deleteById(id);
    }

    @Override
    public BarcodeRule getById(Long id) {
        return barcodeRuleMapper.selectById(id);
    }

    @Override
    public PageResult<BarcodeRule> list(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<BarcodeRule> rows = barcodeRuleMapper.selectList(keyword, status, offset, pageSize);
        long total = barcodeRuleMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
