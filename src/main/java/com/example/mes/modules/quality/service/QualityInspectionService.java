package com.example.mes.modules.quality.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspection;

import java.util.Map;

public interface QualityInspectionService {
    QualityInspection create(QualityInspection entity);
    QualityInspection update(QualityInspection entity);
    void delete(Long id);
    QualityInspection getById(Long id);
    PageResult<QualityInspection> list(String keyword, String inspectionType, Integer result, int page, int pageSize);
    Map<String, Object> stats();
}
