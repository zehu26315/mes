package com.example.mes.modules.masterdata.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.masterdata.entity.MdProduct;

public interface MdProductService {

    MdProduct createProduct(MdProduct product);

    MdProduct updateProduct(MdProduct product);

    void deleteProduct(Long id);

    MdProduct getProductById(Long id);

    PageResult<MdProduct> listProducts(String keyword, Integer status, int page, int pageSize);
}
