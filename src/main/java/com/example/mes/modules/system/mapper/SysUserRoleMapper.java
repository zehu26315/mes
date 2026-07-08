package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRoleMapper {

    int insert(SysUserRole userRole);

    int batchInsert(@Param("list") List<SysUserRole> list);

    int deleteByUserId(@Param("userId") Long userId);

    List<SysUserRole> selectByUserId(@Param("userId") Long userId);
}
