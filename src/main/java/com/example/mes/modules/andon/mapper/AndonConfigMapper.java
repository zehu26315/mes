package com.example.mes.modules.andon.mapper;

import com.example.mes.modules.andon.entity.AndonConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AndonConfigMapper {

    int insert(AndonConfig andonConfig);

    int update(AndonConfig andonConfig);

    AndonConfig selectById(@Param("id") Long id);

    List<AndonConfig> selectList(@Param("keyword") String keyword,
                                 @Param("andonTypeId") Long andonTypeId,
                                 @Param("status") Integer status,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("andonTypeId") Long andonTypeId,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
