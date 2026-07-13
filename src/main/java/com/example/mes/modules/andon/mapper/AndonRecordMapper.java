package com.example.mes.modules.andon.mapper;

import com.example.mes.modules.andon.entity.AndonRecord;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AndonRecordMapper {

    int insert(AndonRecord andonRecord);

    int updateStatus(@Param("id") Long id,
                     @Param("status") String status,
                     @Param("handler") String handler,
                     @Param("confirmTime") LocalDateTime confirmTime,
                     @Param("resolveTime") LocalDateTime resolveTime,
                     @Param("resolveDesc") String resolveDesc,
                     @Param("duration") Long duration);

    AndonRecord selectById(@Param("id") Long id);

    List<AndonRecord> selectList(@Param("keyword") String keyword,
                                 @Param("andonTypeId") Long andonTypeId,
                                 @Param("status") String status,
                                 @Param("workCenter") String workCenter,
                                 @Param("startTime") LocalDateTime startTime,
                                 @Param("endTime") LocalDateTime endTime,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("andonTypeId") Long andonTypeId,
                   @Param("status") String status,
                   @Param("workCenter") String workCenter,
                   @Param("startTime") LocalDateTime startTime,
                   @Param("endTime") LocalDateTime endTime);

    List<AndonRecord> selectActive();

    int deleteById(@Param("id") Long id);
}
