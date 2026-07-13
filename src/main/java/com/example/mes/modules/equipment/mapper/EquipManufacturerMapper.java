package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipManufacturer;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipManufacturerMapper {
    int insert(EquipManufacturer e); int update(EquipManufacturer e);
    EquipManufacturer selectById(@Param("id") Long id);
    List<EquipManufacturer> selectAll();
    int deleteById(@Param("id") Long id);
}
