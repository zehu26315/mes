package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipMaintenanceRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipMaintenanceRecordMapper {
    int insert(EquipMaintenanceRecord e);
    List<EquipMaintenanceRecord> selectByEquipId(@Param("equipId") Long id);
    int completePlan(@Param("planId") Long planId);
}
