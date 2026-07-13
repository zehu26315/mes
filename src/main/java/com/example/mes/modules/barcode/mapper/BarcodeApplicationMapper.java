package com.example.mes.modules.barcode.mapper;

import com.example.mes.modules.barcode.entity.BarcodeApplication;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BarcodeApplicationMapper {

    int insert(BarcodeApplication barcodeApplication);

    int update(BarcodeApplication barcodeApplication);

    BarcodeApplication selectById(@Param("id") Long id);

    List<BarcodeApplication> selectList(@Param("keyword") String keyword,
                                         @Param("status") Integer status,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
