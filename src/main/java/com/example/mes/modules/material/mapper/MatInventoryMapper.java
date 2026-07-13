package com.example.mes.modules.material.mapper;

import com.example.mes.modules.material.entity.MatInventory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatInventoryMapper {

    int insert(MatInventory inventory);

    int update(MatInventory inventory);

    List<MatInventory> selectList(@Param("materialId") Long materialId,
                                  @Param("materialCode") String materialCode,
                                  @Param("warehouse") String warehouse,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);

    long countList(@Param("materialId") Long materialId,
                   @Param("materialCode") String materialCode,
                   @Param("warehouse") String warehouse);

    List<MatInventory> selectLowStock(@Param("offset") int offset,
                                      @Param("limit") int limit);
}
