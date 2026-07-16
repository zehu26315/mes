package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProCompletion;
import com.example.mes.modules.production.mapper.ProCompletionMapper;
import com.example.mes.modules.production.service.impl.ProCompletionServiceImpl;
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
@DisplayName("ProCompletionService 完工单服务")
class ProCompletionServiceImplTest {

    @Mock
    private ProCompletionMapper mapper;

    @InjectMocks
    private ProCompletionServiceImpl service;

    private ProCompletion completion;

    @BeforeEach
    void setUp() {
        completion = ProCompletion.builder()
                .id(1L)
                .completionNo("CMP-001")
                .workOrderId(100L)
                .materialId(200L)
                .planQuantity(new BigDecimal("1000"))
                .completedQuantity(new BigDecimal("950"))
                .qualifiedQuantity(new BigDecimal("930"))
                .defectQuantity(new BigDecimal("20"))
                .warehouseId(1L)
                .locationId(10L)
                .completionTime(LocalDateTime.of(2026, 7, 15, 14, 30))
                .inspectorId(5L)
                .status(1)
                .isDeleted(0)
                .build();
    }

    // ==================== create ====================

    @Test
    @DisplayName("create -> 设置时间戳 + status 默认 1 + insert")
    void create_success() {
        ProCompletion input = ProCompletion.builder()
                .completionNo("CMP-002")
                .workOrderId(101L)
                .status(null)
                .build();

        ProCompletion result = service.create(input);

        assertNotNull(input.getCreateTime());
        assertNotNull(input.getUpdateTime());
        assertEquals(1, input.getStatus());
        verify(mapper).insert(input);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("create -> status 已有值时不覆盖")
    void create_preservesExistingStatus() {
        ProCompletion input = ProCompletion.builder()
                .status(2)
                .build();

        service.create(input);

        assertEquals(2, input.getStatus());
    }

    // ==================== update ====================

    @Test
    @DisplayName("update -> null 掉 completionNo 和 createTime -> 更新后重查")
    void update_success() {
        ProCompletion input = ProCompletion.builder()
                .id(1L)
                .completionNo("should_be_nulled")
                .createTime(LocalDateTime.of(2020, 1, 1, 0, 0))
                .completedQuantity(new BigDecimal("1000"))
                .build();

        ProCompletion refreshed = ProCompletion.builder()
                .id(1L)
                .completionNo(null)
                .completedQuantity(new BigDecimal("1000"))
                .build();

        when(mapper.selectById(1L)).thenReturn(refreshed);

        ProCompletion result = service.update(input);

        assertNull(input.getCompletionNo());
        assertNull(input.getCreateTime());
        assertNotNull(input.getUpdateTime());
        verify(mapper).update(input);
        assertSame(refreshed, result);
    }

    // ==================== delete ====================

    @Test
    @DisplayName("delete -> 调用 mapper.deleteById")
    void delete_success() {
        service.delete(1L);
        verify(mapper).deleteById(1L);
    }

    // ==================== getById ====================

    @Test
    @DisplayName("getById 存在 -> 返回实体")
    void getById_found() {
        when(mapper.selectById(1L)).thenReturn(completion);
        assertSame(completion, service.getById(1L));
    }

    @Test
    @DisplayName("getById 不存在 -> 返回 null")
    void getById_notFound() {
        when(mapper.selectById(999L)).thenReturn(null);
        assertNull(service.getById(999L));
    }

    // ==================== list ====================

    @Test
    @DisplayName("list 分页 -> offset=(page-1)*pageSize")
    void list_pagination() {
        when(mapper.selectList("CMP", 100L, 200L, 1, 20, 10)).thenReturn(List.of(completion));
        when(mapper.countList("CMP", 100L, 200L, 1)).thenReturn(50L);

        PageResult<ProCompletion> result = service.list("CMP", 100L, 200L, 1, 3, 10);

        assertEquals(50L, result.getTotal());
        assertEquals(3, result.getPage());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getRows().size());
    }

    @Test
    @DisplayName("list 第一页 -> offset=0")
    void list_firstPage() {
        when(mapper.selectList(null, null, null, null, 0, 10)).thenReturn(List.of());
        when(mapper.countList(null, null, null, null)).thenReturn(0L);

        PageResult<ProCompletion> result = service.list(null, null, null, null, 1, 10);

        assertEquals(1, result.getPage());
        verify(mapper).selectList(null, null, null, null, 0, 10);
    }
}
