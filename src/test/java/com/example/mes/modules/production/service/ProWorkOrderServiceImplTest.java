package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProWorkOrder;
import com.example.mes.modules.production.mapper.ProWorkOrderMapper;
import com.example.mes.modules.production.service.impl.ProWorkOrderServiceImpl;
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
@DisplayName("ProWorkOrderService 生产工单服务")
class ProWorkOrderServiceImplTest {

    @Mock
    private ProWorkOrderMapper mapper;

    @InjectMocks
    private ProWorkOrderServiceImpl service;

    private ProWorkOrder workOrder;

    @BeforeEach
    void setUp() {
        workOrder = ProWorkOrder.builder()
                .id(1L)
                .workOrderNo("WO-20260701-001")
                .materialId(100L)
                .planQuantity(new BigDecimal("1000"))
                .actualQuantity(BigDecimal.ZERO)
                .routeId(10L)
                .sourceType(1)
                .status(1)
                .priority(0)
                .remark("测试工单")
                .createBy(1L)
                .isDeleted(0)
                .build();
    }

    // ==================== create ====================

    @Test
    @DisplayName("create → 设置时间戳 + 默认值 + insert")
    void create_success() {
        ProWorkOrder input = ProWorkOrder.builder()
                .workOrderNo("WO-20260701-002")
                .materialId(200L)
                .status(null)
                .priority(null)
                .build();

        ProWorkOrder result = service.create(input);

        assertNotNull(input.getCreateTime());
        assertNotNull(input.getUpdateTime());
        assertEquals(1, input.getStatus());
        assertEquals(0, input.getPriority());
        verify(mapper).insert(input);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("create → status/priority 已有值时不覆盖")
    void create_preservesExistingValues() {
        ProWorkOrder input = ProWorkOrder.builder()
                .status(2)
                .priority(3)
                .build();

        service.create(input);

        assertEquals(2, input.getStatus());
        assertEquals(3, input.getPriority());
    }

    // ==================== update ====================

    @Test
    @DisplayName("update → null 掉 workOrderNo、createBy 和 createTime → 更新后重查")
    void update_success() {
        ProWorkOrder input = ProWorkOrder.builder()
                .id(1L)
                .workOrderNo("should_be_nulled")
                .createBy(5L)
                .createTime(LocalDateTime.of(2020, 1, 1, 0, 0))
                .remark("更新备注")
                .priority(2)
                .build();

        ProWorkOrder refreshed = ProWorkOrder.builder()
                .id(1L)
                .workOrderNo(null)
                .createBy(null)
                .remark("更新备注")
                .priority(2)
                .build();

        when(mapper.selectById(1L)).thenReturn(refreshed);

        ProWorkOrder result = service.update(input);

        assertNull(input.getWorkOrderNo());
        assertNull(input.getCreateBy());
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
        when(mapper.selectById(1L)).thenReturn(workOrder);
        assertSame(workOrder, service.getById(1L));
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
        when(mapper.selectList("WO", 100L, 1, 1, 0, 20, 10)).thenReturn(List.of(workOrder));
        when(mapper.countList("WO", 100L, 1, 1, 0)).thenReturn(50L);

        PageResult<ProWorkOrder> result = service.list("WO", 100L, 1, 1, 0, 3, 10);

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

        PageResult<ProWorkOrder> result = service.list(null, null, null, null, null, 1, 10);

        assertEquals(1, result.getPage());
        verify(mapper).selectList(null, null, null, null, null, 0, 10);
    }
}
