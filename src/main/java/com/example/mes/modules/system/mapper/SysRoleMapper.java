package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {

    int insert(SysRole role);

    int update(SysRole role);

    SysRole selectById(@Param("id") Long id);

    List<SysRole> selectAll();

    List<SysRole> selectList(@Param("keyword") String keyword,
                             @Param("status") Integer status,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
