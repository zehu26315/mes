package com.example.mes.modules.quality.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspectionItem;

public interface QualityInspectionItemService {

    QualityInspectionItem createItem(QualityInspectionItem item);

    QualityInspectionItem updateItem(QualityInspectionItem item);

    void deleteItem(Long id);

    QualityInspectionItem getItemById(Long id);

    PageResult<QualityInspectionItem> listItems(String keyword, Long categoryId, int page, int pageSize);
}
