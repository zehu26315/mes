package com.example.mes.modules.masterdata.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.masterdata.entity.MdMaterialCategory;

public interface MdMaterialCategoryService {

    MdMaterialCategory createCategory(MdMaterialCategory category);

    MdMaterialCategory updateCategory(MdMaterialCategory category);

    void deleteCategory(Long id);

    MdMaterialCategory getCategoryById(Long id);

    PageResult<MdMaterialCategory> listCategories(String keyword, Integer status, int page, int pageSize);
}
