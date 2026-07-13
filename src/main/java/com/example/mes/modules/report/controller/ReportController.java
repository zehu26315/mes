package com.example.mes.modules.report.controller;

import com.example.mes.common.response.Result;
import com.example.mes.modules.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/output")
    public Result<List<Map<String, Object>>> getOutput(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return Result.ok(reportService.getOutputReport(startDate, endDate));
    }

    @GetMapping("/defect")
    public Result<List<Map<String, Object>>> getDefect(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return Result.ok(reportService.getDefectReport(startDate, endDate));
    }

    @GetMapping("/oee")
    public Result<List<Map<String, Object>>> getOee(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) Long equipId) {
        return Result.ok(reportService.getOeeReport(startDate, endDate, equipId));
    }

    @GetMapping("/energy")
    public Result<List<Map<String, Object>>> getEnergy(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return Result.ok(reportService.getEnergyReport(startDate, endDate));
    }

    @GetMapping("/trace/product/{barcode}")
    public Result<List<Map<String, Object>>> traceProduct(@PathVariable String barcode) {
        return Result.ok(reportService.traceByBarcode(barcode));
    }

    @GetMapping("/trace/material/{batchNo}")
    public Result<List<Map<String, Object>>> traceMaterialBatch(@PathVariable String batchNo) {
        return Result.ok(reportService.traceMaterialBatch(batchNo));
    }

    @GetMapping("/realtime")
    public Result<List<Map<String, Object>>> getRealtime() {
        return Result.ok(reportService.getRealtimeProduction());
    }

    @GetMapping("/workshop")
    public Result<List<Map<String, Object>>> getWorkshop(
            @RequestParam String workshop,
            @RequestParam String period) {
        return Result.ok(reportService.getWorkshopPeriodReport(workshop, period));
    }
}
