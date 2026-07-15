package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipRepair;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;
public interface EquipRepairMapper {
    int insert(EquipRepair e);
    int update(EquipRepair e);
    EquipRepair selectById(@Param("id") Long id);
    List<EquipRepair> selectList(@Param("keyword") String keyword, @Param("equipmentId") Long equipmentId,
                                 @Param("status") Integer status,
                                 @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("keyword") String keyword, @Param("equipmentId") Long equipmentId,
                   @Param("status") Integer status);
    int deleteById(@Param("id") Long id);
    int complete(@Param("id") Long id, @Param("endTime") LocalDateTime endTime,
                 @Param("status") Integer status, @Param("remark") String remark);
}
