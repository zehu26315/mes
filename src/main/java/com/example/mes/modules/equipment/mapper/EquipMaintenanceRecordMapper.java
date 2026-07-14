package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipMaintenanceRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipMaintenanceRecordMapper {
    int insert(EquipMaintenanceRecord e);
    List<EquipMaintenanceRecord> selectByEquipId(@Param("equipId") Long id);
    List<EquipMaintenanceRecord> selectList(@Param("equipId") Long equipId,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);
    long countList(@Param("equipId") Long equipId,
                   @Param("startDate") String startDate,
                   @Param("endDate") String endDate);
    int completePlan(@Param("planId") Long planId);
}
