package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProReport;

public interface ProReportService {
    ProReport create(ProReport entity);
    ProReport update(ProReport entity);
    void delete(Long id);
    ProReport getById(Long id);
    PageResult<ProReport> list(String keyword, Long orderId, String reportDate, int page, int pageSize);
}
