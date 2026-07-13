package com.example.mes.modules.barcode.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeRecord;
import com.example.mes.modules.barcode.mapper.BarcodeRecordMapper;
import com.example.mes.modules.barcode.service.BarcodeRecordService;
import lombok.RequiredArgsConstructor;
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
    public BarcodeRecord create(BarcodeRecord barcodeRecord) {
        if (barcodeRecord.getBarcodeNo() == null || barcodeRecord.getBarcodeNo().isBlank()) {
            barcodeRecord.setBarcodeNo(UUID.randomUUID().toString().replace("-", ""));
        }
        if (barcodeRecord.getStatus() == null) {
            barcodeRecord.setStatus(1);
        }
        if (barcodeRecord.getPrintCount() == null) {
            barcodeRecord.setPrintCount(0);
        }
        barcodeRecordMapper.insert(barcodeRecord);
        return barcodeRecord;
    }

    @Override
    public BarcodeRecord getById(Long id) {
        return barcodeRecordMapper.selectById(id);
    }

    @Override
    public PageResult<BarcodeRecord> list(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<BarcodeRecord> rows = barcodeRecordMapper.selectList(keyword, status, offset, pageSize);
        long total = barcodeRecordMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        barcodeRecordMapper.deleteById(id);
    }
}
