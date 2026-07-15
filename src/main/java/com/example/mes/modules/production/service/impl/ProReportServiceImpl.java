package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.production.entity.ProReport;
import com.example.mes.modules.production.mapper.ProReportMapper;
import com.example.mes.modules.production.service.ProReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProReportServiceImpl implements ProReportService {

    private final ProReportMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PROD_REPORT_LIST, allEntries = true)
    public ProReport create(ProReport entity) {
        // Auto-generate reportNo: WR + yyyyMMddHHmmssSSS
        if (entity.getReportNo() == null || entity.getReportNo().isEmpty()) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            entity.setReportNo("WR" + timestamp);
        }
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        if (entity.getReportTime() == null) entity.setReportTime(now);
        if (entity.getReportType() == null) entity.setReportType(1);
        if (entity.getReportQuantity() == null) entity.setReportQuantity(java.math.BigDecimal.ZERO);
        if (entity.getQualifiedQuantity() == null) entity.setQualifiedQuantity(java.math.BigDecimal.ZERO);
        if (entity.getDefectQuantity() == null) entity.setDefectQuantity(java.math.BigDecimal.ZERO);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_REPORT, CacheNames.PROD_REPORT_LIST}, allEntries = true)
    public ProReport update(ProReport entity) {
        entity.setReportNo(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_REPORT, CacheNames.PROD_REPORT_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PROD_REPORT, key = "#id", unless = "#result == null")
    public ProReport getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<ProReport> list(String keyword, Long orderId, String reportDate, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProReport> rows = mapper.selectList(keyword, orderId, reportDate, offset, pageSize);
        long total = mapper.countList(keyword, orderId, reportDate);
        return PageResult.of(total, page, pageSize, rows);
    }
}
