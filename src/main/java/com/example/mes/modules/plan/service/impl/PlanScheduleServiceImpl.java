package com.example.mes.modules.plan.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.plan.entity.PlanSchedule;
import com.example.mes.modules.plan.mapper.PlanScheduleMapper;
import com.example.mes.modules.plan.service.PlanScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanScheduleServiceImpl implements PlanScheduleService {

    private final PlanScheduleMapper planScheduleMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.PLAN_SCHEDULE_LIST, allEntries = true)
    public PlanSchedule create(PlanSchedule planSchedule) {
        if (planSchedule.getStatus() == null) {
            planSchedule.setStatus(1);
        }
        planScheduleMapper.insert(planSchedule);
        return planSchedule;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PLAN_SCHEDULE, CacheNames.PLAN_SCHEDULE_LIST}, allEntries = true)
    public PlanSchedule update(PlanSchedule planSchedule) {
        planSchedule.setScheduleNo(null);
        planScheduleMapper.update(planSchedule);
        return planScheduleMapper.selectById(planSchedule.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PLAN_SCHEDULE, CacheNames.PLAN_SCHEDULE_LIST}, allEntries = true)
    public void delete(Long id) {
        planScheduleMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PLAN_SCHEDULE, key = "#id", unless = "#result == null")
    public PlanSchedule getById(Long id) {
        return planScheduleMapper.selectById(id);
    }

    @Override
    public PageResult<PlanSchedule> list(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PlanSchedule> rows = planScheduleMapper.selectList(offset, pageSize);
        long total = planScheduleMapper.countList();
        return PageResult.of(total, page, pageSize, rows);
    }
}
