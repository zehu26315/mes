package com.example.mes.modules.barcode.mapper;

import com.example.mes.modules.barcode.entity.BarcodeTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BarcodeTemplateMapper {

    int insert(BarcodeTemplate barcodeTemplate);

    int update(BarcodeTemplate barcodeTemplate);

    BarcodeTemplate selectById(@Param("id") Long id);

    List<BarcodeTemplate> selectList(@Param("keyword") String keyword,
                                      @Param("status") Integer status,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
