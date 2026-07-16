package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProRepair;
import com.example.mes.modules.production.mapper.ProRepairMapper;
import com.example.mes.modules.production.service.impl.ProRepairServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProRepairService 返修单服务")
class ProRepairServiceImplTest {

    @Mock
    private ProRepairMapper mapper;

    @InjectMocks
    private ProRepairServiceImpl service;

    private ProRepair repair;

    @BeforeEach
    void setUp() {
        repair = ProRepair.builder()
                .id(1L)
                .repairNo("RP-001")
                .workOrderId(100L)
                .completionId(50L)
                .materialId(200L)
                .barcode("BC-20260715-001")
                .repairQuantity(new BigDecimal("20"))
                .repairReason("外观缺陷")
                .repairType(1)
                .assigneeId(10L)
                .status(1)
                .startTime(LocalDateTime.of(2026, 7, 15, 14, 0))
                .endTime(null)
                .isDeleted(0)
                .build();
    }

    // ==================== create ====================

    @Test
    @DisplayName("create -> 设置时间戳 + status 默认 1 + insert")
    void create_success() {
        ProRepair input = ProRepair.builder()
                .repairNo("RP-002")
                .workOrderId(101L)
                .status(null)
                .build();

        ProRepair result = service.create(input);

        assertNotNull(input.getCreateTime());
        assertNotNull(input.getUpdateTime());
        assertEquals(1, input.getStatus());
        verify(mapper).insert(input);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("create -> status 已有值时不覆盖")
    void create_preservesExistingStatus() {
        ProRepair input = ProRepair.builder()
                .status(2)
                .build();

        service.create(input);

        assertEquals(2, input.getStatus());
    }

    // ==================== update ====================

    @Test
    @DisplayName("update -> null 掉 repairNo 和 createTime -> 更新后重查")
    void update_success() {
        ProRepair input = ProRepair.builder()
                .id(1L)
                .repairNo("should_be_nulled")
                .createTime(LocalDateTime.of(2020, 1, 1, 0, 0))
                .repairQuantity(new BigDecimal("30"))
                .build();

        ProRepair refreshed = ProRepair.builder()
                .id(1L)
                .repairNo(null)
                .repairQuantity(new BigDecimal("30"))
                .build();

        when(mapper.selectById(1L)).thenReturn(refreshed);

        ProRepair result = service.update(input);

        assertNull(input.getRepairNo());
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
        when(mapper.selectById(1L)).thenReturn(repair);
        assertSame(repair, service.getById(1L));
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
        when(mapper.selectList("RP", 100L, 1, 1, 20, 10)).thenReturn(List.of(repair));
        when(mapper.countList("RP", 100L, 1, 1)).thenReturn(30L);

        PageResult<ProRepair> result = service.list("RP", 100L, 1, 1, 3, 10);

        assertEquals(30L, result.getTotal());
        assertEquals(3, result.getPage());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getRows().size());
    }

    @Test
    @DisplayName("list 第一页 -> offset=0")
    void list_firstPage() {
        when(mapper.selectList(null, null, null, null, 0, 10)).thenReturn(List.of());
        when(mapper.countList(null, null, null, null)).thenReturn(0L);

        PageResult<ProRepair> result = service.list(null, null, null, null, 1, 10);

        assertEquals(1, result.getPage());
        verify(mapper).selectList(null, null, null, null, 0, 10);
    }
}
