package com.example.mes.modules.equipment.mapper;

import com.example.mes.modules.equipment.entity.EquipMaintenance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EquipMaintenanceMapper {

    int insert(EquipMaintenance e);

    int update(EquipMaintenance e);

    int deleteById(@Param("id") Long id);

    EquipMaintenance selectById(@Param("id") Long id);

    List<EquipMaintenance> selectList(@Param("keyword") String keyword,
                                       @Param("equipmentId") Long equipmentId,
                                       @Param("maintenanceType") String maintenanceType,
                                       @Param("status") Integer status,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("equipmentId") Long equipmentId,
                   @Param("maintenanceType") String maintenanceType,
                   @Param("status") Integer status);
}
