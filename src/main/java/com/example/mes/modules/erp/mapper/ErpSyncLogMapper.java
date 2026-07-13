package com.example.mes.modules.erp.mapper;

import com.example.mes.modules.erp.entity.ErpSyncLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ErpSyncLogMapper {

    int insert(ErpSyncLog log);

    List<ErpSyncLog> selectList(@Param("keyword") String keyword,
                                @Param("status") String status,
                                @Param("offset") int offset,
                                @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("status") String status);
}
