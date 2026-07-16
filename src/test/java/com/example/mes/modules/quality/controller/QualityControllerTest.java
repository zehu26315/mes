package com.example.mes.modules.quality.controller;

import com.example.mes.common.exception.GlobalExceptionHandler;
import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspection;
import com.example.mes.modules.quality.service.QualityInspectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QualityController 质检接口")
class QualityControllerTest {

    @Mock
    private QualityInspectionService service;

    private MockMvc mockMvc;

    private QualityInspection inspection;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new QualityController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

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
                .isDeleted(0)
                .build();
    }

    // ==================== POST /api/quality/inspections ====================

    @Test
    @DisplayName("POST /api/quality/inspections → 200 创建质检")
    void create() throws Exception {
        QualityInspection created = QualityInspection.builder()
                .id(1L)
                .reportNo("QR-002")
                .inspectionType("incoming")
                .result(0)
                .sampleQty(0)
                .defectQty(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        when(service.create(any(QualityInspection.class))).thenReturn(created);

        mockMvc.perform(post("/api/quality/inspections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reportNo\":\"QR-002\",\"inspectionType\":\"incoming\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.reportNo").value("QR-002"));
    }

    // ==================== PUT /api/quality/inspections/{id} ====================

    @Test
    @DisplayName("PUT /api/quality/inspections/1 → 200 更新质检")
    void update() throws Exception {
        QualityInspection updated = QualityInspection.builder()
                .id(1L)
                .reportNo("QR-001")
                .inspector("李四")
                .defectQty(3)
                .build();
        when(service.update(any(QualityInspection.class))).thenReturn(updated);

        mockMvc.perform(put("/api/quality/inspections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"inspector\":\"李四\",\"defectQty\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.inspector").value("李四"));
    }

    // ==================== DELETE /api/quality/inspections/{id} ====================

    @Test
    @DisplayName("DELETE /api/quality/inspections/1 → 200 删除")
    void delete() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/quality/inspections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ==================== GET /api/quality/inspections/{id} ====================

    @Test
    @DisplayName("GET /api/quality/inspections/1 → 200 返回质检")
    void getById() throws Exception {
        when(service.getById(1L)).thenReturn(inspection);

        mockMvc.perform(get("/api/quality/inspections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.reportNo").value("QR-001"))
                .andExpect(jsonPath("$.data.inspector").value("张三"));
    }

    // ==================== GET /api/quality/inspections ====================

    @Test
    @DisplayName("GET /api/quality/inspections 分页 → 200 返回分页数据")
    void list() throws Exception {
        PageResult<QualityInspection> pageResult = PageResult.of(1L, 1, 10, List.of(inspection));
        when(service.list(eq("电池"), isNull(), isNull(), eq(1), eq(10))).thenReturn(pageResult);

        mockMvc.perform(get("/api/quality/inspections?keyword=电池&page=1&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.rows.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/quality/inspections 默认分页 → page=1, pageSize=10")
    void list_defaultPagination() throws Exception {
        when(service.list(isNull(), isNull(), isNull(), eq(1), eq(10)))
                .thenReturn(PageResult.of(0L, 1, 10, List.of()));

        mockMvc.perform(get("/api/quality/inspections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ==================== GET /api/quality/stats ====================

    @Test
    @DisplayName("GET /api/quality/stats → 200 返回统计数据")
    void stats() throws Exception {
        Map<String, Object> statsMap = new LinkedHashMap<>();
        statsMap.put("totalInspections", 100);
        statsMap.put("passRate", 0.8);
        statsMap.put("defectCount", 20);
        statsMap.put("totalSampleQty", 1000);
        when(service.stats()).thenReturn(statsMap);

        mockMvc.perform(get("/api/quality/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalInspections").value(100))
                .andExpect(jsonPath("$.data.passRate").value(0.8))
                .andExpect(jsonPath("$.data.defectCount").value(20));
    }
}
