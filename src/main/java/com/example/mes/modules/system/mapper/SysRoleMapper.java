package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {

    int insert(SysRole role);

    int update(SysRole role);

    SysRole selectById(@Param("id") Long id);

    List<SysRole> selectAll();

    /** 查询某用户所拥有的、启用中的角色编码（一次关联查询，避免全表加载后内存过滤）。 */
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    List<SysRole> selectList(@Param("keyword") String keyword,
                             @Param("status") Integer status,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
