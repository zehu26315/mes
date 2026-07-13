package com.example.mes.modules.masterdata.mapper;

import com.example.mes.modules.masterdata.entity.MdMaterialCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MdMaterialCategoryMapper {

    int insert(MdMaterialCategory category);

    int update(MdMaterialCategory category);

    MdMaterialCategory selectById(@Param("id") Long id);

    List<MdMaterialCategory> selectList(@Param("keyword") String keyword,
                                        @Param("status") Integer status,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
