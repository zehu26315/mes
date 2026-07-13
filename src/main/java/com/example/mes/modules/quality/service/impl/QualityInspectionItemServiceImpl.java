package com.example.mes.modules.quality.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspectionItem;
import com.example.mes.modules.quality.mapper.QualityInspectionItemMapper;
import com.example.mes.modules.quality.service.QualityInspectionItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityInspectionItemServiceImpl implements QualityInspectionItemService {

    private final QualityInspectionItemMapper itemMapper;

    @Override
    @Transactional
    public QualityInspectionItem createItem(QualityInspectionItem item) {
        itemMapper.insert(item);
        return item;
    }

    @Override
    @Transactional
    public QualityInspectionItem updateItem(QualityInspectionItem item) {
        itemMapper.update(item);
        return itemMapper.selectById(item.getId());
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        itemMapper.deleteById(id);
    }

    @Override
    public QualityInspectionItem getItemById(Long id) {
        return itemMapper.selectById(id);
    }

    @Override
    public PageResult<QualityInspectionItem> listItems(String keyword, Long categoryId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<QualityInspectionItem> rows = itemMapper.selectList(keyword, categoryId, offset, pageSize);
        long total = itemMapper.countList(keyword, categoryId);
        return PageResult.of(total, page, pageSize, rows);
    }
}
