package com.example.mes.modules.andon.mapper;

import com.example.mes.modules.andon.entity.AndonType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AndonTypeMapper {

    int insert(AndonType andonType);

    int update(AndonType andonType);

    AndonType selectById(@Param("id") Long id);

    List<AndonType> selectList(@Param("keyword") String keyword,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    long countList(@Param("keyword") String keyword);

    int deleteById(@Param("id") Long id);
}
