package com.example.mes.modules.plan.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.plan.entity.PlanSchedule;

public interface PlanScheduleService {

    PlanSchedule create(PlanSchedule planSchedule);

    PlanSchedule update(PlanSchedule planSchedule);

    void delete(Long id);

    PlanSchedule getById(Long id);

    PageResult<PlanSchedule> list(int page, int pageSize);
}
