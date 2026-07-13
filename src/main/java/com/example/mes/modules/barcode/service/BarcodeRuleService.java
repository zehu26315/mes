package com.example.mes.modules.barcode.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeRule;

public interface BarcodeRuleService {

    BarcodeRule create(BarcodeRule barcodeRule);

    BarcodeRule update(BarcodeRule barcodeRule);

    void deleteById(Long id);

    BarcodeRule getById(Long id);

    PageResult<BarcodeRule> list(String keyword, Integer status, int page, int pageSize);
}
