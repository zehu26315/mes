package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipMaintenancePlan;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipMaintenancePlanMapper {
    int insert(EquipMaintenancePlan e); int update(EquipMaintenancePlan e);
    EquipMaintenancePlan selectById(@Param("id") Long id);
    List<EquipMaintenancePlan> selectList(@Param("equipId") Long e, @Param("status") String s,
                                          @Param("startDate") String sd, @Param("endDate") String ed,
                                          @Param("offset") int o, @Param("limit") int l);
    long countList(@Param("equipId") Long e, @Param("status") String s,
                   @Param("startDate") String sd, @Param("endDate") String ed);
    int deleteById(@Param("id") Long id);
}
