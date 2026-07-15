package com.example.mes.modules.equipment.mapper;

import com.example.mes.modules.equipment.entity.EquipInspection;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EquipInspectionMapper {

    int insert(EquipInspection e);

    int update(EquipInspection e);

    int deleteById(@Param("id") Long id);

    EquipInspection selectById(@Param("id") Long id);

    List<EquipInspection> selectList(@Param("keyword") String keyword,
                                      @Param("equipmentId") Long equipmentId,
                                      @Param("result") Integer result,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("equipmentId") Long equipmentId,
                   @Param("result") Integer result);
}
