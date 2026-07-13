package com.example.mes.modules.quality.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityDefectRecord;

public interface QualityDefectRecordService {

    QualityDefectRecord createRecord(QualityDefectRecord record);

    QualityDefectRecord updateRecord(QualityDefectRecord record);

    void deleteRecord(Long id);

    QualityDefectRecord getRecordById(Long id);

    PageResult<QualityDefectRecord> listRecords(String keyword, Long workOrderId, String defectType, int page, int pageSize);
}
