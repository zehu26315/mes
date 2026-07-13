package com.example.mes.modules.barcode.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.barcode.entity.BarcodeRecord;

public interface BarcodeRecordService {

    BarcodeRecord create(BarcodeRecord barcodeRecord);

    BarcodeRecord getById(Long id);

    PageResult<BarcodeRecord> list(String keyword, Integer status, int page, int pageSize);

    void deleteById(Long id);
}
