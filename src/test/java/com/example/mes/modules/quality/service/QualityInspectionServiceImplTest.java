package com.example.mes.modules.quality.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspection;
import com.example.mes.modules.quality.mapper.QualityInspectionMapper;
import com.example.mes.modules.quality.service.impl.QualityInspectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QualityInspectionService 质检服务")
class QualityInspectionServiceImplTest {

    @Mock
    private QualityInspectionMapper mapper;

    @InjectMocks
    private QualityInspectionServiceImpl service;

    private QualityInspection inspection;

    @BeforeEach
    void setUp() {
        inspection = QualityInspection.builder()
                .id(1L)
                .reportNo("QR-001")
                .orderId(100L)
                .productModel("AA-Battery")
                .batchNo("B-20260701")
                .inspectionType("outgoing")
                .sampleQty(50)
                .defectQty(2)
                .result(0)
                .inspector("张三")
                .remark("抽样检查")
                .isDeleted(0)
                .build();
    }

    // ==================== create ====================

    @Test
    @DisplayName("create → 设置时间戳 + 默认值 + insert")
    void create_success() {
        QualityInspection input = QualityInspection.builder()
                .reportNo("QR-002")
                .inspectionType("incoming")
                .result(null)
                .sampleQty(null)
                .defectQty(null)
                .build();

        QualityInspection result = service.create(input);

        assertNotNull(input.getCreateTime());
        assertNotNull(input.getUpdateTime());
        assertEquals(0, input.getResult());
        assertEquals(0, input.getSampleQty());
        assertEquals(0, input.getDefectQty());
        verify(mapper).insert(input);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("create → result/sampleQty/defectQty 已有值时不覆盖")
    void create_preservesExistingValues() {
        QualityInspection input = QualityInspection.builder()
                .result(1)
                .sampleQty(30)
                .defectQty(5)
                .build();

        service.create(input);

        assertEquals(1, input.getResult());
        assertEquals(30, input.getSampleQty());
        assertEquals(5, input.getDefectQty());
    }

    // ==================== update ====================

    @Test
    @DisplayName("update → null 掉 reportNo 和 createTime → 更新后重查")
    void update_success() {
        QualityInspection input = QualityInspection.builder()
                .id(1L)
                .reportNo("should_be_nulled")
                .createTime(LocalDateTime.of(2020, 1, 1, 0, 0))
                .inspector("李四")
                .defectQty(3)
                .build();

        QualityInspection refreshed = QualityInspection.builder()
                .id(1L)
                .reportNo(null)
                .inspector("李四")
                .defectQty(3)
                .build();

        when(mapper.selectById(1L)).thenReturn(refreshed);

        QualityInspection result = service.update(input);

        assertNull(input.getReportNo());
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
        when(mapper.selectById(1L)).thenReturn(inspection);
        assertSame(inspection, service.getById(1L));
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
        when(mapper.selectList("电池", "outgoing", 0, 20, 10)).thenReturn(List.of(inspection));
        when(mapper.countList("电池", "outgoing", 0)).thenReturn(50L);

        PageResult<QualityInspection> result = service.list("电池", "outgoing", 0, 3, 10);

        assertEquals(50L, result.getTotal());
        assertEquals(3, result.getPage());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getRows().size());
    }

    @Test
    @DisplayName("list 第一页 → offset=0")
    void list_firstPage() {
        when(mapper.selectList(null, null, null, 0, 10)).thenReturn(List.of());
        when(mapper.countList(null, null, null)).thenReturn(0L);

        PageResult<QualityInspection> result = service.list(null, null, null, 1, 10);

        assertEquals(1, result.getPage());
        verify(mapper).selectList(null, null, null, 0, 10);
    }

    @Test
    @DisplayName("list 空关键词 → 直接传空字符串给 mapper")
    void list_emptyKeyword() {
        when(mapper.selectList(eq(""), isNull(), isNull(), anyInt(), anyInt())).thenReturn(List.of());
        when(mapper.countList(eq(""), isNull(), isNull())).thenReturn(0L);

        PageResult<QualityInspection> result = service.list("", null, null, 1, 10);

        assertEquals(0L, result.getTotal());
        verify(mapper).selectList(eq(""), isNull(), isNull(), eq(0), eq(10));
    }

    // ==================== stats ====================

    @Test
    @DisplayName("stats → 统计通过率、缺陷数、总抽样量")
    void stats_success() {
        Map<String, Object> row0 = Map.of("result", 0, "count", 80L, "total_qty", 800L);
        Map<String, Object> row1 = Map.of("result", 1, "count", 20L, "total_qty", 200L);
        when(mapper.statsByResult()).thenReturn(List.of(row0, row1));

        Map<String, Object> stats = service.stats();

        assertEquals(100, stats.get("totalInspections"));
        assertEquals(0.8, (Double) stats.get("passRate"), 0.001);
        assertEquals(20, stats.get("defectCount"));
        assertEquals(1000, stats.get("totalSampleQty"));
    }

    @Test
    @DisplayName("stats 空结果 → passRate=0.0")
    void stats_empty() {
        when(mapper.statsByResult()).thenReturn(List.of());

        Map<String, Object> stats = service.stats();

        assertEquals(0, stats.get("totalInspections"));
        assertEquals(0.0, (Double) stats.get("passRate"), 0.001);
        assertEquals(0, stats.get("defectCount"));
        assertEquals(0, stats.get("totalSampleQty"));
    }

    @Test
    @DisplayName("stats 全缺陷 → passRate=0.0")
    void stats_allDefect() {
        Map<String, Object> row1 = Map.of("result", 1, "count", 10L, "total_qty", 100L);
        Map<String, Object> row2 = Map.of("result", 2, "count", 5L, "total_qty", 50L);
        when(mapper.statsByResult()).thenReturn(List.of(row1, row2));

        Map<String, Object> stats = service.stats();

        assertEquals(15, stats.get("totalInspections"));
        assertEquals(0.0, (Double) stats.get("passRate"), 0.001);
        assertEquals(15, stats.get("defectCount"));
    }
}
