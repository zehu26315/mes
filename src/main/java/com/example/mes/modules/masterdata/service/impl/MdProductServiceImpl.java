package com.example.mes.modules.masterdata.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.masterdata.entity.MdProduct;
import com.example.mes.modules.masterdata.mapper.MdProductMapper;
import com.example.mes.modules.masterdata.service.MdProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MdProductServiceImpl implements MdProductService {

    private final MdProductMapper mdProductMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.MD_PRODUCT, CacheNames.MD_PRODUCT_LIST}, allEntries = true)
    public MdProduct createProduct(MdProduct product) {
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        mdProductMapper.insert(product);
        return product;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.MD_PRODUCT, CacheNames.MD_PRODUCT_LIST}, allEntries = true)
    public MdProduct updateProduct(MdProduct product) {
        product.setProductCode(null);
        mdProductMapper.update(product);
        return mdProductMapper.selectById(product.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.MD_PRODUCT, CacheNames.MD_PRODUCT_LIST}, allEntries = true)
    public void deleteProduct(Long id) {
        mdProductMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.MD_PRODUCT, key = "#id", unless = "#result == null")
    public MdProduct getProductById(Long id) {
        return mdProductMapper.selectById(id);
    }

    @Override
    public PageResult<MdProduct> listProducts(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<MdProduct> rows = mdProductMapper.selectList(keyword, status, offset, pageSize);
        long total = mdProductMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
