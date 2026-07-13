package com.example.mes.modules.equipment.service.impl;

import com.example.mes.common.response.TreeNode;
import com.example.mes.common.util.TreeUtils;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.equipment.entity.EquipCategory;
import com.example.mes.modules.equipment.mapper.EquipCategoryMapper;
import com.example.mes.modules.equipment.service.EquipCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipCategoryServiceImpl implements EquipCategoryService {
    private final EquipCategoryMapper mapper;

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_CATEGORY, CacheNames.EQUIP_CATEGORY_TREE}, allEntries = true)
    public EquipCategory create(EquipCategory e) { mapper.insert(e); return e; }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_CATEGORY, CacheNames.EQUIP_CATEGORY_TREE}, allEntries = true)
    public EquipCategory update(EquipCategory e) { e.setCategoryCode(null); mapper.update(e); return mapper.selectById(e.getId()); }

    @Override @Transactional
    @CacheEvict(cacheNames = {CacheNames.EQUIP_CATEGORY, CacheNames.EQUIP_CATEGORY_TREE}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override @Cacheable(value = CacheNames.EQUIP_CATEGORY, key = "#id", unless = "#result == null")
    public EquipCategory getById(Long id) { return mapper.selectById(id); }

    @Override @Cacheable(value = CacheNames.EQUIP_CATEGORY_TREE, unless = "#result == null || #result.isEmpty()")
    public List<TreeNode<EquipCategory>> getTree() {
        return TreeUtils.buildTree(mapper.selectAll(), 0L, EquipCategory::getId,
                c -> c.getParentId() == null ? 0L : c.getParentId());
    }
}
