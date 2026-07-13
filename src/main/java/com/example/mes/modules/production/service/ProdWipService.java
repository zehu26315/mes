package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdWip;

import java.util.List;

public interface ProdWipService {

    PageResult<ProdWip> list(int page, int pageSize);

    List<ProdWip> getByProcessCode(String processCode);
}
