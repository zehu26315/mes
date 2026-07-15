package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysLoginLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLoginLogMapper {

    int insert(SysLoginLog log);

    SysLoginLog selectById(@Param("id") Long id);

    List<SysLoginLog> selectList(@Param("loginName") String loginName,
                                 @Param("status") Integer status,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countList(@Param("loginName") String loginName,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
