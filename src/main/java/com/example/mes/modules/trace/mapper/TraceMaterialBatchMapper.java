package com.example.mes.modules.trace.mapper;

import com.example.mes.modules.trace.entity.TraceMaterialBatch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TraceMaterialBatchMapper {

    int insert(TraceMaterialBatch materialBatch);

    TraceMaterialBatch selectById(@Param("id") Long id);

    TraceMaterialBatch selectByBatchNo(@Param("batchNo") String batchNo);

    List<TraceMaterialBatch> selectList(@Param("offset") int offset,
                                         @Param("limit") int limit);

    long countList();
}
