package com.example.mes.modules.plan.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.plan.entity.PlanMps;
import com.example.mes.modules.plan.mapper.PlanMpsMapper;
import com.example.mes.modules.plan.service.PlanMpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanMpsServiceImpl implements PlanMpsService {

    private final PlanMpsMapper planMpsMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.PLAN_MPS_LIST, allEntries = true)
    public PlanMps create(PlanMps planMps) {
        if (planMps.getStatus() == null) {
            planMps.setStatus(1);
        }
        planMpsMapper.insert(planMps);
        return planMps;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PLAN_MPS, CacheNames.PLAN_MPS_LIST}, allEntries = true)
    public PlanMps update(PlanMps planMps) {
        planMps.setPlanNo(null);
        planMpsMapper.update(planMps);
        return planMpsMapper.selectById(planMps.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PLAN_MPS, CacheNames.PLAN_MPS_LIST}, allEntries = true)
    public void delete(Long id) {
        planMpsMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PLAN_MPS, key = "#id", unless = "#result == null")
    public PlanMps getById(Long id) {
        return planMpsMapper.selectById(id);
    }

    @Override
    public PageResult<PlanMps> list(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PlanMps> rows = planMpsMapper.selectList(keyword, status, offset, pageSize);
        long total = planMpsMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
