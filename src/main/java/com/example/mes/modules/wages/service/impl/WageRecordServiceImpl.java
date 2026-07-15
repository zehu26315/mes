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

    @Override
    @Transactional
    public WageRecord updateRecord(WageRecord record) {
        WageRecord existing = wageRecordMapper.selectById(record.getId());
        if (existing == null) {
            throw new IllegalArgumentException("工资记录不存在");
        }

        // 不允许通过 update 接口修改 code 类字段（对齐 SysUserServiceImpl.updateUser）
        record.setEmployeeCode(null);
        record.setProcessCode(null);

        // 基于「合并后」的数量/单价重算总额：部分更新时用原值兜底，
        // 避免缺失字段被当作 0 从而把 total_amount 覆盖为错误值
        BigDecimal quantity = record.getQuantity() != null ? record.getQuantity() : existing.getQuantity();
        BigDecimal unitPrice = record.getUnitPrice() != null ? record.getUnitPrice() : existing.getUnitPrice();
        if (quantity != null && unitPrice != null) {
            record.setTotalAmount(quantity.multiply(unitPrice));
        } else {
            // 不足以计算时置空，动态 <set> 会跳过该列，保留原值
            record.setTotalAmount(null);
        }

        wageRecordMapper.update(record);
        return wageRecordMapper.selectById(record.getId());
    }
}
