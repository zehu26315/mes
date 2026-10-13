package com.example.mes.modules.system.service;

import com.example.mes.common.response.LoginVO;
import com.example.mes.common.response.PageResult;
import com.example.mes.modules.system.entity.SysUser;
import com.example.mes.modules.system.mapper.SysUserMapper;
import com.example.mes.modules.system.service.impl.SysUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("SysUserService 用户服务")
class SysUserServiceImplTest {

    @Mock
    private SysUserMapper sysUserMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SysUserServiceImpl service;

    private SysUser user;

    @BeforeEach
    void setUp() {
        user = SysUser.builder()
                .id(1L)
                .username("admin")
                .password("encoded_pass")
                .nickname("管理员")
                .status(1)
                .isDeleted(0)
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encoded");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
    }

    // ==================== createUser ====================

    @Test
    @DisplayName("创建用户 用户名已存在 → 抛 IllegalArgumentException")
    void createUser_duplicateUsername() {
        when(sysUserMapper.selectByUsername("admin")).thenReturn(user);

        SysUser input = SysUser.builder().username("admin").password("123456").build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.createUser(input));
        assertTrue(ex.getMessage().contains("已存在"));
        verify(sysUserMapper, never()).insert(any());
    }

    @Test
    @DisplayName("创建用户 成功 → 密码加密 + status 默认 1 + insert")
    void createUser_success() {
        when(sysUserMapper.selectByUsername("newuser")).thenReturn(null);

        SysUser input = SysUser.builder()
                .username("newuser")
                .password("123456")
                .status(null)
                .build();

        SysUser result = service.createUser(input);

        verify(passwordEncoder).encode("123456");
        assertEquals("$2a$10$encoded", input.getPassword());
        assertEquals(1, input.getStatus());
        verify(sysUserMapper).insert(input);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("创建用户 username=null → 抛出 IllegalArgumentException")
    void createUser_nullUsername() {
        SysUser input = SysUser.builder().password("123456").build();

        assertThrows(IllegalArgumentException.class, () -> service.createUser(input));

        verify(sysUserMapper, never()).selectByUsername(any());
        verify(sysUserMapper, never()).insert(any());
    }

    // ==================== updateUser ====================

    @Test
    @DisplayName("更新用户 → 非空密码重新加密, username 置 null → 返回刷新实体")
    void updateUser() {
        SysUser input = SysUser.builder()
                .id(1L)
                .username("should_be_nulled")
                .password("new_password")
                .nickname("新名称")
                .build();

        SysUser refreshed = SysUser.builder()
                .id(1L)
                .username(null)
                .password(null)
                .nickname("新名称")
                .build();

        when(sysUserMapper.selectById(1L)).thenReturn(refreshed);

        SysUser result = service.updateUser(input);

        assertEquals("$2a$10$encoded", input.getPassword());
        assertNull(input.getUsername());
        verify(sysUserMapper).update(input);
        assertSame(refreshed, result);
    }

    // ==================== deleteUser ====================

    @Test
    @DisplayName("删除用户 → 调用 mapper.deleteById")
    void deleteUser() {
        service.deleteUser(1L);
        verify(sysUserMapper).deleteById(1L);
    }

    // ==================== getUserById ====================

    @Test
    @DisplayName("getUserById 存在 → 返回用户")
    void getUserById_found() {
        when(sysUserMapper.selectById(1L)).thenReturn(user);
        assertSame(user, service.getUserById(1L));
    }

    @Test
    @DisplayName("getUserById 不存在 → 返回 null")
    void getUserById_notFound() {
        when(sysUserMapper.selectById(999L)).thenReturn(null);
        assertNull(service.getUserById(999L));
    }

    // ==================== listUsers ====================

    @Test
    @DisplayName("分页列表 → offset=(page-1)*pageSize")
    void listUsers_pagination() {
        when(sysUserMapper.selectList("", 1, 20, 10)).thenReturn(List.of(user));
        when(sysUserMapper.countList("", 1)).thenReturn(50L);

        PageResult<SysUser> result = service.listUsers("", 1, 3, 10);

        assertEquals(50L, result.getTotal());
        assertEquals(3, result.getPage());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getRows().size());
    }

    @Test
    @DisplayName("分页 第一页 → offset=0")
    void listUsers_firstPage() {
        when(sysUserMapper.selectList(null, null, 0, 10)).thenReturn(List.of());
        when(sysUserMapper.countList(null, null)).thenReturn(0L);

        PageResult<SysUser> result = service.listUsers(null, null, 1, 10);

        assertEquals(1, result.getPage());
        verify(sysUserMapper).selectList(null, null, 0, 10);
    }

    // ==================== login ====================

    @Test
    @DisplayName("登录成功 → 有效凭据 + 用户启用 → 返回 LoginVO")
    void login_success() {
        when(passwordEncoder.matches("correct", user.getPassword())).thenReturn(true);
        when(sysUserMapper.selectByUsername("admin")).thenReturn(user);

        LoginVO result = service.login("admin", "correct");

        assertEquals(1L, result.getUserId());
        assertEquals("admin", result.getUsername());
        assertEquals("管理员", result.getNickname());
    }

    @Test
    @DisplayName("登录 密码错误 → 抛异常 + 走 dummyHash 防时序攻击")
    void login_wrongPassword() {
        when(sysUserMapper.selectByUsername("admin")).thenReturn(user);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.login("admin", "wrong"));
        assertEquals("用户名或密码错误", ex.getMessage());
    }

    @Test
    @DisplayName("登录 用户不存在 → 抛异常 + 走 dummyHash 防时序攻击")
    void login_userNotFound() {
        when(sysUserMapper.selectByUsername("ghost")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.login("ghost", "any"));
        assertEquals("用户名或密码错误", ex.getMessage());
    }

    @Test
    @DisplayName("登录 用户被禁用 status=0 → 抛异常")
    void login_disabledUser_statusZero() {
        SysUser disabled = SysUser.builder()
                .id(2L)
                .username("disabled")
                .password("hash")
                .status(0)
                .build();

        when(sysUserMapper.selectByUsername("disabled")).thenReturn(disabled);
        when(passwordEncoder.matches("pass", "hash")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.login("disabled", "pass"));
        assertEquals("用户名或密码错误", ex.getMessage());
    }

    @Test
    @DisplayName("登录 用户被禁用 status=null → 抛异常")
    void login_disabledUser_statusNull() {
        SysUser noStatus = SysUser.builder()
                .id(3L)
                .username("nostatus")
                .password("hash")
                .status(null)
                .build();

        when(sysUserMapper.selectByUsername("nostatus")).thenReturn(noStatus);
        when(passwordEncoder.matches("pass", "hash")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.login("nostatus", "pass"));
        assertEquals("用户名或密码错误", ex.getMessage());
    }
}
