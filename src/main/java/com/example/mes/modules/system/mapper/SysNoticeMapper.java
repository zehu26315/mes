package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysNotice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysNoticeMapper {

    int insert(SysNotice notice);

    int update(SysNotice notice);

    SysNotice selectById(@Param("id") Long id);

    List<SysNotice> selectList(@Param("keyword") String keyword,
                               @Param("noticeType") Integer noticeType,
                               @Param("status") Integer status,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("noticeType") Integer noticeType,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
