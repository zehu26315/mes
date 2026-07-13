package com.example.mes.modules.masterdata.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.masterdata.entity.MdMaterial;

public interface MdMaterialService {

    MdMaterial createMaterial(MdMaterial material);

    MdMaterial updateMaterial(MdMaterial material);

    void deleteMaterial(Long id);

    MdMaterial getMaterialById(Long id);

    PageResult<MdMaterial> listMaterials(String keyword, Integer status, int page, int pageSize);
}
