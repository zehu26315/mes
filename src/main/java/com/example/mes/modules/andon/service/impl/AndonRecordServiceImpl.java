package com.example.mes.modules.andon.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.andon.entity.AndonRecord;
import com.example.mes.modules.andon.mapper.AndonRecordMapper;
import com.example.mes.modules.andon.service.AndonRecordService;
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
public class AndonRecordServiceImpl implements AndonRecordService {

    private final AndonRecordMapper andonRecordMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.ANDON_RECORD_LIST, allEntries = true)
    public AndonRecord create(AndonRecord andonRecord) {
        if (andonRecord.getStatus() == null) {
            andonRecord.setStatus(1);
        }
        if (andonRecord.getInitTime() == null) {
            andonRecord.setInitTime(LocalDateTime.now());
        }
        andonRecordMapper.insert(andonRecord);
        return andonRecord;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.ANDON_RECORD, CacheNames.ANDON_RECORD_LIST}, allEntries = true)
    public AndonRecord update(AndonRecord andonRecord) {
        andonRecord.setCreateTime(null);
        andonRecord.setUpdateTime(LocalDateTime.now());
        andonRecordMapper.update(andonRecord);
        return andonRecordMapper.selectById(andonRecord.getId());
    }

    @Override
    @Cacheable(value = CacheNames.ANDON_RECORD, key = "#id", unless = "#result == null")
    public AndonRecord getById(Long id) {
        return andonRecordMapper.selectById(id);
    }

    @Override
    public PageResult<AndonRecord> list(String keyword, Long andonConfigId, Integer status, String productionLine,
                                        String startTime, String endTime, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        LocalDateTime start = null;
        LocalDateTime end = null;
        try {
            if (startTime != null && !startTime.isEmpty()) {
                start = LocalDateTime.parse(startTime, FORMATTER);
            }
            if (endTime != null && !endTime.isEmpty()) {
                end = LocalDateTime.parse(endTime, FORMATTER);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("时间参数格式错误，请使用 yyyy-MM-dd HH:mm:ss");
        }
        List<AndonRecord> rows = andonRecordMapper.selectList(keyword, andonConfigId, status, productionLine,
                start, end, offset, pageSize);
        long total = andonRecordMapper.countList(keyword, andonConfigId, status, productionLine, start, end);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public List<AndonRecord> getActive() {
        return andonRecordMapper.selectActive();
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.ANDON_RECORD, CacheNames.ANDON_RECORD_LIST}, allEntries = true)
    public void delete(Long id) {
        andonRecordMapper.deleteById(id);
    }
}
