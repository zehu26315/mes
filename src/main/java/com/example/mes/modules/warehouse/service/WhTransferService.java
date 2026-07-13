package com.example.mes.modules.warehouse.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.warehouse.entity.WhTransfer;

public interface WhTransferService {
    WhTransfer create(WhTransfer entity);
    PageResult<WhTransfer> list(String status, Long fromWarehouseId, Long toWarehouseId, int page, int pageSize);
    WhTransfer getById(Long id);
}
