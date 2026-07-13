package com.example.mes.modules.wages.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.wages.entity.WageRecord;

public interface WageRecordService {

    WageRecord createRecord(WageRecord record);

    WageRecord getRecordById(Long id);

    PageResult<WageRecord> listRecords(String keyword, int page, int pageSize);

    void deleteRecord(Long id);
}
