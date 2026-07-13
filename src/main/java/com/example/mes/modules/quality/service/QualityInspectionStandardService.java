package com.example.mes.modules.quality.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspectionStandard;

public interface QualityInspectionStandardService {

    QualityInspectionStandard createStandard(QualityInspectionStandard standard);

    QualityInspectionStandard updateStandard(QualityInspectionStandard standard);

    void deleteStandard(Long id);

    QualityInspectionStandard getStandardById(Long id);

    PageResult<QualityInspectionStandard> listStandards(String keyword, Integer status, int page, int pageSize);
}
