package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipInspectionPlan;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipInspectionPlanMapper {
    int insert(EquipInspectionPlan e); int update(EquipInspectionPlan e);
    EquipInspectionPlan selectById(@Param("id") Long id);
    List<EquipInspectionPlan> selectAll();
    int deleteById(@Param("id") Long id);
}
