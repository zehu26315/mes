package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMenuMapper {

    int deleteByRoleId(@Param("roleId") Long roleId);

    int batchInsert(@Param("list") List<SysRoleMenu> list);

    List<SysRoleMenu> selectByRoleId(@Param("roleId") Long roleId);
}
