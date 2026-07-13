package com.example.mes.modules.quality.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityDefectRecord;
import com.example.mes.modules.quality.mapper.QualityDefectRecordMapper;
import com.example.mes.modules.quality.service.QualityDefectRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityDefectRecordServiceImpl implements QualityDefectRecordService {

    private final QualityDefectRecordMapper defectMapper;

    @Override
    @Transactional
    public QualityDefectRecord createRecord(QualityDefectRecord record) {
        defectMapper.insert(record);
        return record;
    }

    @Override
    @Transactional
    public QualityDefectRecord updateRecord(QualityDefectRecord record) {
        defectMapper.update(record);
        return defectMapper.selectById(record.getId());
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        defectMapper.deleteById(id);
    }

    @Override
    public QualityDefectRecord getRecordById(Long id) {
        return defectMapper.selectById(id);
    }

    @Override
    public PageResult<QualityDefectRecord> listRecords(String keyword, Long workOrderId, String defectType, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<QualityDefectRecord> rows = defectMapper.selectList(keyword, workOrderId, defectType, offset, pageSize);
        long total = defectMapper.countList(keyword, workOrderId, defectType);
        return PageResult.of(total, page, pageSize, rows);
    }
}
