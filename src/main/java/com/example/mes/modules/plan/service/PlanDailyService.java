package com.example.mes.modules.plan.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.plan.entity.PlanDaily;

import java.time.LocalDate;

public interface PlanDailyService {

    PlanDaily create(PlanDaily planDaily);

    PlanDaily update(PlanDaily planDaily);

    void delete(Long id);

    PlanDaily getById(Long id);

    PageResult<PlanDaily> list(String keyword, Integer status, LocalDate planDate, int page, int pageSize);
}
