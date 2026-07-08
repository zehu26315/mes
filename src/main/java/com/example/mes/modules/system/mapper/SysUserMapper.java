package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {

    int insert(SysUser user);

    int update(SysUser user);

    SysUser selectById(@Param("id") Long id);

    List<SysUser> selectList(@Param("keyword") String keyword,
                             @Param("status") Integer status,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
