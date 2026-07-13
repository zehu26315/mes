package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipFault;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipFaultMapper {
    int insert(EquipFault e); int update(EquipFault e);
    EquipFault selectById(@Param("id") Long id);
    List<EquipFault> selectAll();
    int deleteById(@Param("id") Long id);
}
