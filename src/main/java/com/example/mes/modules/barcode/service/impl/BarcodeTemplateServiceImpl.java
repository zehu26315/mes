package com.example.mes.modules.barcode.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeTemplate;
import com.example.mes.modules.barcode.mapper.BarcodeTemplateMapper;
import com.example.mes.modules.barcode.service.BarcodeTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarcodeTemplateServiceImpl implements BarcodeTemplateService {

    private final BarcodeTemplateMapper barcodeTemplateMapper;

    @Override
    @Transactional
    public BarcodeTemplate create(BarcodeTemplate barcodeTemplate) {
        if (barcodeTemplate.getStatus() == null) {
            barcodeTemplate.setStatus(1);
        }
        barcodeTemplateMapper.insert(barcodeTemplate);
        return barcodeTemplate;
    }

    @Override
    @Transactional
    public BarcodeTemplate update(BarcodeTemplate barcodeTemplate) {
        barcodeTemplateMapper.update(barcodeTemplate);
        return barcodeTemplateMapper.selectById(barcodeTemplate.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        barcodeTemplateMapper.deleteById(id);
    }

    @Override
    public BarcodeTemplate getById(Long id) {
        return barcodeTemplateMapper.selectById(id);
    }

    @Override
    public PageResult<BarcodeTemplate> list(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<BarcodeTemplate> rows = barcodeTemplateMapper.selectList(keyword, status, offset, pageSize);
        long total = barcodeTemplateMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
