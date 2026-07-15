package com.example.mes.modules.system.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.system.entity.SysNotice;
import com.example.mes.modules.system.mapper.SysNoticeMapper;
import com.example.mes.modules.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysNoticeMapper noticeMapper;

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.SYS_NOTICE_LIST}, allEntries = true)
    public SysNotice create(SysNotice notice) {
        notice.setCreateTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        if (notice.getStatus() == null) {
            notice.setStatus(1);
        }
        if (notice.getNoticeType() == null) {
            notice.setNoticeType(0);
        }
        noticeMapper.insert(notice);
        return notice;
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.SYS_NOTICE, CacheNames.SYS_NOTICE_LIST}, allEntries = true)
    public SysNotice update(SysNotice notice) {
        notice.setCreateBy(null);  // immutable
        notice.setCreateTime(null); // immutable
        notice.setUpdateTime(LocalDateTime.now());
        noticeMapper.update(notice);
        return noticeMapper.selectById(notice.getId());
    }

    @Override
    @Transactional
    @CacheEvict(value = {CacheNames.SYS_NOTICE, CacheNames.SYS_NOTICE_LIST}, allEntries = true)
    public void delete(Long id) {
        noticeMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.SYS_NOTICE, key = "#id", unless = "#result == null")
    public SysNotice getById(Long id) {
        return noticeMapper.selectById(id);
    }

    @Override
    public PageResult<SysNotice> list(String keyword, Integer noticeType, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<SysNotice> rows = noticeMapper.selectList(keyword, noticeType, status, offset, pageSize);
        long total = noticeMapper.countList(keyword, noticeType, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
