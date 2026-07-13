package com.example.mes.modules.equipment.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.equipment.entity.EquipAsset;
import com.example.mes.modules.equipment.entity.EquipStatusLog;
import com.example.mes.modules.equipment.mapper.EquipAssetMapper;
import com.example.mes.modules.equipment.mapper.EquipStatusLogMapper;
import com.example.mes.modules.equipment.service.EquipAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipAssetServiceImpl implements EquipAssetService {
    private final EquipAssetMapper assetMapper;
    private final EquipStatusLogMapper statusLogMapper;

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_ASSET, CacheNames.EQUIP_ASSET_LIST}, allEntries = true)
    public EquipAsset create(EquipAsset e) { assetMapper.insert(e); return e; }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_ASSET, CacheNames.EQUIP_ASSET_LIST}, allEntries = true)
    public EquipAsset update(EquipAsset e) { e.setEquipCode(null); assetMapper.update(e); return assetMapper.selectById(e.getId()); }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_ASSET, CacheNames.EQUIP_ASSET_LIST}, allEntries = true)
    public void delete(Long id) { assetMapper.deleteById(id); }

    @Override @Cacheable(value = CacheNames.EQUIP_ASSET, key = "#id", unless = "#result == null")
    public EquipAsset getById(Long id) { return assetMapper.selectById(id); }

    @Override
    public PageResult<EquipAsset> list(String keyword, String status, Long categoryId, String productionLine, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<EquipAsset> rows = assetMapper.selectList(keyword, status, categoryId, productionLine, offset, pageSize);
        long total = assetMapper.countList(keyword, status, categoryId, productionLine);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_ASSET, CacheNames.EQUIP_ASSET_LIST}, allEntries = true)
    public EquipAsset updateStatus(Long id, String status, String reason, String operator) {
        EquipAsset asset = assetMapper.selectById(id);
        if (asset == null) throw new IllegalArgumentException("Equipment not found: " + id);
        String fromStatus = asset.getStatus();
        assetMapper.updateStatus(id, status);
        EquipStatusLog log = EquipStatusLog.builder().equipId(id).fromStatus(fromStatus)
                .toStatus(status).reason(reason).operator(operator).build();
        statusLogMapper.insert(log);
        return assetMapper.selectById(id);
    }
}
