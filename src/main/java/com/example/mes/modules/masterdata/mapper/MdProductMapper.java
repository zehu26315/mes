package com.example.mes.modules.masterdata.mapper;

import com.example.mes.modules.masterdata.entity.MdProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MdProductMapper {

    int insert(MdProduct product);

    int update(MdProduct product);

    MdProduct selectById(@Param("id") Long id);

    List<MdProduct> selectList(@Param("keyword") String keyword,
                               @Param("status") Integer status,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
