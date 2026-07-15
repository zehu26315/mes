package com.example.mes.modules.equipment.mapper;

import com.example.mes.modules.equipment.entity.EquipLedger;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EquipLedgerMapper {

    int insert(EquipLedger e);

    int update(EquipLedger e);

    EquipLedger selectById(@Param("id") Long id);

    List<EquipLedger> selectList(@Param("equipmentCode") String equipmentCode,
                                  @Param("equipmentName") String equipmentName,
                                  @Param("category") String category,
                                  @Param("status") Integer status,
                                  @Param("keyword") String keyword,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    long countList(@Param("equipmentCode") String equipmentCode,
                   @Param("equipmentName") String equipmentName,
                   @Param("category") String category,
                   @Param("status") Integer status,
                   @Param("keyword") String keyword);

    int deleteById(@Param("id") Long id);
}
