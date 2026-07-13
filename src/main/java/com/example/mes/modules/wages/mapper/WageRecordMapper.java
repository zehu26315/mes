package com.example.mes.modules.wages.mapper;

import com.example.mes.modules.wages.entity.WageRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WageRecordMapper {

    int insert(WageRecord record);

    WageRecord selectById(@Param("id") Long id);

    List<WageRecord> selectList(@Param("keyword") String keyword,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    long countList(@Param("keyword") String keyword);

    int deleteById(@Param("id") Long id);
}
