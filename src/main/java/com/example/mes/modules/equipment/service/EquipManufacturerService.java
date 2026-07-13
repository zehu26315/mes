package com.example.mes.modules.equipment.service;

import com.example.mes.modules.equipment.entity.EquipManufacturer;
import java.util.List;

public interface EquipManufacturerService {
    EquipManufacturer create(EquipManufacturer e);
    EquipManufacturer update(EquipManufacturer e);
    void delete(Long id);
    EquipManufacturer getById(Long id);
    List<EquipManufacturer> listAll();
}
