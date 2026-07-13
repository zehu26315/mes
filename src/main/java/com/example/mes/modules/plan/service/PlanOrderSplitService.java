package com.example.mes.modules.plan.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.plan.entity.PlanOrderSplit;

public interface PlanOrderSplitService {

    PlanOrderSplit create(PlanOrderSplit planOrderSplit);

    PlanOrderSplit update(PlanOrderSplit planOrderSplit);

    void delete(Long id);

    PlanOrderSplit getById(Long id);

    PageResult<PlanOrderSplit> list(int page, int pageSize);
}
