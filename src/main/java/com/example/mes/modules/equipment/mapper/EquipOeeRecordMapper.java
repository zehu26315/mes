package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipOeeRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipOeeRecordMapper {
    int insert(EquipOeeRecord e);
    List<EquipOeeRecord> selectList(@Param("equipId") Long e, @Param("startDate") String sd,
                                    @Param("endDate") String ed, @Param("offset") int o, @Param("limit") int l);
    long countList(@Param("equipId") Long e, @Param("startDate") String sd, @Param("endDate") String ed);
}
