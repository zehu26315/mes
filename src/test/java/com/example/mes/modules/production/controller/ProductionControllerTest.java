package com.example.mes.modules.production.controller;

import com.example.mes.common.exception.GlobalExceptionHandler;
import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.*;
import com.example.mes.modules.production.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductionController 生产管理接口")
class ProductionControllerTest {

    @Mock private ProductionOrderService orderService;
    @Mock private ProWorkOrderService workOrderService;
    @Mock private ProTaskService taskService;
    @Mock private ProDispatchService dispatchService;
    @Mock private ProCompletionService completionService;
    @Mock private ProRepairService repairService;
    @Mock private ProReportService reportService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ProductionController(orderService, workOrderService, taskService,
                        dispatchService, completionService, repairService, reportService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // ==================== ProductionOrder ====================

    @Nested
    @DisplayName("生产订单 /api/production/orders")
    class OrderEndpoints {

        @Test @DisplayName("POST → 200 创建订单")
        void create() throws Exception {
            ProductionOrder created = ProductionOrder.builder().id(1L).orderNo("PO-001").productModel("AA-Battery").build();
            when(orderService.create(any())).thenReturn(created);

            mockMvc.perform(post("/api/production/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"orderNo\":\"PO-001\",\"productModel\":\"AA-Battery\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.id").value(1));
        }

        @Test @DisplayName("PUT /1 → 200 更新订单")
        void update() throws Exception {
            ProductionOrder updated = ProductionOrder.builder().id(1L).remark("更新备注").build();
            when(orderService.update(any())).thenReturn(updated);

            mockMvc.perform(put("/api/production/orders/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"remark\":\"更新备注\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("DELETE /1 → 200 删除")
        void deleteOrder() throws Exception {
            doNothing().when(orderService).delete(1L);
            mockMvc.perform(delete("/api/production/orders/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("GET /1 → 200 返回订单")
        void getById() throws Exception {
            ProductionOrder order = ProductionOrder.builder().id(1L).orderNo("PO-001").productModel("AA-Battery").build();
            when(orderService.getById(1L)).thenReturn(order);

            mockMvc.perform(get("/api/production/orders/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.orderNo").value("PO-001"));
        }

        @Test @DisplayName("GET 分页 → 200")
        void list() throws Exception {
            when(orderService.list(isNull(), isNull(), eq(1), eq(10)))
                    .thenReturn(PageResult.of(0L, 1, 10, List.of()));

            mockMvc.perform(get("/api/production/orders"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.total").value(0));
        }
    }

    @Test @DisplayName("GET /api/production/dashboard → 200")
    void dashboard() throws Exception {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("stats", List.of());
        data.put("dailyOutput", List.of());
        data.put("lineOutput", List.of());
        when(orderService.dashboardStats()).thenReturn(data);

        mockMvc.perform(get("/api/production/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ==================== ProWorkOrder ====================

    @Nested
    @DisplayName("工单 /api/production/work-orders")
    class WorkOrderEndpoints {

        @Test @DisplayName("POST → 200 创建工单")
        void create() throws Exception {
            ProWorkOrder entity = ProWorkOrder.builder().id(1L).workOrderNo("WO-001").status(1).build();
            when(workOrderService.create(any())).thenReturn(entity);

            mockMvc.perform(post("/api/production/work-orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"workOrderNo\":\"WO-001\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.workOrderNo").value("WO-001"));
        }

        @Test @DisplayName("PUT /1 → 200 更新")
        void update() throws Exception {
            when(workOrderService.update(any())).thenReturn(ProWorkOrder.builder().id(1L).build());
            mockMvc.perform(put("/api/production/work-orders/1")
                            .contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("DELETE /1 → 200")
        void deleteWorkOrder() throws Exception {
            doNothing().when(workOrderService).delete(1L);
            mockMvc.perform(delete("/api/production/work-orders/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("GET /1 → 200")
        void getById() throws Exception {
            when(workOrderService.getById(1L)).thenReturn(ProWorkOrder.builder().id(1L).workOrderNo("WO-001").build());
            mockMvc.perform(get("/api/production/work-orders/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.data.workOrderNo").value("WO-001"));
        }

        @Test @DisplayName("GET 分页 keyword 覆盖 workOrderNo → 200")
        void list_keywordOverrides() throws Exception {
            when(workOrderService.list(eq("search"), isNull(), isNull(), isNull(), isNull(), eq(1), eq(10)))
                    .thenReturn(PageResult.of(1L, 1, 10, List.of()));

            mockMvc.perform(get("/api/production/work-orders?keyword=search&workOrderNo=WO-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("GET 分页 无keyword时用workOrderNo → 200")
        void list_workOrderNoFallback() throws Exception {
            when(workOrderService.list(eq("WO-001"), isNull(), isNull(), isNull(), isNull(), eq(1), eq(10)))
                    .thenReturn(PageResult.of(0L, 1, 10, List.of()));

            mockMvc.perform(get("/api/production/work-orders?workOrderNo=WO-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }
    }

    // ==================== ProTask ====================

    @Nested
    @DisplayName("任务 /api/production/tasks")
    class TaskEndpoints {

        @Test @DisplayName("POST → 200 创建任务")
        void create() throws Exception {
            ProTask entity = ProTask.builder().id(1L).taskNo("TK-001").status(1).build();
            when(taskService.create(any())).thenReturn(entity);

            mockMvc.perform(post("/api/production/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"taskNo\":\"TK-001\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.taskNo").value("TK-001"));
        }

        @Test @DisplayName("PUT /1 → 200")
        void update() throws Exception {
            when(taskService.update(any())).thenReturn(ProTask.builder().id(1L).build());
            mockMvc.perform(put("/api/production/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("DELETE /1 → 200")
        void deleteTask() throws Exception {
            doNothing().when(taskService).delete(1L);
            mockMvc.perform(delete("/api/production/tasks/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("GET /1 → 200")
        void getById() throws Exception {
            when(taskService.getById(1L)).thenReturn(ProTask.builder().id(1L).taskNo("TK-001").build());
            mockMvc.perform(get("/api/production/tasks/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.data.taskNo").value("TK-001"));
        }

        @Test @DisplayName("GET 分页 keyword 覆盖 taskNo")
        void list_keywordOverrides() throws Exception {
            when(taskService.list(eq("search"), isNull(), isNull(), isNull(), isNull(), eq(1), eq(10)))
                    .thenReturn(PageResult.of(0L, 1, 10, List.of()));

            mockMvc.perform(get("/api/production/tasks?keyword=search&taskNo=TK-001"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }
    }

    // ==================== ProDispatch ====================

    @Nested
    @DisplayName("派工 /api/production/dispatches")
    class DispatchEndpoints {

        @Test @DisplayName("POST → 200 创建派工")
        void create() throws Exception {
            ProDispatch entity = ProDispatch.builder().id(1L).dispatchNo("DP-001").status(1).build();
            when(dispatchService.create(any())).thenReturn(entity);

            mockMvc.perform(post("/api/production/dispatches")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"dispatchNo\":\"DP-001\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.dispatchNo").value("DP-001"));
        }

        @Test @DisplayName("PUT /1 → 200")
        void update() throws Exception {
            when(dispatchService.update(any())).thenReturn(ProDispatch.builder().id(1L).build());
            mockMvc.perform(put("/api/production/dispatches/1")
                            .contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("DELETE /1 → 200")
        void deleteDispatch() throws Exception {
            doNothing().when(dispatchService).delete(1L);
            mockMvc.perform(delete("/api/production/dispatches/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("GET /1 → 200")
        void getById() throws Exception {
            when(dispatchService.getById(1L)).thenReturn(ProDispatch.builder().id(1L).dispatchNo("DP-001").build());
            mockMvc.perform(get("/api/production/dispatches/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.data.dispatchNo").value("DP-001"));
        }

        @Test @DisplayName("GET 分页 keyword 覆盖 dispatchNo")
        void list_keywordOverrides() throws Exception {
            when(dispatchService.list(eq("search"), isNull(), isNull(), isNull(), isNull(), eq(1), eq(10)))
                    .thenReturn(PageResult.of(0L, 1, 10, List.of()));

            mockMvc.perform(get("/api/production/dispatches?keyword=search&dispatchNo=DP-001"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }
    }

    // ==================== ProCompletion ====================

    @Nested
    @DisplayName("完工 /api/production/completions")
    class CompletionEndpoints {

        @Test @DisplayName("POST → 200 创建完工记录")
        void create() throws Exception {
            ProCompletion entity = ProCompletion.builder().id(1L).completionNo("CP-001").status(1).build();
            when(completionService.create(any())).thenReturn(entity);

            mockMvc.perform(post("/api/production/completions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"completionNo\":\"CP-001\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.completionNo").value("CP-001"));
        }

        @Test @DisplayName("PUT /1 → 200")
        void update() throws Exception {
            when(completionService.update(any())).thenReturn(ProCompletion.builder().id(1L).build());
            mockMvc.perform(put("/api/production/completions/1")
                            .contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("DELETE /1 → 200")
        void deleteCompletion() throws Exception {
            doNothing().when(completionService).delete(1L);
            mockMvc.perform(delete("/api/production/completions/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("GET /1 → 200")
        void getById() throws Exception {
            when(completionService.getById(1L)).thenReturn(ProCompletion.builder().id(1L).completionNo("CP-001").build());
            mockMvc.perform(get("/api/production/completions/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.data.completionNo").value("CP-001"));
        }

        @Test @DisplayName("GET 分页 keyword 覆盖 completionNo")
        void list_keywordOverrides() throws Exception {
            when(completionService.list(eq("search"), isNull(), isNull(), isNull(), eq(1), eq(10)))
                    .thenReturn(PageResult.of(0L, 1, 10, List.of()));

            mockMvc.perform(get("/api/production/completions?keyword=search&completionNo=CP-001"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }
    }

    // ==================== ProRepair ====================

    @Nested
    @DisplayName("返修 /api/production/repairs")
    class RepairEndpoints {

        @Test @DisplayName("POST → 200 创建返修记录")
        void create() throws Exception {
            ProRepair entity = ProRepair.builder().id(1L).repairNo("RP-001").status(1).build();
            when(repairService.create(any())).thenReturn(entity);

            mockMvc.perform(post("/api/production/repairs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"repairNo\":\"RP-001\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.repairNo").value("RP-001"));
        }

        @Test @DisplayName("PUT /1 → 200")
        void update() throws Exception {
            when(repairService.update(any())).thenReturn(ProRepair.builder().id(1L).build());
            mockMvc.perform(put("/api/production/repairs/1")
                            .contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("DELETE /1 → 200")
        void deleteRepair() throws Exception {
            doNothing().when(repairService).delete(1L);
            mockMvc.perform(delete("/api/production/repairs/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("GET /1 → 200")
        void getById() throws Exception {
            when(repairService.getById(1L)).thenReturn(ProRepair.builder().id(1L).repairNo("RP-001").build());
            mockMvc.perform(get("/api/production/repairs/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.data.repairNo").value("RP-001"));
        }

        @Test @DisplayName("GET 分页 keyword 覆盖 repairNo")
        void list_keywordOverrides() throws Exception {
            when(repairService.list(eq("search"), isNull(), isNull(), isNull(), eq(1), eq(10)))
                    .thenReturn(PageResult.of(0L, 1, 10, List.of()));

            mockMvc.perform(get("/api/production/repairs?keyword=search&repairNo=RP-001"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }
    }

    // ==================== ProReport ====================

    @Nested
    @DisplayName("报工 /api/production/reports")
    class ReportEndpoints {

        @Test @DisplayName("POST → 200 创建报工")
        void create() throws Exception {
            ProReport entity = ProReport.builder().id(1L).reportNo("WR20260701080000000").reportType(1)
                    .reportQuantity(BigDecimal.valueOf(100)).build();
            when(reportService.create(any())).thenReturn(entity);

            mockMvc.perform(post("/api/production/reports")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"reportType\":1,\"reportQuantity\":100}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.data.reportNo").value("WR20260701080000000"));
        }

        @Test @DisplayName("PUT /1 → 200")
        void update() throws Exception {
            when(reportService.update(any())).thenReturn(ProReport.builder().id(1L).build());
            mockMvc.perform(put("/api/production/reports/1")
                            .contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("DELETE /1 → 200")
        void deleteReport() throws Exception {
            doNothing().when(reportService).delete(1L);
            mockMvc.perform(delete("/api/production/reports/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        }

        @Test @DisplayName("GET /1 → 200")
        void getById() throws Exception {
            when(reportService.getById(1L)).thenReturn(ProReport.builder().id(1L).reportNo("WR001").build());
            mockMvc.perform(get("/api/production/reports/1"))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.data.reportNo").value("WR001"));
        }

        @Test @DisplayName("GET 分页 → 200")
        void list() throws Exception {
            when(reportService.list(isNull(), isNull(), isNull(), eq(1), eq(10)))
                    .thenReturn(PageResult.of(0L, 1, 10, List.of()));

            mockMvc.perform(get("/api/production/reports"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200));
        }
    }
}
