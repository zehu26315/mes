package com.example.mes.modules.equipment.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.equipment.entity.EquipInspection;
import com.example.mes.modules.equipment.mapper.EquipInspectionMapper;
import com.example.mes.modules.equipment.service.EquipInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipInspectionServiceImpl implements EquipInspectionService {

    private final EquipInspectionMapper mapper;

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.EQUIP_INSPECTION_LIST, allEntries = true)
    public EquipInspection create(EquipInspection e) {
        e.setCreateTime(LocalDateTime.now());
        e.setUpdateTime(LocalDateTime.now());
        mapper.insert(e);
        return e;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.EQUIP_INSPECTION, CacheNames.EQUIP_INSPECTION_LIST}, allEntries = true)
    public EquipInspection update(EquipInspection e) {
        e.setUpdateTime(LocalDateTime.now());
        mapper.update(e);
        return mapper.selectById(e.getId());
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.EQUIP_INSPECTION, CacheNames.EQUIP_INSPECTION_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.EQUIP_INSPECTION, key = "#id", unless = "#result == null")
    public EquipInspection getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<EquipInspection> list(String keyword, Long equipmentId, Integer result, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<EquipInspection> rows = mapper.selectList(keyword, equipmentId, result, offset, pageSize);
        long total = mapper.countList(keyword, equipmentId, result);
        return PageResult.of(total, page, pageSize, rows);
    }
}
