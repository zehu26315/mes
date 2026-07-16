package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.production.entity.ProductionOrder;
import com.example.mes.modules.production.mapper.ProductionOrderMapper;
import com.example.mes.modules.production.service.ProductionOrderService;
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
public class ProductionOrderServiceImpl implements ProductionOrderService {

    private final ProductionOrderMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PROD_ORDER_LIST, allEntries = true)
    public ProductionOrder create(ProductionOrder entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(0);
        if (entity.getPlannedQty() == null) entity.setPlannedQty(0);
        if (entity.getCompletedQty() == null) entity.setCompletedQty(0);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_ORDER, CacheNames.PROD_ORDER_LIST}, allEntries = true)
    public ProductionOrder update(ProductionOrder entity) {
        entity.setOrderNo(null);
        entity.setCreateBy(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PROD_ORDER, CacheNames.PROD_ORDER_LIST}, allEntries = true)
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.PROD_ORDER, key = "#id", unless = "#result == null")
    public ProductionOrder getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public PageResult<ProductionOrder> list(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProductionOrder> rows = mapper.selectList(keyword, status, offset, pageSize);
        long total = mapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public Map<String, Object> dashboardStats() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> statusStats = mapper.dashboardStatusStats();
        List<Map<String, Object>> stats = new java.util.ArrayList<>();
        for (Map<String, Object> row : statusStats) {
            Object s = row.get("status");
            Number count = (Number) row.get("count");
            int c = count != null ? count.intValue() : 0;
            Map<String, Object> statItem = new java.util.HashMap<>();
            statItem.put("status", s);
            statItem.put("cnt", c);
            stats.add(statItem);
        }
        result.put("stats", stats);

        // frontend expects dailyOutput with [{day, output}] — day as "YYYY-MM-DD" string
        List<Map<String, Object>> dailyOutput = mapper.dashboardDailyOutput();
        for (Map<String, Object> item : dailyOutput) {
            Object dateVal = item.get("date");
            if (dateVal != null) {
                item.put("day", dateVal.toString());
            }
            item.remove("date");
            // ensure output is a number
            Object outVal = item.get("output");
            if (outVal instanceof Number n) {
                item.put("output", n.intValue());
            }
        }
        result.put("dailyOutput", dailyOutput);

        // frontend expects lineOutput with [{line, output}]
        List<Map<String, Object>> lineOutput = mapper.dashboardLineOutput();
        for (Map<String, Object> item : lineOutput) {
            Object nameVal = item.remove("name");
            if (nameVal != null) item.put("line", nameVal.toString());
            Object val = item.remove("value");
            if (val instanceof Number n) {
                item.put("output", n.intValue());
            } else if (val != null) {
                item.put("output", Integer.parseInt(val.toString()));
            }
        }
        result.put("lineOutput", lineOutput);

        return result;
    }
}
