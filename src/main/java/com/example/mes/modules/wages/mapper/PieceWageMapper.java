package com.example.mes.modules.wages.mapper;

import com.example.mes.modules.wages.entity.PieceWage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PieceWageMapper {
    int insert(PieceWage entity);
    int update(PieceWage entity);
    PieceWage selectById(@Param("id") Long id);
    List<PieceWage> selectList(@Param("keyword") String keyword,
                                @Param("status") Integer status,
                                @Param("wageDate") String wageDate,
                                @Param("offset") int offset, @Param("limit") int limit);
    long countList(@Param("keyword") String keyword,
                   @Param("status") Integer status,
                   @Param("wageDate") String wageDate);
    int deleteById(@Param("id") Long id);
}
