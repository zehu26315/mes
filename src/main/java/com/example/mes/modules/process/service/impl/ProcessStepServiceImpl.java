package com.example.mes.modules.process.service.impl;

import com.example.mes.config.CacheNames;
import com.example.mes.modules.process.entity.ProcessStep;
import com.example.mes.modules.process.mapper.ProcessStepMapper;
import com.example.mes.modules.process.service.ProcessStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessStepServiceImpl implements ProcessStepService {

    private final ProcessStepMapper processStepMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PROCESS_ROUTE, CacheNames.PROCESS_STEP}, allEntries = true)
    public ProcessStep createStep(Long routeId, ProcessStep step) {
        step.setRouteId(routeId);
        processStepMapper.insert(step);
        return step;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PROCESS_ROUTE, CacheNames.PROCESS_STEP}, allEntries = true)
    public ProcessStep updateStep(ProcessStep step) {
        step.setStepCode(null);
        processStepMapper.update(step);
        return processStepMapper.selectById(step.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PROCESS_ROUTE, CacheNames.PROCESS_STEP}, allEntries = true)
    public void deleteStep(Long id) {
        processStepMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PROCESS_STEP, key = "#id", unless = "#result == null")
    public ProcessStep getStepById(Long id) {
        return processStepMapper.selectById(id);
    }

    @Override
    public List<ProcessStep> listStepsByRouteId(Long routeId) {
        return processStepMapper.selectByRouteId(routeId);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.PROCESS_ROUTE, CacheNames.PROCESS_STEP}, allEntries = true)
    public ProcessStep updateSortOrder(Long id, Integer sortOrder) {
        processStepMapper.updateSortOrder(id, sortOrder);
        return processStepMapper.selectById(id);
    }
}
