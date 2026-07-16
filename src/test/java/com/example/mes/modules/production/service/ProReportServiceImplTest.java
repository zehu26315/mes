package com.example.mes.modules.production.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProReport;
import com.example.mes.modules.production.mapper.ProReportMapper;
import com.example.mes.modules.production.service.impl.ProReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProReportService 报工单服务")
class ProReportServiceImplTest {

    @Mock
    private ProReportMapper mapper;

    @InjectMocks
    private ProReportServiceImpl service;

    private ProReport report;

    @BeforeEach
    void setUp() {
        report = ProReport.builder()
                .id(1L)
                .reportNo("WR20260715143000123")
                .orderId(100L)
                .taskId(200L)
                .operationId(300L)
                .processName("组装")
                .worker("张三")
                .workstation("LINE-01")
                .workerId(10L)
                .reportType(1)
                .reportQuantity(new BigDecimal("500"))
                .qualifiedQuantity(new BigDecimal("480"))
                .defectQuantity(new BigDecimal("20"))
                .defectReason("尺寸偏差")
                .equipmentId(50L)
                .barcode("BC-20260715-100")
                .reportDate(LocalDate.of(2026, 7, 15))
                .shift("白班")
                .reportTime(LocalDateTime.of(2026, 7, 15, 14, 30))
                .isDeleted(0)
                .build();
    }

    // ==================== create ====================

    @Test
    @DisplayName("create -> 设置时间戳 + 默认值 + insert")
    void create_success() {
        ProReport input = ProReport.builder()
                .reportNo("WR20260715143000999")
                .orderId(101L)
                .reportType(null)
                .reportQuantity(null)
                .qualifiedQuantity(null)
                .defectQuantity(null)
                .reportTime(null)
                .build();

        ProReport result = service.create(input);

        assertNotNull(input.getCreateTime());
        assertNotNull(input.getUpdateTime());
        assertNotNull(input.getReportTime());
        assertEquals(1, input.getReportType());
        assertEquals(BigDecimal.ZERO, input.getReportQuantity());
        assertEquals(BigDecimal.ZERO, input.getQualifiedQuantity());
        assertEquals(BigDecimal.ZERO, input.getDefectQuantity());
        verify(mapper).insert(input);
        assertEquals(input, result);
    }

    @Test
    @DisplayName("create -> reportNo 为 null 时自动生成 WR + 17 位时间戳")
    void create_autoGeneratesReportNo() {
        ProReport input = ProReport.builder()
                .orderId(102L)
                .reportNo(null)
                .build();

        service.create(input);

        assertNotNull(input.getReportNo());
        assertTrue(input.getReportNo().startsWith("WR"), "reportNo should start with WR");
        // WR prefix + 17 digits timestamp = 19 characters total
        assertEquals(19, input.getReportNo().length());
    }

    @Test
    @DisplayName("create -> reportNo 为空字符串时自动生成")
    void create_autoGeneratesReportNo_whenEmpty() {
        ProReport input = ProReport.builder()
                .orderId(103L)
                .reportNo("")
                .build();

        service.create(input);

        assertNotNull(input.getReportNo());
        assertTrue(input.getReportNo().startsWith("WR"));
        assertEquals(19, input.getReportNo().length());
    }

    @Test
    @DisplayName("create -> reportNo 已有值时不覆盖")
    void create_preservesExistingReportNo() {
        String existingReportNo = "WR20260715143000999";
        ProReport input = ProReport.builder()
                .reportNo(existingReportNo)
                .orderId(104L)
                .build();

        service.create(input);

        assertEquals(existingReportNo, input.getReportNo());
    }

    @Test
    @DisplayName("create -> reportTime 已有值时不覆盖")
    void create_preservesExistingReportTime() {
        LocalDateTime existingTime = LocalDateTime.of(2026, 7, 14, 10, 0);
        ProReport input = ProReport.builder()
                .reportNo("WR20260715143000123")
                .reportTime(existingTime)
                .build();

        service.create(input);

        assertEquals(existingTime, input.getReportTime());
    }

    @Test
    @DisplayName("create -> reportType/reportQuantity/qualifiedQuantity/defectQuantity 已有值时不覆盖")
    void create_preservesExistingDefaults() {
        ProReport input = ProReport.builder()
                .reportNo("WR20260715143000123")
                .reportType(2)
                .reportQuantity(new BigDecimal("300"))
                .qualifiedQuantity(new BigDecimal("280"))
                .defectQuantity(new BigDecimal("20"))
                .build();

        service.create(input);

        assertEquals(2, input.getReportType());
        assertEquals(new BigDecimal("300"), input.getReportQuantity());
        assertEquals(new BigDecimal("280"), input.getQualifiedQuantity());
        assertEquals(new BigDecimal("20"), input.getDefectQuantity());
    }

    // ==================== update ====================

    @Test
    @DisplayName("update -> null 掉 reportNo 和 createTime -> 更新后重查")
    void update_success() {
        ProReport input = ProReport.builder()
                .id(1L)
                .reportNo("should_be_nulled")
                .createTime(LocalDateTime.of(2020, 1, 1, 0, 0))
                .worker("李四")
                .defectQuantity(new BigDecimal("5"))
                .build();

        ProReport refreshed = ProReport.builder()
                .id(1L)
                .reportNo(null)
                .worker("李四")
                .defectQuantity(new BigDecimal("5"))
                .build();

        when(mapper.selectById(1L)).thenReturn(refreshed);

        ProReport result = service.update(input);

        assertNull(input.getReportNo());
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
        when(mapper.selectById(1L)).thenReturn(report);
        assertSame(report, service.getById(1L));
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
        when(mapper.selectList("电池", 100L, "2026-07-15", 20, 10)).thenReturn(List.of(report));
        when(mapper.countList("电池", 100L, "2026-07-15")).thenReturn(50L);

        PageResult<ProReport> result = service.list("电池", 100L, "2026-07-15", 3, 10);

        assertEquals(50L, result.getTotal());
        assertEquals(3, result.getPage());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getRows().size());
    }

    @Test
    @DisplayName("list 第一页 -> offset=0")
    void list_firstPage() {
        when(mapper.selectList(null, null, null, 0, 10)).thenReturn(List.of());
        when(mapper.countList(null, null, null)).thenReturn(0L);

        PageResult<ProReport> result = service.list(null, null, null, 1, 10);

        assertEquals(1, result.getPage());
        verify(mapper).selectList(null, null, null, 0, 10);
    }
}
