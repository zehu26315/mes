package com.example.mes.modules.quality.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityPatrol;

public interface QualityPatrolService {

    QualityPatrol createPatrol(QualityPatrol patrol);

    QualityPatrol updatePatrol(QualityPatrol patrol);

    void deletePatrol(Long id);

    QualityPatrol getPatrolById(Long id);

    PageResult<QualityPatrol> listPatrols(String keyword, Long workOrderId, String result, int page, int pageSize);
}
