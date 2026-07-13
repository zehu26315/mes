package com.example.mes.modules.barcode.mapper;

import com.example.mes.modules.barcode.entity.BarcodeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BarcodeRecordMapper {

    int insert(BarcodeRecord barcodeRecord);

    int update(BarcodeRecord barcodeRecord);

    BarcodeRecord selectById(@Param("id") Long id);

    BarcodeRecord selectByBarcodeNo(@Param("barcodeNo") String barcodeNo);

    List<BarcodeRecord> selectList(@Param("keyword") String keyword,
                                    @Param("status") Integer status,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
