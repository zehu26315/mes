package com.example.mes.modules.barcode.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeType;
import com.example.mes.modules.barcode.mapper.BarcodeTypeMapper;
import com.example.mes.modules.barcode.service.BarcodeTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarcodeTypeServiceImpl implements BarcodeTypeService {

    private final BarcodeTypeMapper barcodeTypeMapper;

    @Override
    @Transactional
    public BarcodeType create(BarcodeType barcodeType) {
        if (barcodeType.getStatus() == null) {
            barcodeType.setStatus(1);
        }
        barcodeTypeMapper.insert(barcodeType);
        return barcodeType;
    }

    @Override
    @Transactional
    public BarcodeType update(BarcodeType barcodeType) {
        barcodeTypeMapper.update(barcodeType);
        return barcodeTypeMapper.selectById(barcodeType.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        barcodeTypeMapper.deleteById(id);
    }

    @Override
    public BarcodeType getById(Long id) {
        return barcodeTypeMapper.selectById(id);
    }

    @Override
    public PageResult<BarcodeType> list(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<BarcodeType> rows = barcodeTypeMapper.selectList(keyword, status, offset, pageSize);
        long total = barcodeTypeMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
