package com.example.mes.modules.plan.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.plan.entity.PlanMps;

public interface PlanMpsService {

    PlanMps create(PlanMps planMps);

    PlanMps update(PlanMps planMps);

    void delete(Long id);

    PlanMps getById(Long id);

    PageResult<PlanMps> list(String keyword, Integer status, int page, int pageSize);
}
