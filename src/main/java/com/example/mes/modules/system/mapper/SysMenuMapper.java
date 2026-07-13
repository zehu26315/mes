package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper {

    int insert(SysMenu menu);

    int update(SysMenu menu);

    SysMenu selectById(@Param("id") Long id);

    List<SysMenu> selectList(@Param("keyword") String keyword,
                             @Param("status") Integer status,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
