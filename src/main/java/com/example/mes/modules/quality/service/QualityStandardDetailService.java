package com.example.mes.modules.quality.service;

import com.example.mes.modules.quality.entity.QualityStandardDetail;

import java.util.List;

public interface QualityStandardDetailService {

    QualityStandardDetail createDetail(QualityStandardDetail detail);

    QualityStandardDetail updateDetail(QualityStandardDetail detail);

    void deleteDetail(Long id);

    QualityStandardDetail getDetailById(Long id);

    List<QualityStandardDetail> getDetailsByStandardId(Long standardId);

    void batchSaveDetails(Long standardId, List<QualityStandardDetail> details);
}
