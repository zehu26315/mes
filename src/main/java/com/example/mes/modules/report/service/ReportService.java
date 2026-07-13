package com.example.mes.modules.report.service;

import java.util.List;
import java.util.Map;

public interface ReportService {

    /**
     * Aggregate prod_reporting by processCode for a date range.
     * Each result map contains: processCode, processName, totalQty, goodQty, defectQty
     */
    List<Map<String, Object>> getOutputReport(String startDate, String endDate);

    /**
     * Aggregate quality_defect_record by defectType for a date range.
     * Each result map contains: defectType, count, defectQty
     */
    List<Map<String, Object>> getDefectReport(String startDate, String endDate);

    /**
     * OEE records filtered by date range and optional equipment ID.
     * Each result map contains: availability, performance, quality, oee
     */
    List<Map<String, Object>> getOeeReport(String startDate, String endDate, Long equipId);

    /**
     * Energy records aggregated by date.
     * Each result map contains: recordDate, electricity, water, gas, compressedAir, steam
     */
    List<Map<String, Object>> getEnergyReport(String startDate, String endDate);

    /**
     * Trace a product by barcode.
     * Returns: product, workOrder, materials
     */
    List<Map<String, Object>> traceByBarcode(String barcodeNo);

    /**
     * Trace material batch by batch number.
     * Returns: materialBatch, productMaterials, relatedProducts
     */
    List<Map<String, Object>> traceMaterialBatch(String batchNo);

    /**
     * Current active work orders (status IN_PROGRESS) with latest reporting.
     * Each result map contains: workOrder, latestReporting
     */
    List<Map<String, Object>> getRealtimeProduction();

    /**
     * Aggregate production data by workshop for a period (e.g. "2026-07").
     * Each result map contains workshop-level aggregate statistics.
     */
    List<Map<String, Object>> getWorkshopPeriodReport(String workshop, String period);
}
