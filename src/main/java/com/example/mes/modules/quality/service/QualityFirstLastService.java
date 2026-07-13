package com.example.mes.modules.quality.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityFirstLast;

public interface QualityFirstLastService {

    QualityFirstLast createRecord(QualityFirstLast record);

    QualityFirstLast updateRecord(QualityFirstLast record);

    void deleteRecord(Long id);

    QualityFirstLast getRecordById(Long id);

    PageResult<QualityFirstLast> listRecords(String keyword, Long workOrderId, String result, int page, int pageSize);
}
