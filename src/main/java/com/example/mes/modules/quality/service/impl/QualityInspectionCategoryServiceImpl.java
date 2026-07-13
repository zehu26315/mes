package com.example.mes.modules.quality.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspectionCategory;
import com.example.mes.modules.quality.mapper.QualityInspectionCategoryMapper;
import com.example.mes.modules.quality.service.QualityInspectionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityInspectionCategoryServiceImpl implements QualityInspectionCategoryService {

    private final QualityInspectionCategoryMapper categoryMapper;

    @Override
    @Transactional
    public QualityInspectionCategory createCategory(QualityInspectionCategory category) {
        categoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public QualityInspectionCategory updateCategory(QualityInspectionCategory category) {
        categoryMapper.update(category);
        return categoryMapper.selectById(category.getId());
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public QualityInspectionCategory getCategoryById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public PageResult<QualityInspectionCategory> listCategories(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<QualityInspectionCategory> rows = categoryMapper.selectList(keyword, offset, pageSize);
        long total = categoryMapper.countList(keyword);
        return PageResult.of(total, page, pageSize, rows);
    }
}
