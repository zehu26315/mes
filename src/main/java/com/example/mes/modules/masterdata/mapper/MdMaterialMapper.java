package com.example.mes.modules.masterdata.mapper;

import com.example.mes.modules.masterdata.entity.MdMaterial;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MdMaterialMapper {

    int insert(MdMaterial material);

    int update(MdMaterial material);

    MdMaterial selectById(@Param("id") Long id);

    List<MdMaterial> selectList(@Param("keyword") String keyword,
                                @Param("status") Integer status,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
