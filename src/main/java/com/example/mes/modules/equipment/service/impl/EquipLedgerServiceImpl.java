package com.example.mes.modules.equipment.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.equipment.entity.EquipLedger;
import com.example.mes.modules.equipment.mapper.EquipLedgerMapper;
import com.example.mes.modules.equipment.service.EquipLedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipLedgerServiceImpl implements EquipLedgerService {

    private final EquipLedgerMapper mapper;

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.EQUIP_LEDGER_LIST, allEntries = true)
    public EquipLedger create(EquipLedger entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) {
            entity.setStatus(0);
        }
        mapper.insert(entity);
        return entity;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.EQUIP_LEDGER, CacheNames.EQUIP_LEDGER_LIST}, allEntries = true)
    public EquipLedger update(EquipLedger entity) {
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.EQUIP_LEDGER, CacheNames.EQUIP_LEDGER_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.EQUIP_LEDGER, key = "#id", unless = "#result == null")
    public EquipLedger getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<EquipLedger> list(String equipmentCode, String equipmentName, String category,
                                         Integer status, String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<EquipLedger> rows = mapper.selectList(equipmentCode, equipmentName, category, status, keyword, offset, pageSize);
        long total = mapper.countList(equipmentCode, equipmentName, category, status, keyword);
        return PageResult.of(total, page, pageSize, rows);
    }
}
