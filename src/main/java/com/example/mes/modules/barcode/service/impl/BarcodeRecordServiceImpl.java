package com.example.mes.modules.barcode.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.barcode.entity.BarcodeRecord;
import com.example.mes.modules.barcode.mapper.BarcodeRecordMapper;
import com.example.mes.modules.barcode.service.BarcodeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BarcodeRecordServiceImpl implements BarcodeRecordService {

    private final BarcodeRecordMapper barcodeRecordMapper;

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.BARCODE_RECORD_LIST, allEntries = true)
    public BarcodeRecord create(BarcodeRecord barcodeRecord) {
        if (barcodeRecord.getBarcode() == null || barcodeRecord.getBarcode().isBlank()) {
            barcodeRecord.setBarcode(UUID.randomUUID().toString().replace("-", ""));
        }
        barcodeRecordMapper.insert(barcodeRecord);
        return barcodeRecord;
    }

    @Override
    @Cacheable(value = CacheNames.BARCODE_RECORD, key = "#id", unless = "#result == null")
    public BarcodeRecord getById(Long id) {
        return barcodeRecordMapper.selectById(id);
    }

    @Override
    public PageResult<BarcodeRecord> list(String keyword, String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<BarcodeRecord> rows = barcodeRecordMapper.selectList(keyword, status, offset, pageSize);
        long total = barcodeRecordMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.BARCODE_RECORD, CacheNames.BARCODE_RECORD_LIST}, allEntries = true)
    public void deleteById(Long id) {
        barcodeRecordMapper.deleteById(id);
    }
}
