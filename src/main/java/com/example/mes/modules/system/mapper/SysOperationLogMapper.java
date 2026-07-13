package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysOperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysOperationLogMapper {

    int insert(SysOperationLog log);

    List<SysOperationLog> selectList(@Param("offset") int offset,
                                     @Param("limit") int limit);

    long countList();
}
