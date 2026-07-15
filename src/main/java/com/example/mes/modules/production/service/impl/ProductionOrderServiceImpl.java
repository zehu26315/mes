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
        int totalOrders = 0, inProgress = 0, completed = 0, pending = 0;
        for (Map<String, Object> row : statusStats) {
            Object s = row.get("status");
            Number count = (Number) row.get("count");
            int c = count != null ? count.intValue() : 0;
            totalOrders += c;
            if (s instanceof Number n && n.intValue() == 1) inProgress = c;
            else if (s instanceof Number n && n.intValue() == 2) completed = c;
            else pending += c;
            // frontend expects [{status, cnt}]
            Map<String, Object> statItem = new java.util.HashMap<>();
            statItem.put("status", s);
            statItem.put("cnt", c);
            stats.add(statItem);
        }
        result.put("stats", stats);

        // frontend expects dailyOutput with [{day, output}]
        List<Map<String, Object>> dailyOutput = mapper.dashboardDailyOutput();
        for (Map<String, Object> item : dailyOutput) {
            if (item.containsKey("date")) {
                item.put("day", item.remove("date"));
            }
        }
        result.put("dailyOutput", dailyOutput);

        // frontend expects lineOutput with [{line, output}]
        List<Map<String, Object>> lineOutput = mapper.dashboardLineOutput();
        for (Map<String, Object> item : lineOutput) {
            if (item.containsKey("name")) {
                item.put("line", item.remove("name"));
            }
            if (item.containsKey("value")) {
                item.put("output", item.remove("value"));
            }
        }
        result.put("lineOutput", lineOutput);

        return result;
    }
}
