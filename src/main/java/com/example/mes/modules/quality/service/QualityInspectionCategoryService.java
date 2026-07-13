package com.example.mes.modules.quality.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspectionCategory;

public interface QualityInspectionCategoryService {

    QualityInspectionCategory createCategory(QualityInspectionCategory category);

    QualityInspectionCategory updateCategory(QualityInspectionCategory category);

    void deleteCategory(Long id);

    QualityInspectionCategory getCategoryById(Long id);

    PageResult<QualityInspectionCategory> listCategories(String keyword, int page, int pageSize);
}
