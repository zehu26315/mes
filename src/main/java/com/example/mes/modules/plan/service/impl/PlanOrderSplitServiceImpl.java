package com.example.mes.modules.plan.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.plan.entity.PlanOrderSplit;
import com.example.mes.modules.plan.mapper.PlanOrderSplitMapper;
import com.example.mes.modules.plan.service.PlanOrderSplitService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanOrderSplitServiceImpl implements PlanOrderSplitService {

    private final PlanOrderSplitMapper planOrderSplitMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.PLAN_ORDER_SPLIT_LIST, allEntries = true)
    public PlanOrderSplit create(PlanOrderSplit planOrderSplit) {
        planOrderSplitMapper.insert(planOrderSplit);
        return planOrderSplit;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PLAN_ORDER_SPLIT, CacheNames.PLAN_ORDER_SPLIT_LIST}, allEntries = true)
    public PlanOrderSplit update(PlanOrderSplit planOrderSplit) {
        planOrderSplit.setSourceOrderNo(null);
        planOrderSplitMapper.update(planOrderSplit);
        return planOrderSplitMapper.selectById(planOrderSplit.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PLAN_ORDER_SPLIT, CacheNames.PLAN_ORDER_SPLIT_LIST}, allEntries = true)
    public void delete(Long id) {
        planOrderSplitMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PLAN_ORDER_SPLIT, key = "#id", unless = "#result == null")
    public PlanOrderSplit getById(Long id) {
        return planOrderSplitMapper.selectById(id);
    }

    @Override
    public PageResult<PlanOrderSplit> list(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PlanOrderSplit> rows = planOrderSplitMapper.selectList(offset, pageSize);
        long total = planOrderSplitMapper.countList();
        return PageResult.of(total, page, pageSize, rows);
    }
}
