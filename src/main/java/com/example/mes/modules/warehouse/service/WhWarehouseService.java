package com.example.mes.modules.warehouse.service;

import com.example.mes.modules.warehouse.entity.WhWarehouse;
import java.util.List;

public interface WhWarehouseService {
    WhWarehouse create(WhWarehouse entity);
    WhWarehouse update(WhWarehouse entity);
    void delete(Long id);
    WhWarehouse getById(Long id);
    List<WhWarehouse> listAll();
}
