package com.example.mes.modules.process.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.process.entity.PrcOperation;
import com.example.mes.modules.process.mapper.PrcOperationMapper;
import com.example.mes.modules.process.service.PrcOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrcOperationServiceImpl implements PrcOperationService {

    private final PrcOperationMapper mapper;

    @Override @Transactional
    @CacheEvict(value = CacheNames.PRC_OPERATION_LIST, allEntries = true)
    public PrcOperation create(PrcOperation entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(1);
        mapper.insert(entity);
        return entity;
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PRC_OPERATION, CacheNames.PRC_OPERATION_LIST}, allEntries = true)
    public PrcOperation update(PrcOperation entity) {
        entity.setOperationCode(null);
        entity.setCreateTime(null);
        entity.setUpdateTime(LocalDateTime.now());
        mapper.update(entity);
        return mapper.selectById(entity.getId());
    }

    @Override @Transactional
    @CacheEvict(value = {CacheNames.PRC_OPERATION, CacheNames.PRC_OPERATION_LIST}, allEntries = true)
    public void delete(Long id) { mapper.deleteById(id); }

    @Override
    @Cacheable(value = CacheNames.PRC_OPERATION, key = "#id", unless = "#result == null")
    public PrcOperation getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<PrcOperation> list(String operationCode, String operationName, Integer operationType,
                                          Integer status, String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<PrcOperation> rows = mapper.selectList(operationCode, operationName, operationType, status, keyword, offset, pageSize);
        long total = mapper.countList(operationCode, operationName, operationType, status, keyword);
        return PageResult.of(total, page, pageSize, rows);
    }
}
