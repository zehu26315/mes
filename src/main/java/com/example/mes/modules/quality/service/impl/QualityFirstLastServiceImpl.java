package com.example.mes.modules.quality.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityFirstLast;
import com.example.mes.modules.quality.mapper.QualityFirstLastMapper;
import com.example.mes.modules.quality.service.QualityFirstLastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityFirstLastServiceImpl implements QualityFirstLastService {

    private final QualityFirstLastMapper firstLastMapper;

    @Override
    @Transactional
    public QualityFirstLast createRecord(QualityFirstLast record) {
        firstLastMapper.insert(record);
        return record;
    }

    @Override
    @Transactional
    public QualityFirstLast updateRecord(QualityFirstLast record) {
        firstLastMapper.update(record);
        return firstLastMapper.selectById(record.getId());
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        firstLastMapper.deleteById(id);
    }

    @Override
    public QualityFirstLast getRecordById(Long id) {
        return firstLastMapper.selectById(id);
    }

    @Override
    public PageResult<QualityFirstLast> listRecords(String keyword, Long workOrderId, String result, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<QualityFirstLast> rows = firstLastMapper.selectList(keyword, workOrderId, result, offset, pageSize);
        long total = firstLastMapper.countList(keyword, workOrderId, result);
        return PageResult.of(total, page, pageSize, rows);
    }
}
