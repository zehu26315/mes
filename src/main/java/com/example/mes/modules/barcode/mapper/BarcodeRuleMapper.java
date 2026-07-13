package com.example.mes.modules.barcode.mapper;

import com.example.mes.modules.barcode.entity.BarcodeRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BarcodeRuleMapper {

    int insert(BarcodeRule barcodeRule);

    int update(BarcodeRule barcodeRule);

    BarcodeRule selectById(@Param("id") Long id);

    List<BarcodeRule> selectList(@Param("keyword") String keyword,
                                  @Param("status") Integer status,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
