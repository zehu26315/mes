package com.example.mes.modules.wages.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.wages.entity.PieceWage;
import com.example.mes.modules.wages.mapper.PieceWageMapper;
import com.example.mes.modules.wages.service.PieceWageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PieceWageServiceImpl implements PieceWageService {

    private final PieceWageMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.WAGES_PIECE_LIST, allEntries = true)
    public PieceWage create(PieceWage entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(0);
        if (entity.getUnitPrice() == null) entity.setUnitPrice(BigDecimal.ZERO);
        if (entity.getCompletedQty() == null) entity.setCompletedQty(0);
        // Auto-calculate totalWage
        entity.setTotalWage(entity.getUnitPrice().multiply(BigDecimal.valueOf(entity.getCompletedQty())));
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.WAGES_PIECE, CacheNames.WAGES_PIECE_LIST}, allEntries = true)
    public PieceWage update(PieceWage entity) {
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        // Re-calculate totalWage if both unitPrice and completedQty are set
        if (entity.getUnitPrice() != null && entity.getCompletedQty() != null) {
            entity.setTotalWage(entity.getUnitPrice().multiply(BigDecimal.valueOf(entity.getCompletedQty())));
        }
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.WAGES_PIECE, CacheNames.WAGES_PIECE_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override
    @Cacheable(value = CacheNames.WAGES_PIECE, key = "#id", unless = "#result == null")
    public PieceWage getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<PieceWage> list(String keyword, Integer status, String wageDate,
                                       int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PieceWage> rows = mapper.selectList(keyword, status, wageDate, offset, pageSize);
        long total = mapper.countList(keyword, status, wageDate);
        return PageResult.of(total, page, pageSize, rows);
    }
}
