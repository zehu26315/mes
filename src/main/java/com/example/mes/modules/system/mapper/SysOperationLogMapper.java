package com.example.mes.modules.system.mapper;

import com.example.mes.modules.system.entity.SysOperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysOperationLogMapper {

    int insert(SysOperationLog log);

    SysOperationLog selectById(@Param("id") Long id);

    List<SysOperationLog> selectList(@Param("operationName") String operationName,
                                     @Param("operationType") String operationType,
                                     @Param("operationIp") String operationIp,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    long countList(@Param("operationName") String operationName,
                   @Param("operationType") String operationType,
                   @Param("operationIp") String operationIp,
                   @Param("startTime") String startTime,
                   @Param("endTime") String endTime);

    int deleteById(@Param("id") Long id);

    int deleteBatch(@Param("ids") List<Long> ids);

    List<Map<String, Object>> statsByType();

    List<Map<String, Object>> statsByOperator();

    long countTotal();
    long countToday();
    long countWeek();
    long countMonth();
    java.time.LocalDateTime getEarliestTime();
    java.time.LocalDateTime getLatestTime();
    java.time.LocalDateTime getEarliestDate(@Param("keepDays") int keepDays);
    long countOlderThan(@Param("keepDays") int keepDays);

    long deleteOlderThan(@Param("keepDays") int keepDays);
}
