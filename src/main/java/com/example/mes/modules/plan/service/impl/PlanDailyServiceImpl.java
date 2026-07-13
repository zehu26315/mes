package com.example.mes.modules.plan.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.plan.entity.PlanDaily;
import com.example.mes.modules.plan.mapper.PlanDailyMapper;
import com.example.mes.modules.plan.service.PlanDailyService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanDailyServiceImpl implements PlanDailyService {

    private final PlanDailyMapper planDailyMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.PLAN_DAILY_LIST, allEntries = true)
    public PlanDaily create(PlanDaily planDaily) {
        if (planDaily.getStatus() == null) {
            planDaily.setStatus(1);
        }
        planDailyMapper.insert(planDaily);
        return planDaily;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PLAN_DAILY, CacheNames.PLAN_DAILY_LIST}, allEntries = true)
    public PlanDaily update(PlanDaily planDaily) {
        planDaily.setPlanNo(null);
        planDailyMapper.update(planDaily);
        return planDailyMapper.selectById(planDaily.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PLAN_DAILY, CacheNames.PLAN_DAILY_LIST}, allEntries = true)
    public void delete(Long id) {
        planDailyMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PLAN_DAILY, key = "#id", unless = "#result == null")
    public PlanDaily getById(Long id) {
        return planDailyMapper.selectById(id);
    }

    @Override
    public PageResult<PlanDaily> list(String keyword, Integer status, LocalDate planDate, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PlanDaily> rows = planDailyMapper.selectList(keyword, status, planDate, offset, pageSize);
        long total = planDailyMapper.countList(keyword, status, planDate);
        return PageResult.of(total, page, pageSize, rows);
    }
}
