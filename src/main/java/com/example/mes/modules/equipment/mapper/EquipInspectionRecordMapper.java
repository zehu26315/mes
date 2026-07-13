package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipInspectionRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipInspectionRecordMapper {
    int insert(EquipInspectionRecord e);
    List<EquipInspectionRecord> selectByEquipId(@Param("equipId") Long id);
}
