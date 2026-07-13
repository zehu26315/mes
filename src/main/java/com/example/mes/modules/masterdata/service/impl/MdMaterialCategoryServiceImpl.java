package com.example.mes.modules.masterdata.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.masterdata.entity.MdMaterialCategory;
import com.example.mes.modules.masterdata.mapper.MdMaterialCategoryMapper;
import com.example.mes.modules.masterdata.service.MdMaterialCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MdMaterialCategoryServiceImpl implements MdMaterialCategoryService {

    private final MdMaterialCategoryMapper mdMaterialCategoryMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.MD_MATERIAL_CATEGORY, CacheNames.MD_MATERIAL_CATEGORY_LIST}, allEntries = true)
    public MdMaterialCategory createCategory(MdMaterialCategory category) {
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        mdMaterialCategoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.MD_MATERIAL_CATEGORY, CacheNames.MD_MATERIAL_CATEGORY_LIST}, allEntries = true)
    public MdMaterialCategory updateCategory(MdMaterialCategory category) {
        category.setCategoryCode(null);
        mdMaterialCategoryMapper.update(category);
        return mdMaterialCategoryMapper.selectById(category.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.MD_MATERIAL_CATEGORY, CacheNames.MD_MATERIAL_CATEGORY_LIST}, allEntries = true)
    public void deleteCategory(Long id) {
        mdMaterialCategoryMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.MD_MATERIAL_CATEGORY, key = "#id", unless = "#result == null")
    public MdMaterialCategory getCategoryById(Long id) {
        return mdMaterialCategoryMapper.selectById(id);
    }

    @Override
    public PageResult<MdMaterialCategory> listCategories(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MdMaterialCategory> rows = mdMaterialCategoryMapper.selectList(keyword, status, offset, pageSize);
        long total = mdMaterialCategoryMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
