package com.example.mes.modules.wages.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.wages.entity.WageRecord;
import com.example.mes.modules.wages.mapper.WageRecordMapper;
import com.example.mes.modules.wages.service.WageRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WageRecordServiceImpl implements WageRecordService {

    private final WageRecordMapper wageRecordMapper;

    @Override
    @Transactional
    public WageRecord createRecord(WageRecord record) {
        BigDecimal quantity = record.getQuantity() != null ? record.getQuantity() : BigDecimal.ZERO;
        BigDecimal unitPrice = record.getUnitPrice() != null ? record.getUnitPrice() : BigDecimal.ZERO;
        record.setTotalAmount(quantity.multiply(unitPrice));
        wageRecordMapper.insert(record);
        return record;
    }

    @Override
    public WageRecord getRecordById(Long id) {
        return wageRecordMapper.selectById(id);
    }

    @Override
    public PageResult<WageRecord> listRecords(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<WageRecord> rows = wageRecordMapper.selectList(keyword, offset, pageSize);
        long total = wageRecordMapper.countList(keyword);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        wageRecordMapper.deleteById(id);
    }
}
