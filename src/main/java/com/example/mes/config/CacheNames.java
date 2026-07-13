package com.example.mes.config;

/**
 * Centralized cache name constants to avoid string typos in {@link org.springframework.cache.annotation.Cacheable}
 * and {@link org.springframework.cache.annotation.CacheEvict} annotations.
 */
public interface CacheNames {

    String SYS_USER = "sys_user";
    String SYS_USER_LIST = "sys_user_list";

    String SYS_ROLE = "sys_role";
    String SYS_ROLE_ALL = "sys_role_all";
    String SYS_ROLE_LIST = "sys_role_list";
}
