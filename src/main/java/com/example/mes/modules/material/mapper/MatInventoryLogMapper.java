package com.example.mes.modules.material.mapper;

import com.example.mes.modules.material.entity.MatInventoryLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatInventoryLogMapper {

    int insert(MatInventoryLog log);

    List<MatInventoryLog> selectList(@Param("materialId") Long materialId,
                                     @Param("materialCode") String materialCode,
                                     @Param("changeType") String changeType,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    long countList(@Param("materialId") Long materialId,
                   @Param("materialCode") String materialCode,
                   @Param("changeType") String changeType);
}
