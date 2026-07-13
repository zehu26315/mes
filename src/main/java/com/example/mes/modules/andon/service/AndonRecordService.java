package com.example.mes.modules.andon.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.andon.entity.AndonRecord;

import java.util.List;

public interface AndonRecordService {

    AndonRecord create(AndonRecord andonRecord);

    AndonRecord getById(Long id);

    PageResult<AndonRecord> list(String keyword, Long andonTypeId, String status, String workCenter,
                                 String startTime, String endTime, int page, int pageSize);

    AndonRecord updateStatus(Long id, String status, String handler, String resolveDesc);

    List<AndonRecord> getActive();

    void delete(Long id);
}
