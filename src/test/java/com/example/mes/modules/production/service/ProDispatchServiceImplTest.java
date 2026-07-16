package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProDispatch;
import com.example.mes.modules.production.mapper.ProDispatchMapper;
import com.example.mes.modules.production.service.impl.ProDispatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProDispatchService 生产派工服务")
class ProDispatchServiceImplTest {

    @Mock
    private ProDispatchMapper mapper;

    @InjectMocks
    private ProDispatchServiceImpl service;

    private ProDispatch dispatch;

    @BeforeEach
    void setUp() {
        dispatch = ProDispatch.builder()
                .id(1L)
                .dispatchNo("DP-20260701-001")
                .workOrderId(100L)
                .operationId(10L)
                .productionLine("LINE-A")
                .planQuantity(new BigDecimal("500"))
                .actualQuantity(BigDecimal.ZERO)
                .assigneeId(50L)
                .status(1)
                .isDeleted(0)
                .build();
    }

    // ==================== create ====================

    @Test
    @DisplayName("create → 设置时间戳 + 默认值 + insert")
    void create_success() {
        ProDispatch input = ProDispatch.builder()
                .dispatchNo("DP-20260701-002")
                .workOrderId(200L)
                .status(null)
                .build();

        ProDispatch result = service.create(input);

        assertNotNull(input.getCreateTime());
        assertNotNull(input.getUpdateTime());
        assertEquals(1, input.getStatus());
        verify(mapper).insert(input);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("create → status 已有值时不覆盖")
    void create_preservesExistingValues() {
        ProDispatch input = ProDispatch.builder()
                .status(2)
                .build();

        service.create(input);

        assertEquals(2, input.getStatus());
    }

    // ==================== update ====================

    @Test
    @DisplayName("update → null 掉 dispatchNo 和 createTime → 更新后重查")
    void update_success() {
        ProDispatch input = ProDispatch.builder()
                .id(1L)
                .dispatchNo("should_be_nulled")
                .createTime(LocalDateTime.of(2020, 1, 1, 0, 0))
                .productionLine("LINE-B")
                .actualQuantity(new BigDecimal("300"))
                .build();

        ProDispatch refreshed = ProDispatch.builder()
                .id(1L)
                .dispatchNo(null)
                .productionLine("LINE-B")
                .actualQuantity(new BigDecimal("300"))
                .build();

        when(mapper.selectById(1L)).thenReturn(refreshed);

        ProDispatch result = service.update(input);

        assertNull(input.getDispatchNo());
        assertNull(input.getCreateTime());
        assertNotNull(input.getUpdateTime());
        verify(mapper).update(input);
        assertSame(refreshed, result);
    }

    // ==================== delete ====================

    @Test
    @DisplayName("delete → 调用 mapper.deleteById")
    void delete_success() {
        service.delete(1L);
        verify(mapper).deleteById(1L);
    }

    // ==================== getById ====================

    @Test
    @DisplayName("getById 存在 → 返回实体")
    void getById_found() {
        when(mapper.selectById(1L)).thenReturn(dispatch);
        assertSame(dispatch, service.getById(1L));
    }

    @Test
    @DisplayName("getById 不存在 → 返回 null")
    void getById_notFound() {
        when(mapper.selectById(999L)).thenReturn(null);
        assertNull(service.getById(999L));
    }

    // ==================== list ====================

    @Test
    @DisplayName("list 分页 → offset=(page-1)*pageSize")
    void list_pagination() {
        when(mapper.selectList("DP", 100L, "LINE-A", 1, 50L, 20, 10)).thenReturn(List.of(dispatch));
        when(mapper.countList("DP", 100L, "LINE-A", 1, 50L)).thenReturn(50L);

        PageResult<ProDispatch> result = service.list("DP", 100L, "LINE-A", 1, 50L, 3, 10);

        assertEquals(50L, result.getTotal());
        assertEquals(3, result.getPage());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getRows().size());
    }

    @Test
    @DisplayName("list 第一页 → offset=0")
    void list_firstPage() {
        when(mapper.selectList(null, null, null, null, null, 0, 10)).thenReturn(List.of());
        when(mapper.countList(null, null, null, null, null)).thenReturn(0L);

        PageResult<ProDispatch> result = service.list(null, null, null, null, null, 1, 10);

        assertEquals(1, result.getPage());
        verify(mapper).selectList(null, null, null, null, null, 0, 10);
    }
}
