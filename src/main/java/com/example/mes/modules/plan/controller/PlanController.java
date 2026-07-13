package com.example.mes.modules.plan.controller;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.Result;
import com.example.mes.modules.plan.entity.PlanDaily;
import com.example.mes.modules.plan.entity.PlanMps;
import com.example.mes.modules.plan.entity.PlanOrderSplit;
import com.example.mes.modules.plan.entity.PlanSchedule;
import com.example.mes.modules.plan.service.PlanDailyService;
import com.example.mes.modules.plan.service.PlanMpsService;
import com.example.mes.modules.plan.service.PlanOrderSplitService;
import com.example.mes.modules.plan.service.PlanScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanMpsService planMpsService;
    private final PlanDailyService planDailyService;
    private final PlanScheduleService planScheduleService;
    private final PlanOrderSplitService planOrderSplitService;

    // ==================== MPS ====================

    @PostMapping("/mps")
    public Result<PlanMps> createMps(@RequestBody PlanMps planMps) {
        return Result.ok(planMpsService.create(planMps));
    }

    @PutMapping("/mps/{id}")
    public Result<PlanMps> updateMps(@PathVariable Long id, @RequestBody PlanMps planMps) {
        planMps.setId(id);
        return Result.ok(planMpsService.update(planMps));
    }

    @DeleteMapping("/mps/{id}")
    public Result<Void> deleteMps(@PathVariable Long id) {
        planMpsService.delete(id);
        return Result.ok();
    }

    @GetMapping("/mps/{id}")
    public Result<PlanMps> getMpsById(@PathVariable Long id) {
        return Result.ok(planMpsService.getById(id));
    }

    @GetMapping("/mps")
    public Result<PageResult<PlanMps>> listMps(@RequestParam(defaultValue = "") String keyword,
                                                @RequestParam(required = false) Integer status,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(planMpsService.list(keyword, status, page, pageSize));
    }

    // ==================== Daily ====================

    @PostMapping("/daily")
    public Result<PlanDaily> createDaily(@RequestBody PlanDaily planDaily) {
        return Result.ok(planDailyService.create(planDaily));
    }

    @GetMapping("/daily")
    public Result<PageResult<PlanDaily>> listDaily(@RequestParam(defaultValue = "") String keyword,
                                                    @RequestParam(required = false) Integer status,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate planDate,
                                                    @RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(planDailyService.list(keyword, status, planDate, page, pageSize));
    }

    // ==================== Schedule ====================

    @PostMapping("/schedules")
    public Result<PlanSchedule> createSchedule(@RequestBody PlanSchedule planSchedule) {
        return Result.ok(planScheduleService.create(planSchedule));
    }

    @GetMapping("/schedules")
    public Result<PageResult<PlanSchedule>> listSchedules(@RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(planScheduleService.list(page, pageSize));
    }

    // ==================== Order Split ====================

    @PostMapping("/splits")
    public Result<PlanOrderSplit> createSplit(@RequestBody PlanOrderSplit planOrderSplit) {
        return Result.ok(planOrderSplitService.create(planOrderSplit));
    }

    @GetMapping("/splits")
    public Result<PageResult<PlanOrderSplit>> listSplits(@RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(planOrderSplitService.list(page, pageSize));
    }
}
