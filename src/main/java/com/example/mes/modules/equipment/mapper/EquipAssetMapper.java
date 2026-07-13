package com.example.mes.modules.equipment.mapper;
import com.example.mes.modules.equipment.entity.EquipAsset;
import org.apache.ibatis.annotations.Param;
import java.util.List;
public interface EquipAssetMapper {
    int insert(EquipAsset e); int update(EquipAsset e);
    EquipAsset selectById(@Param("id") Long id);
    List<EquipAsset> selectList(@Param("keyword") String k, @Param("status") String s,
                                @Param("categoryId") Long c, @Param("productionLine") String p,
                                @Param("offset") int o, @Param("limit") int l);
    long countList(@Param("keyword") String k, @Param("status") String s,
                   @Param("categoryId") Long c, @Param("productionLine") String p);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
