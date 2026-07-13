package com.example.mes.modules.andon.mapper;

import com.example.mes.modules.andon.entity.AndonReason;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AndonReasonMapper {

    int insert(AndonReason andonReason);

    int update(AndonReason andonReason);

    AndonReason selectById(@Param("id") Long id);

    List<AndonReason> selectAll();

    List<AndonReason> selectByAndonTypeId(@Param("andonTypeId") Long andonTypeId);

    List<AndonReason> selectList(@Param("keyword") String keyword,
                                 @Param("andonTypeId") Long andonTypeId,
                                 @Param("status") Integer status,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countList(@Param("keyword") String keyword,
                   @Param("andonTypeId") Long andonTypeId,
                   @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
