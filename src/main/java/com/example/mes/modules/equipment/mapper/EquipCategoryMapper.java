package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipCategory;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipCategoryMapper {
    int insert(EquipCategory e); int update(EquipCategory e);
    EquipCategory selectById(@Param("id") Long id);
    List<EquipCategory> selectAll();
    int deleteById(@Param("id") Long id);
}
