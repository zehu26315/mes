package com.example.mes.modules.barcode.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeApplication;

public interface BarcodeApplicationService {

    BarcodeApplication create(BarcodeApplication barcodeApplication);

    BarcodeApplication update(BarcodeApplication barcodeApplication);

    void deleteById(Long id);

    BarcodeApplication getById(Long id);

    PageResult<BarcodeApplication> list(String keyword, Integer status, int page, int pageSize);
}
