package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysConfigMapper {

    int update(SysConfig config);

    List<SysConfig> selectAll();

    SysConfig selectById(@Param("id") Long id);
}
