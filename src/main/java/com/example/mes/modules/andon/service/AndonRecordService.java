package com.example.mes.modules.andon.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.andon.entity.AndonRecord;

import java.util.List;

public interface AndonRecordService {

    AndonRecord create(AndonRecord andonRecord);

    AndonRecord update(AndonRecord andonRecord);

    AndonRecord getById(Long id);

    PageResult<AndonRecord> list(String keyword, Long andonConfigId, Integer status, String productionLine,
                                 String startTime, String endTime, int page, int pageSize);

    List<AndonRecord> getActive();

    void delete(Long id);
}
