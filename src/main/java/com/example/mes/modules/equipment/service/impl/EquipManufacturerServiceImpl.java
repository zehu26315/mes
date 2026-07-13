package com.example.mes.modules.equipment.service.impl;

import com.example.mes.config.CacheNames;
import com.example.mes.modules.equipment.entity.EquipManufacturer;
import com.example.mes.modules.equipment.mapper.EquipManufacturerMapper;
import com.example.mes.modules.equipment.service.EquipManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipManufacturerServiceImpl implements EquipManufacturerService {
    private final EquipManufacturerMapper mapper;

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_MANUFACTURER, CacheNames.EQUIP_MANUFACTURER_ALL}, allEntries = true)
    public EquipManufacturer create(EquipManufacturer e) { if (e.getStatus() == null) e.setStatus(1); mapper.insert(e); return e; }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_MANUFACTURER, CacheNames.EQUIP_MANUFACTURER_ALL}, allEntries = true)
    public EquipManufacturer update(EquipManufacturer e) { e.setManufacturerCode(null); mapper.update(e); return mapper.selectById(e.getId()); }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_MANUFACTURER, CacheNames.EQUIP_MANUFACTURER_ALL}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override @Cacheable(value = CacheNames.EQUIP_MANUFACTURER, key = "#id", unless = "#result == null")
    public EquipManufacturer getById(Long id) { return mapper.selectById(id); }

    @Override @Cacheable(value = CacheNames.EQUIP_MANUFACTURER_ALL, unless = "#result == null || #result.isEmpty()")
    public List<EquipManufacturer> listAll() { return mapper.selectAll(); }
}
