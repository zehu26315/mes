package com.example.mes.modules.material.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.material.entity.MatReturn;

public interface MatReturnService {

    MatReturn create(MatReturn matReturn);

    MatReturn getById(Long id);

    PageResult<MatReturn> list(String status, Long workOrderId, String materialCode, int page, int pageSize);
}
