package com.example.mes.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    private Long userId;
    private String username;
    private String realName;
    private Long deptId;
    private List<String> roles;
}
