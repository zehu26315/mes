package com.example.mes.modules.quality.service;

import com.example.mes.modules.quality.entity.QualityInspectionResult;

import java.util.List;

public interface QualityInspectionResultService {

    QualityInspectionResult createResult(QualityInspectionResult result);

    QualityInspectionResult updateResult(QualityInspectionResult result);

    void deleteResult(Long id);

    QualityInspectionResult getResultById(Long id);

    List<QualityInspectionResult> getResultsByOrderId(Long orderId);

    void batchSaveResults(Long orderId, List<QualityInspectionResult> results);
}
