package com.example.mes.modules.equipment.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.equipment.entity.EquipRepair;
import com.example.mes.modules.equipment.mapper.EquipRepairMapper;
import com.example.mes.modules.equipment.service.EquipRepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipRepairServiceImpl implements EquipRepairService {
    private final EquipRepairMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.EQUIP_REPAIR_LIST, allEntries = true)
    public EquipRepair create(EquipRepair e) {
        e.setStatus(0); // 0=待维修
        e.setCreateTime(LocalDateTime.now());
        e.setUpdateTime(LocalDateTime.now());
        mapper.insert(e);
        return e;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.EQUIP_REPAIR, CacheNames.EQUIP_REPAIR_LIST}, allEntries = true)
    public EquipRepair update(EquipRepair e) {
        e.setUpdateTime(LocalDateTime.now());
        mapper.update(e);
        return mapper.selectById(e.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.EQUIP_REPAIR, CacheNames.EQUIP_REPAIR_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override
    @Cacheable(value = CacheNames.EQUIP_REPAIR, key = "#id", unless = "#result == null")
    public EquipRepair getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<EquipRepair> list(String keyword, Long equipmentId, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<EquipRepair> rows = mapper.selectList(keyword, equipmentId, status, offset, pageSize);
        long total = mapper.countList(keyword, equipmentId, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.EQUIP_REPAIR, CacheNames.EQUIP_REPAIR_LIST}, allEntries = true)
    public EquipRepair complete(Long id) {
        EquipRepair repair = mapper.selectById(id);
        if (repair == null) throw new IllegalArgumentException("维修记录不存在: " + id);
        LocalDateTime now = LocalDateTime.now();
        mapper.complete(id, now, 2, "已修复"); // status=2=已修复
        return mapper.selectById(id);
    }
}
