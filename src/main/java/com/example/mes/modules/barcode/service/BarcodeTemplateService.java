package com.example.mes.modules.barcode.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeTemplate;

public interface BarcodeTemplateService {

    BarcodeTemplate create(BarcodeTemplate barcodeTemplate);

    BarcodeTemplate update(BarcodeTemplate barcodeTemplate);

    void deleteById(Long id);

    BarcodeTemplate getById(Long id);

    PageResult<BarcodeTemplate> list(String keyword, Integer status, int page, int pageSize);
}
