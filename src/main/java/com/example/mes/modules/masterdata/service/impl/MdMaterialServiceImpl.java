package com.example.mes.modules.masterdata.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.masterdata.entity.MdMaterial;
import com.example.mes.modules.masterdata.mapper.MdMaterialMapper;
import com.example.mes.modules.masterdata.service.MdMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MdMaterialServiceImpl implements MdMaterialService {

    private final MdMaterialMapper mdMaterialMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.MD_MATERIAL, CacheNames.MD_MATERIAL_LIST}, allEntries = true)
    public MdMaterial createMaterial(MdMaterial material) {
        if (material.getStatus() == null) {
            material.setStatus(1);
        }
        mdMaterialMapper.insert(material);
        return material;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.MD_MATERIAL, CacheNames.MD_MATERIAL_LIST}, allEntries = true)
    public MdMaterial updateMaterial(MdMaterial material) {
        material.setMaterialCode(null);
        mdMaterialMapper.update(material);
        return mdMaterialMapper.selectById(material.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.MD_MATERIAL, CacheNames.MD_MATERIAL_LIST}, allEntries = true)
    public void deleteMaterial(Long id) {
        mdMaterialMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.MD_MATERIAL, key = "#id", unless = "#result == null")
    public MdMaterial getMaterialById(Long id) {
        return mdMaterialMapper.selectById(id);
    }

    @Override
    public PageResult<MdMaterial> listMaterials(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MdMaterial> rows = mdMaterialMapper.selectList(keyword, status, offset, pageSize);
        long total = mdMaterialMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
