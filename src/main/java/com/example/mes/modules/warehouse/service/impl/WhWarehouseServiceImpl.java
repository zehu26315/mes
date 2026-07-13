package com.example.mes.modules.warehouse.service.impl;

import com.example.mes.config.CacheNames;
import com.example.mes.modules.warehouse.entity.WhWarehouse;
import com.example.mes.modules.warehouse.mapper.WhWarehouseMapper;
import com.example.mes.modules.warehouse.service.WhWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhWarehouseServiceImpl implements WhWarehouseService {
    private final WhWarehouseMapper mapper;

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.WH_WAREHOUSE, CacheNames.WH_WAREHOUSE_ALL}, allEntries = true)
    public WhWarehouse create(WhWarehouse entity) {
        if (entity.getStatus() == null) entity.setStatus(1);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.WH_WAREHOUSE, CacheNames.WH_WAREHOUSE_ALL}, allEntries = true)
    public WhWarehouse update(WhWarehouse entity) {
        entity.setWhCode(null);
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.WH_WAREHOUSE, CacheNames.WH_WAREHOUSE_ALL}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override @Cacheable(value = CacheNames.WH_WAREHOUSE, key = "#id", unless = "#result == null")
    public WhWarehouse getById(Long id) { return mapper.selectById(id); }

    @Override @Cacheable(value = CacheNames.WH_WAREHOUSE_ALL, unless = "#result == null || #result.isEmpty()")
    public List<WhWarehouse> listAll() { return mapper.selectAll(); }
}
