package com.example.mes.modules.andon.mapper;

import com.example.mes.modules.andon.entity.AndonRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AndonRecordMapper {

    int insert(AndonRecord andonRecord);

    int update(AndonRecord andonRecord);

    AndonRecord selectById(@Param("id") Long id);

    List<AndonRecord> selectList(@Param("keyword") String keyword,
                                 @Param("andonConfigId") Long andonConfigId,
                                 @Param("status") Integer status,
                                 @Param("productionLine") String productionLine,
                                 @Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("andonConfigId") Long andonConfigId,
                   @Param("status") Integer status,
                   @Param("productionLine") String productionLine,
                   @Param("startTime") LocalDateTime startTime,
                   @Param("endTime") LocalDateTime endTime);

    List<AndonRecord> selectActive();

    int deleteById(@Param("id") Long id);
}
