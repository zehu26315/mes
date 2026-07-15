package com.example.mes.modules.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysNotice {

    private Long id;
    private String noticeTitle;
    /** 0=公告, 1=通知, 2=提醒 */
    private Integer noticeType;
    private String noticeContent;
    private String createBy;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
