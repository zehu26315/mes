package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProductionOrder;
import com.example.mes.modules.production.mapper.ProductionOrderMapper;
import com.example.mes.modules.production.service.impl.ProductionOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductionOrderService 生产订单服务")
class ProductionOrderServiceImplTest {

    @Mock
    private ProductionOrderMapper mapper;

    @InjectMocks
    private ProductionOrderServiceImpl service;

    private ProductionOrder order;

    @BeforeEach
    void setUp() {
        order = ProductionOrder.builder()
                .id(1L)
                .orderNo("PO-20260701-001")
                .productModel("AA-Battery")
                .batchNo("B-20260701")
                .plannedQty(10000)
                .completedQty(5000)
                .productionLine("L1")
                .status(1)
                .deliveryDate(LocalDate.of(2026, 7, 15))
                .createBy("admin")
                .remark("紧急订单")
                .isDeleted(0)
                .build();
    }

    // ==================== create ====================

    @Test
    @DisplayName("create → 设置时间戳 + 默认值(status/plannedQty/completedQty=0) + insert")
    void create_success() {
        ProductionOrder input = ProductionOrder.builder()
                .orderNo("PO-002")
                .productModel("AAA-Battery")
                .status(null)
                .plannedQty(null)
                .completedQty(null)
                .build();

        ProductionOrder result = service.create(input);

        assertNotNull(input.getCreateTime());
        assertNotNull(input.getUpdateTime());
        assertEquals(0, input.getStatus());
        assertEquals(0, input.getPlannedQty());
        assertEquals(0, input.getCompletedQty());
        verify(mapper).insert(input);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("create → status/plannedQty/completedQty 已有值时不覆盖")
    void create_preservesExistingValues() {
        ProductionOrder input = ProductionOrder.builder()
                .status(2)
                .plannedQty(5000)
                .completedQty(2000)
                .build();

        service.create(input);

        assertEquals(2, input.getStatus());
        assertEquals(5000, input.getPlannedQty());
        assertEquals(2000, input.getCompletedQty());
    }

    // ==================== update ====================

    @Test
    @DisplayName("update → null 掉 orderNo/createBy/createTime → 更新后重查")
    void update_success() {
        ProductionOrder input = ProductionOrder.builder()
                .id(1L)
                .orderNo("should_be_nulled")
                .createBy("should_be_nulled")
                .createTime(LocalDateTime.of(2020, 1, 1, 0, 0))
                .remark("更新备注")
                .build();

        ProductionOrder refreshed = ProductionOrder.builder()
                .id(1L)
                .orderNo(null)
                .createBy(null)
                .remark("更新备注")
                .build();

        when(mapper.selectById(1L)).thenReturn(refreshed);

        ProductionOrder result = service.update(input);

        assertNull(input.getOrderNo());
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
    @DisplayName("getById 存在 → 返回订单")
    void getById_found() {
        when(mapper.selectById(1L)).thenReturn(order);
        assertSame(order, service.getById(1L));
    }

    @Test
    @DisplayName("getById 不存在 → 返回 null")
    void getById_notFound() {
        when(mapper.selectById(999L)).thenReturn(null);
        assertNull(service.getById(999L));
    }

    // ==================== list ====================

    @Test
    @DisplayName("list 分页 → offset=(page-1)*pageSize, PageResult 正确")
    void list_pagination() {
        when(mapper.selectList("电池", 1, 20, 10)).thenReturn(List.of(order));
        when(mapper.countList("电池", 1)).thenReturn(50L);

        PageResult<ProductionOrder> result = service.list("电池", 1, 3, 10);

        assertEquals(50L, result.getTotal());
        assertEquals(3, result.getPage());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getRows().size());
    }

    @Test
    @DisplayName("list 第一页 → offset=0")
    void list_firstPage() {
        when(mapper.selectList(null, null, 0, 10)).thenReturn(List.of());
        when(mapper.countList(null, null)).thenReturn(0L);

        PageResult<ProductionOrder> result = service.list(null, null, 1, 10);

        assertEquals(1, result.getPage());
        verify(mapper).selectList(null, null, 0, 10);
    }

    // ==================== dashboardStats ====================

    @Test
    @DisplayName("dashboardStats → 聚合三组查询 → status/dailyOutput/lineOutput")
    void dashboardStats_success() {
        // Use mutable maps because dashboardStats() calls Map.remove()
        List<Map<String, Object>> statusStats = List.of(
                mutableMap("status", 0, "count", 5L),
                mutableMap("status", 1, "count", 10L),
                mutableMap("status", 2, "count", 8L)
        );

        List<Map<String, Object>> dailyOutput = List.of(
                mutableMap("date", "2026-07-01", "output", 500L),
                mutableMap("date", "2026-07-02", "output", 600L)
        );

        List<Map<String, Object>> lineOutput = List.of(
                mutableMap("name", "L1", "value", 300L),
                mutableMap("name", "L2", "value", 400L)
        );

        when(mapper.dashboardStatusStats()).thenReturn(statusStats);
        when(mapper.dashboardDailyOutput()).thenReturn(dailyOutput);
        when(mapper.dashboardLineOutput()).thenReturn(lineOutput);

        Map<String, Object> result = service.dashboardStats();

        // stats list
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> stats = (List<Map<String, Object>>) result.get("stats");
        assertEquals(3, stats.size());
        assertEquals(5, stats.get(0).get("cnt"));
        assertEquals(10, stats.get(1).get("cnt"));
        assertEquals(8, stats.get(2).get("cnt"));

        // dailyOutput: date remapped to day
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> daily = (List<Map<String, Object>>) result.get("dailyOutput");
        assertEquals("2026-07-01", daily.get(0).get("day"));
        assertEquals(500, daily.get(0).get("output"));
        assertNull(daily.get(0).get("date")); // original key removed

        // lineOutput: name→line, value→output
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> lines = (List<Map<String, Object>>) result.get("lineOutput");
        assertEquals("L1", lines.get(0).get("line"));
        assertEquals(300, lines.get(0).get("output"));
        assertNull(lines.get(0).get("name"));  // original key removed
        assertNull(lines.get(0).get("value")); // original key removed
    }

    @Test
    @DisplayName("dashboardStats 空数据 → 返回空列表")
    void dashboardStats_empty() {
        when(mapper.dashboardStatusStats()).thenReturn(List.of());
        when(mapper.dashboardDailyOutput()).thenReturn(List.of());
        when(mapper.dashboardLineOutput()).thenReturn(List.of());

        Map<String, Object> result = service.dashboardStats();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> stats = (List<Map<String, Object>>) result.get("stats");
        assertTrue(stats.isEmpty());
    }

    @Test
    @DisplayName("dashboardStats status=null → 计入 pending")
    void dashboardStats_nullStatus() {
        java.util.Map<String, Object> row = new java.util.HashMap<>();
        row.put("status", null);
        row.put("count", 3L);
        List<Map<String, Object>> statusStats = List.of(row);
        when(mapper.dashboardStatusStats()).thenReturn(statusStats);
        when(mapper.dashboardDailyOutput()).thenReturn(List.of());
        when(mapper.dashboardLineOutput()).thenReturn(List.of());

        Map<String, Object> result = service.dashboardStats();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> stats = (List<Map<String, Object>>) result.get("stats");
        assertEquals(3, stats.get(0).get("cnt"));
    }

    /** Helper: creates a mutable HashMap so dashboardStats() can call Map.remove(). */
    private static java.util.Map<String, Object> mutableMap(String k1, Object v1, String k2, Object v2) {
        java.util.Map<String, Object> m = new java.util.HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        return m;
    }
}
