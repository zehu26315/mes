package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdReporting;

public interface ProdReportingService {

    ProdReporting create(ProdReporting reporting);

    PageResult<ProdReporting> list(Long workOrderId, String processCode, int page, int pageSize);

    PageResult<ProdReporting> listCompletions(int page, int pageSize);
}
