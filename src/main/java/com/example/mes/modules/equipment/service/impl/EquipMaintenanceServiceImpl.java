package com.example.mes.modules.equipment.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.equipment.entity.EquipMaintenance;
import com.example.mes.modules.equipment.mapper.EquipMaintenanceMapper;
import com.example.mes.modules.equipment.service.EquipMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipMaintenanceServiceImpl implements EquipMaintenanceService {

    private final EquipMaintenanceMapper mapper;

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.EQUIP_MAINTENANCE_LIST, allEntries = true)
    public EquipMaintenance create(EquipMaintenance e) {
        e.setCreateTime(LocalDateTime.now());
        e.setUpdateTime(LocalDateTime.now());
        if (e.getStatus() == null) {
            e.setStatus(0);
        }
        mapper.insert(e);
        return e;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.EQUIP_MAINTENANCE, CacheNames.EQUIP_MAINTENANCE_LIST}, allEntries = true)
    public EquipMaintenance update(EquipMaintenance e) {
        e.setUpdateTime(LocalDateTime.now());
        mapper.update(e);
        return mapper.selectById(e.getId());
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.EQUIP_MAINTENANCE, CacheNames.EQUIP_MAINTENANCE_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.EQUIP_MAINTENANCE, key = "#id", unless = "#result == null")
    public EquipMaintenance getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<EquipMaintenance> list(String keyword, Long equipmentId, String maintenanceType, Integer status,
                                               int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<EquipMaintenance> rows = mapper.selectList(keyword, equipmentId, maintenanceType, status, offset, pageSize);
        long total = mapper.countList(keyword, equipmentId, maintenanceType, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
