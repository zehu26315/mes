package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipRepair;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipRepairMapper {
    int insert(EquipRepair e); int update(EquipRepair e);
    EquipRepair selectById(@Param("id") Long id);
    List<EquipRepair> selectList(@Param("equipId") Long e, @Param("status") String s,
                                 @Param("offset") int o, @Param("limit") int l);
    long countList(@Param("equipId") Long e, @Param("status") String s);
    int deleteById(@Param("id") Long id);
    int complete(@Param("id") Long id, @Param("endTime") java.time.LocalDateTime et, @Param("downtime") Long d);
}
