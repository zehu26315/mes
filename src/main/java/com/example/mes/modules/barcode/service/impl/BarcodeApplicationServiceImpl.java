package com.example.mes.modules.barcode.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeApplication;
import com.example.mes.modules.barcode.mapper.BarcodeApplicationMapper;
import com.example.mes.modules.barcode.service.BarcodeApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarcodeApplicationServiceImpl implements BarcodeApplicationService {

    private final BarcodeApplicationMapper barcodeApplicationMapper;

    @Override
    @Transactional
    public BarcodeApplication create(BarcodeApplication barcodeApplication) {
        if (barcodeApplication.getStatus() == null) {
            barcodeApplication.setStatus(1);
        }
        barcodeApplicationMapper.insert(barcodeApplication);
        return barcodeApplication;
    }

    @Override
    @Transactional
    public BarcodeApplication update(BarcodeApplication barcodeApplication) {
        barcodeApplicationMapper.update(barcodeApplication);
        return barcodeApplicationMapper.selectById(barcodeApplication.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        barcodeApplicationMapper.deleteById(id);
    }

    @Override
    public BarcodeApplication getById(Long id) {
        return barcodeApplicationMapper.selectById(id);
    }

    @Override
    public PageResult<BarcodeApplication> list(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<BarcodeApplication> rows = barcodeApplicationMapper.selectList(keyword, status, offset, pageSize);
        long total = barcodeApplicationMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
