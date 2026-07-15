package com.example.mes.modules.quality.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.quality.entity.QualityInspection;
import com.example.mes.modules.quality.mapper.QualityInspectionMapper;
import com.example.mes.modules.quality.service.QualityInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QualityInspectionServiceImpl implements QualityInspectionService {

    private final QualityInspectionMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.QUALITY_INSPECTION_LIST, allEntries = true)
    public QualityInspection create(QualityInspection entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getResult() == null) entity.setResult(0);
        if (entity.getSampleQty() == null) entity.setSampleQty(0);
        if (entity.getDefectQty() == null) entity.setDefectQty(0);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.QUALITY_INSPECTION, CacheNames.QUALITY_INSPECTION_LIST}, allEntries = true)
    public QualityInspection update(QualityInspection entity) {
        entity.setReportNo(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.QUALITY_INSPECTION, CacheNames.QUALITY_INSPECTION_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override
    @Cacheable(value = CacheNames.QUALITY_INSPECTION, key = "#id", unless = "#result == null")
    public QualityInspection getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<QualityInspection> list(String keyword, String inspectionType, Integer result, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<QualityInspection> rows = mapper.selectList(keyword, inspectionType, result, offset, pageSize);
        long total = mapper.countList(keyword, inspectionType, result);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public Map<String, Object> stats() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> byResult = mapper.statsByResult();
        int total = 0, pass = 0, fail = 0, sampleQty = 0;
        for (Map<String, Object> row : byResult) {
            Object r = row.get("result");
            Number c = (Number) row.get("count");
            Number qty = (Number) row.get("total_qty");
            int count = c != null ? c.intValue() : 0;
            total += count;
            sampleQty += (qty != null ? qty.intValue() : 0);
            if (r instanceof Number n && n.intValue() == 0) pass = count;
            else fail += count;
        }
        double passRate = total > 0 ? (double) pass / total : 0.0;
        result.put("totalInspections", total);
        result.put("passRate", passRate);
        result.put("defectCount", fail);
        result.put("totalSampleQty", sampleQty);
        return result;
    }
}
