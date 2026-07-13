package com.example.mes.modules.production.mapper;

import com.example.mes.modules.production.entity.ProdWip;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProdWipMapper {

    int insert(ProdWip wip);

    int update(ProdWip wip);

    List<ProdWip> selectList(@Param("offset") int offset,
                             @Param("limit") int limit);

    long countList();

    List<ProdWip> selectByProcessCode(@Param("processCode") String processCode);

    int updateQty(@Param("id") Long id,
                  @Param("wipQty") java.math.BigDecimal wipQty);
}
