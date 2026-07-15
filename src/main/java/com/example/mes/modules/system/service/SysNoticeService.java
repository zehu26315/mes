package com.example.mes.modules.system.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysNotice;

public interface SysNoticeService {

    SysNotice create(SysNotice notice);

    SysNotice update(SysNotice notice);

    void delete(Long id);

    SysNotice getById(Long id);

    PageResult<SysNotice> list(String keyword, Integer noticeType, Integer status, int page, int pageSize);
}
