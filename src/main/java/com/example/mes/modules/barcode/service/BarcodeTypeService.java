package com.example.mes.modules.barcode.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeType;

public interface BarcodeTypeService {

    BarcodeType create(BarcodeType barcodeType);

    BarcodeType update(BarcodeType barcodeType);

    void deleteById(Long id);

    BarcodeType getById(Long id);

    PageResult<BarcodeType> list(String keyword, Integer status, int page, int pageSize);
}
