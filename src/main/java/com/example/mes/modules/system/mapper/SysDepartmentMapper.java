package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysDepartment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDepartmentMapper {

    int insert(SysDepartment dept);

    int update(SysDepartment dept);

    SysDepartment selectById(@Param("id") Long id);

    List<SysDepartment> selectList(@Param("keyword") String keyword,
                                   @Param("status") Integer status,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
