package com.example.mes.modules.equipment.service;

import com.example.mes.common.response.TreeNode;
import com.example.mes.modules.equipment.entity.EquipCategory;
import java.util.List;

public interface EquipCategoryService {
    EquipCategory create(EquipCategory e);
    EquipCategory update(EquipCategory e);
    void delete(Long id);
    EquipCategory getById(Long id);
    List<TreeNode<EquipCategory>> getTree();
}
