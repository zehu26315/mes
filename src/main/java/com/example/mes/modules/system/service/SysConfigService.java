package com.example.mes.modules.system.service;

import com.example.mes.modules.system.entity.SysConfig;

import java.util.List;

public interface SysConfigService {

    SysConfig updateConfig(SysConfig config);

    List<SysConfig> listAll();

    SysConfig getConfigById(Long id);
}
