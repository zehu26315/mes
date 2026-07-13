package com.example.mes.modules.report.service.impl;

import com.example.mes.modules.equipment.entity.EquipEnergyRecord;
import com.example.mes.modules.equipment.entity.EquipOeeRecord;
import com.example.mes.modules.equipment.mapper.EquipEnergyRecordMapper;
import com.example.mes.modules.equipment.mapper.EquipOeeRecordMapper;
import com.example.mes.modules.production.entity.ProdReporting;
import com.example.mes.modules.production.entity.ProdWip;
import com.example.mes.modules.production.entity.ProdWorkOrder;
import com.example.mes.modules.production.mapper.ProdReportingMapper;
import com.example.mes.modules.production.mapper.ProdWipMapper;
import com.example.mes.modules.production.mapper.ProdWorkOrderMapper;
import com.example.mes.modules.quality.entity.QualityDefectRecord;
import com.example.mes.modules.quality.entity.QualityInspectionOrder;
import com.example.mes.modules.quality.mapper.QualityDefectRecordMapper;
import com.example.mes.modules.quality.mapper.QualityInspectionOrderMapper;
import com.example.mes.modules.report.service.ReportService;
import com.example.mes.modules.trace.entity.TraceMaterialBatch;
import com.example.mes.modules.trace.entity.TraceProduct;
import com.example.mes.modules.trace.entity.TraceProductMaterial;
import com.example.mes.modules.trace.mapper.TraceMaterialBatchMapper;
import com.example.mes.modules.trace.mapper.TraceProductMapper;
import com.example.mes.modules.trace.mapper.TraceProductMaterialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final int PAGE_SIZE = 1000;
    private static final int IN_PROGRESS_STATUS = 2;

    private final ProdWorkOrderMapper prodWorkOrderMapper;
    private final ProdReportingMapper prodReportingMapper;
    private final EquipOeeRecordMapper equipOeeRecordMapper;
    private final EquipEnergyRecordMapper equipEnergyRecordMapper;
    private final QualityDefectRecordMapper qualityDefectRecordMapper;
    private final TraceProductMapper traceProductMapper;
    private final TraceMaterialBatchMapper traceMaterialBatchMapper;
    private final TraceProductMaterialMapper traceProductMaterialMapper;
    private final ProdWipMapper prodWipMapper;
    private final QualityInspectionOrderMapper qualityInspectionOrderMapper;

    // ==================== Output Report ====================

    @Override
    public List<Map<String, Object>> getOutputReport(String startDate, String endDate) {
        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate).plusDays(1).atStartOfDay();

        List<ProdReporting> allReports = fetchAllReporting(start, end);

        // Group by processCode and aggregate
        Map<String, Map<String, Object>> aggregated = new LinkedHashMap<>();

        for (ProdReporting r : allReports) {
            String processCode = r.getProcessCode() != null ? r.getProcessCode() : "UNKNOWN";
            Map<String, Object> entry = aggregated.computeIfAbsent(processCode, k -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("processCode", processCode);
                m.put("processName", r.getProcessName());
                m.put("totalQty", BigDecimal.ZERO);
                m.put("goodQty", BigDecimal.ZERO);
                m.put("defectQty", BigDecimal.ZERO);
                return m;
            });
            entry.put("totalQty", ((BigDecimal) entry.get("totalQty")).add(Optional.ofNullable(r.getTotalQty()).orElse(BigDecimal.ZERO)));
            entry.put("goodQty", ((BigDecimal) entry.get("goodQty")).add(Optional.ofNullable(r.getGoodQty()).orElse(BigDecimal.ZERO)));
            entry.put("defectQty", ((BigDecimal) entry.get("defectQty")).add(Optional.ofNullable(r.getDefectQty()).orElse(BigDecimal.ZERO)));
        }

        return new ArrayList<>(aggregated.values());
    }

    // ==================== Defect Report ====================

    @Override
    public List<Map<String, Object>> getDefectReport(String startDate, String endDate) {
        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate).plusDays(1).atStartOfDay();

        List<QualityDefectRecord> allRecords = fetchAllDefectRecords(start, end);

        // Group by defectType
        Map<String, Map<String, Object>> aggregated = new LinkedHashMap<>();

        for (QualityDefectRecord r : allRecords) {
            String defectType = r.getDefectType() != null ? r.getDefectType() : "UNKNOWN";
            Map<String, Object> entry = aggregated.computeIfAbsent(defectType, k -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("defectType", defectType);
                m.put("count", 0L);
                m.put("defectQty", 0);
                return m;
            });
            entry.put("count", (long) entry.get("count") + 1);
            int currentQty = (int) entry.get("defectQty");
            entry.put("defectQty", currentQty + (r.getDefectQty() != null ? r.getDefectQty() : 0));
        }

        return new ArrayList<>(aggregated.values());
    }

    // ==================== OEE Report ====================

    @Override
    public List<Map<String, Object>> getOeeReport(String startDate, String endDate, Long equipId) {
        List<EquipOeeRecord> records = equipOeeRecordMapper.selectList(equipId, startDate, endDate, 0, 10000);

        if (records.isEmpty()) {
            return Collections.emptyList();
        }

        BigDecimal avgAvailability = avg(records, EquipOeeRecord::getAvailability);
        BigDecimal avgPerformance = avg(records, EquipOeeRecord::getPerformance);
        BigDecimal avgQuality = avg(records, EquipOeeRecord::getQuality);
        BigDecimal avgOee = avg(records, EquipOeeRecord::getOee);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("availability", avgAvailability);
        result.put("performance", avgPerformance);
        result.put("quality", avgQuality);
        result.put("oee", avgOee);
        result.put("recordCount", records.size());

        return Collections.singletonList(result);
    }

    // ==================== Energy Report ====================

    @Override
    public List<Map<String, Object>> getEnergyReport(String startDate, String endDate) {
        List<EquipEnergyRecord> records = equipEnergyRecordMapper.selectList(null, startDate, endDate, 0, 10000);

        // Group by recordDate and sum energy values
        Map<LocalDate, Map<String, Object>> aggregated = new LinkedHashMap<>();

        for (EquipEnergyRecord r : records) {
            LocalDate date = r.getRecordDate();
            if (date == null) continue;

            Map<String, Object> entry = aggregated.computeIfAbsent(date, d -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("recordDate", d.format(DateTimeFormatter.ISO_LOCAL_DATE));
                m.put("electricity", BigDecimal.ZERO);
                m.put("water", BigDecimal.ZERO);
                m.put("gas", BigDecimal.ZERO);
                m.put("compressedAir", BigDecimal.ZERO);
                m.put("steam", BigDecimal.ZERO);
                return m;
            });
            entry.put("electricity", ((BigDecimal) entry.get("electricity")).add(Optional.ofNullable(r.getElectricity()).orElse(BigDecimal.ZERO)));
            entry.put("water", ((BigDecimal) entry.get("water")).add(Optional.ofNullable(r.getWater()).orElse(BigDecimal.ZERO)));
            entry.put("gas", ((BigDecimal) entry.get("gas")).add(Optional.ofNullable(r.getGas()).orElse(BigDecimal.ZERO)));
            entry.put("compressedAir", ((BigDecimal) entry.get("compressedAir")).add(Optional.ofNullable(r.getCompressedAir()).orElse(BigDecimal.ZERO)));
            entry.put("steam", ((BigDecimal) entry.get("steam")).add(Optional.ofNullable(r.getSteam()).orElse(BigDecimal.ZERO)));
        }

        return aggregated.values().stream()
                .sorted(Comparator.comparing(m -> (String) m.get("recordDate")))
                .collect(Collectors.toList());
    }

    // ==================== Trace by Barcode ====================

    @Override
    public List<Map<String, Object>> traceByBarcode(String barcodeNo) {
        TraceProduct product = traceProductMapper.selectByBarcodeNo(barcodeNo);
        if (product == null) {
            return Collections.emptyList();
        }

        ProdWorkOrder workOrder = (product.getWorkOrderId() != null)
                ? prodWorkOrderMapper.selectById(product.getWorkOrderId())
                : null;

        List<TraceProductMaterial> materials = traceProductMaterialMapper.selectByBarcodeNo(barcodeNo);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("product", product);
        result.put("workOrder", workOrder);
        result.put("materials", materials);

        return Collections.singletonList(result);
    }

    // ==================== Trace by Material Batch ====================

    @Override
    public List<Map<String, Object>> traceMaterialBatch(String batchNo) {
        TraceMaterialBatch materialBatch = traceMaterialBatchMapper.selectByBatchNo(batchNo);
        if (materialBatch == null) {
            return Collections.emptyList();
        }

        List<TraceProductMaterial> productMaterials = traceProductMaterialMapper.selectByMaterialBatchNo(batchNo);

        // Find all related products by their barcodes
        List<TraceProduct> relatedProducts = new ArrayList<>();
        for (TraceProductMaterial pm : productMaterials) {
            if (pm.getBarcodeNo() != null) {
                TraceProduct product = traceProductMapper.selectByBarcodeNo(pm.getBarcodeNo());
                if (product != null) {
                    relatedProducts.add(product);
                }
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("materialBatch", materialBatch);
        result.put("productMaterials", productMaterials);
        result.put("relatedProducts", relatedProducts);

        return Collections.singletonList(result);
    }

    // ==================== Realtime Production ====================

    @Override
    public List<Map<String, Object>> getRealtimeProduction() {
        List<ProdWorkOrder> activeOrders = fetchAllWorkOrdersByStatus(IN_PROGRESS_STATUS);

        List<Map<String, Object>> results = new ArrayList<>();
        for (ProdWorkOrder order : activeOrders) {
            List<ProdReporting> reportingList = fetchAllReportingByWorkOrder(order.getId());
            ProdReporting latestReporting = reportingList.stream()
                    .max(Comparator.comparing(r -> r.getCreateTime() != null ? r.getCreateTime() : LocalDateTime.MIN))
                    .orElse(null);

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("workOrder", order);
            entry.put("latestReporting", latestReporting);
            entry.put("reportingCount", reportingList.size());
            results.add(entry);
        }

        return results;
    }

    // ==================== Workshop Period Report ====================

    @Override
    public List<Map<String, Object>> getWorkshopPeriodReport(String workshop, String period) {
        YearMonth ym = YearMonth.parse(period);
        LocalDateTime periodStart = ym.atDay(1).atStartOfDay();
        LocalDateTime periodEnd = ym.atEndOfMonth().plusDays(1).atStartOfDay();

        List<ProdWorkOrder> allOrders = fetchAllWorkOrders();

        // Filter by workshop and period (using createTime as proxy for date)
        List<ProdWorkOrder> filtered = allOrders.stream()
                .filter(o -> workshop == null || workshop.isEmpty() || Objects.equals(workshop, o.getWorkshop()))
                .filter(o -> {
                    if (o.getStartDate() != null) {
                        LocalDate sd = o.getStartDate();
                        return !sd.isBefore(periodStart.toLocalDate()) && sd.isBefore(periodEnd.toLocalDate());
                    }
                    return false;
                })
                .collect(Collectors.toList());

        BigDecimal totalQty = filtered.stream()
                .map(o -> Optional.ofNullable(o.getQuantity()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCompleted = filtered.stream()
                .map(o -> Optional.ofNullable(o.getCompletedQty()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDefective = filtered.stream()
                .map(o -> Optional.ofNullable(o.getDefectiveQty()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("workshop", workshop);
        result.put("period", period);
        result.put("orderCount", filtered.size());
        result.put("totalQty", totalQty);
        result.put("totalCompleted", totalCompleted);
        result.put("totalDefective", totalDefective);
        result.put("completionRate", totalQty.compareTo(BigDecimal.ZERO) > 0
                ? totalCompleted.divide(totalQty, 4, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        return Collections.singletonList(result);
    }

    // ==================== Pagination helpers ====================

    /**
     * Fetch all ProdReporting records within a date range by paginating through the mapper.
     */
    private List<ProdReporting> fetchAllReporting(LocalDateTime start, LocalDateTime end) {
        List<ProdReporting> all = new ArrayList<>();
        int offset = 0;
        while (true) {
            List<ProdReporting> page = prodReportingMapper.selectList(null, null, offset, PAGE_SIZE);
            if (page.isEmpty()) break;

            for (ProdReporting r : page) {
                if (r.getCreateTime() != null && !r.getCreateTime().isBefore(start) && r.getCreateTime().isBefore(end)) {
                    all.add(r);
                }
            }
            if (page.size() < PAGE_SIZE) break;
            offset += PAGE_SIZE;
        }
        return all;
    }

    /**
     * Fetch all QualityDefectRecord records within a date range by paginating through the mapper.
     */
    private List<QualityDefectRecord> fetchAllDefectRecords(LocalDateTime start, LocalDateTime end) {
        List<QualityDefectRecord> all = new ArrayList<>();
        int offset = 0;
        while (true) {
            List<QualityDefectRecord> page = qualityDefectRecordMapper.selectList(null, null, null, offset, PAGE_SIZE);
            if (page.isEmpty()) break;

            for (QualityDefectRecord r : page) {
                if (r.getCreateTime() != null && !r.getCreateTime().isBefore(start) && r.getCreateTime().isBefore(end)) {
                    all.add(r);
                }
            }
            if (page.size() < PAGE_SIZE) break;
            offset += PAGE_SIZE;
        }
        return all;
    }

    /**
     * Fetch all ProdReporting records for a specific work order.
     */
    private List<ProdReporting> fetchAllReportingByWorkOrder(Long workOrderId) {
        List<ProdReporting> all = new ArrayList<>();
        int offset = 0;
        while (true) {
            List<ProdReporting> page = prodReportingMapper.selectList(workOrderId, null, offset, PAGE_SIZE);
            if (page.isEmpty()) break;
            all.addAll(page);
            if (page.size() < PAGE_SIZE) break;
            offset += PAGE_SIZE;
        }
        return all;
    }

    /**
     * Fetch all work orders with a given status.
     */
    private List<ProdWorkOrder> fetchAllWorkOrdersByStatus(Integer status) {
        List<ProdWorkOrder> all = new ArrayList<>();
        int offset = 0;
        while (true) {
            List<ProdWorkOrder> page = prodWorkOrderMapper.selectList(null, status, offset, PAGE_SIZE);
            if (page.isEmpty()) break;
            all.addAll(page);
            if (page.size() < PAGE_SIZE) break;
            offset += PAGE_SIZE;
        }
        return all;
    }

    /**
     * Fetch all work orders.
     */
    private List<ProdWorkOrder> fetchAllWorkOrders() {
        List<ProdWorkOrder> all = new ArrayList<>();
        int offset = 0;
        while (true) {
            List<ProdWorkOrder> page = prodWorkOrderMapper.selectList(null, null, offset, PAGE_SIZE);
            if (page.isEmpty()) break;
            all.addAll(page);
            if (page.size() < PAGE_SIZE) break;
            offset += PAGE_SIZE;
        }
        return all;
    }

    // ==================== Utility ====================

    @FunctionalInterface
    private interface BigDecimalGetter<T> {
        BigDecimal get(T obj);
    }

    private <T> BigDecimal avg(List<T> items, BigDecimalGetter<T> getter) {
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (T item : items) {
            BigDecimal val = getter.get(item);
            if (val != null) {
                sum = sum.add(val);
                count++;
            }
        }
        return count > 0 ? sum.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
}
