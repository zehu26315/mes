package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.production.entity.ProRepair;
import com.example.mes.modules.production.mapper.ProRepairMapper;
import com.example.mes.modules.production.service.ProRepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProRepairServiceImpl implements ProRepairService {

    private final ProRepairMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PROD_REPAIR_LIST, allEntries = true)
    public ProRepair create(ProRepair entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(1);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_REPAIR, CacheNames.PROD_REPAIR_LIST}, allEntries = true)
    public ProRepair update(ProRepair entity) {
        entity.setRepairNo(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_REPAIR, CacheNames.PROD_REPAIR_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PROD_REPAIR, key = "#id", unless = "#result == null")
    public ProRepair getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<ProRepair> list(String repairNo, Long workOrderId,
                                       Integer status, Integer repairType, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProRepair> rows = mapper.selectList(repairNo, workOrderId, status, repairType, offset, pageSize);
        long total = mapper.countList(repairNo, workOrderId, status, repairType);
        return PageResult.of(total, page, pageSize, rows);
    }
}
