package com.example.mes.modules.dashboard.service.impl;

import com.example.mes.config.CacheNames;
import com.example.mes.modules.andon.entity.AndonRecord;
import com.example.mes.modules.andon.mapper.AndonRecordMapper;
import com.example.mes.modules.dashboard.service.DashboardService;
import com.example.mes.modules.equipment.entity.EquipAsset;
import com.example.mes.modules.equipment.entity.EquipEnergyRecord;
import com.example.mes.modules.equipment.entity.EquipOeeRecord;
import com.example.mes.modules.equipment.mapper.EquipAssetMapper;
import com.example.mes.modules.equipment.mapper.EquipEnergyRecordMapper;
import com.example.mes.modules.equipment.mapper.EquipOeeRecordMapper;
import com.example.mes.modules.material.mapper.MatInventoryMapper;
import com.example.mes.modules.plan.mapper.PlanMpsMapper;
import com.example.mes.modules.production.entity.ProdReporting;
import com.example.mes.modules.production.entity.ProdWip;
import com.example.mes.modules.production.entity.ProdWorkOrder;
import com.example.mes.modules.production.mapper.ProdReportingMapper;
import com.example.mes.modules.production.mapper.ProdWipMapper;
import com.example.mes.modules.production.mapper.ProdWorkOrderMapper;
import com.example.mes.modules.quality.entity.QualityInspectionOrder;
import com.example.mes.modules.quality.mapper.QualityInspectionOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dashboard aggregation service — reads across multiple modules to produce
 * summary statistics. All aggregation happens in Java so no new mapper XML
 * is needed.
 *
 * Work order status conventions assumed by this service:
 *   1 – Created / Pending
 *   2 – In Production
 *   3 – Completed
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private static final int MAX_FETCH = 10_000;
    private static final int RECENT_OEE_LIMIT = 30;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ProdWorkOrderMapper prodWorkOrderMapper;
    private final ProdWipMapper prodWipMapper;
    private final EquipAssetMapper equipAssetMapper;
    private final EquipOeeRecordMapper equipOeeRecordMapper;
    private final AndonRecordMapper andonRecordMapper;
    private final QualityInspectionOrderMapper qualityInspectionOrderMapper;
    private final PlanMpsMapper planMpsMapper;
    private final MatInventoryMapper matInventoryMapper;
    private final EquipEnergyRecordMapper equipEnergyRecordMapper;
    private final ProdReportingMapper prodReportingMapper;

    // -------------------------------------------------------------------
    // 1. Summary
    // -------------------------------------------------------------------

    @Override
    @Cacheable(value = CacheNames.DASHBOARD, key = "#root.method.name")
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();

        // ---- counts ----
        long totalWorkOrders = prodWorkOrderMapper.countList(null, null);
        long completedWorkOrders = prodWorkOrderMapper.countList(null, 3);
        long activeAlarms = andonRecordMapper.selectActive().size();
        long totalEquipment = equipAssetMapper.countList(null, null, null, null);

        // ---- WIP total ----
        List<ProdWip> allWip = prodWipMapper.selectList(0, MAX_FETCH);
        BigDecimal wipTotal = allWip.stream()
                .map(w -> w.getWipQty() != null ? w.getWipQty() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ---- low stock ----
        long lowStockCount = matInventoryMapper.selectLowStock(0, MAX_FETCH).size();

        // ---- OEE / yield from recent records ----
        List<EquipOeeRecord> recentOee = equipOeeRecordMapper.selectList(
                null, null, null, 0, RECENT_OEE_LIMIT);
        BigDecimal oeeAverage = averageBigDecimal(recentOee, EquipOeeRecord::getOee);
        BigDecimal yieldRate = averageBigDecimal(recentOee, EquipOeeRecord::getQuality);

        summary.put("totalWorkOrders", totalWorkOrders);
        summary.put("completedWorkOrders", completedWorkOrders);
        summary.put("activeAlarms", activeAlarms);
        summary.put("totalEquipment", totalEquipment);
        summary.put("oeeAverage", oeeAverage);
        summary.put("yieldRate", yieldRate);
        summary.put("wipTotal", wipTotal);
        summary.put("lowStockCount", lowStockCount);
        return summary;
    }

    // -------------------------------------------------------------------
    // 2. Recent work orders
    // -------------------------------------------------------------------

    @Override
    @Cacheable(value = CacheNames.DASHBOARD, key = "#root.method.name + ':' + #limit")
    public List<Map<String, Object>> getRecentWorkOrders(int limit) {
        List<ProdWorkOrder> orders = prodWorkOrderMapper.selectList(null, null, 0, limit);
        return orders.stream().map(o -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", o.getId());
            m.put("orderNo", o.getOrderNo());
            m.put("productName", o.getProductName());
            m.put("quantity", o.getQuantity());
            m.put("completedQty", o.getCompletedQty());
            m.put("status", o.getStatus());
            m.put("productionLine", o.getProductionLine());
            m.put("createTime", o.getCreateTime());
            return m;
        }).collect(Collectors.toList());
    }

    // -------------------------------------------------------------------
    // 3. Recent alarms
    // -------------------------------------------------------------------

    @Override
    @Cacheable(value = CacheNames.DASHBOARD, key = "#root.method.name + ':' + #limit")
    public List<Map<String, Object>> getRecentAlarms(int limit) {
        List<AndonRecord> alarms = andonRecordMapper.selectList(
                null, null, null, null, null, null, 0, limit);
        return alarms.stream().map(a -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", a.getId());
            m.put("andonTypeId", a.getAndonTypeId());
            m.put("workCenter", a.getWorkCenter());
            m.put("productionLine", a.getProductionLine());
            m.put("status", a.getStatus());
            m.put("severity", a.getSeverity());
            m.put("description", a.getDescription());
            m.put("reportTime", a.getReportTime());
            m.put("duration", a.getDuration());
            return m;
        }).collect(Collectors.toList());
    }

    // -------------------------------------------------------------------
    // 4. Production line status
    // -------------------------------------------------------------------

    @Override
    @Cacheable(value = CacheNames.DASHBOARD, key = "#root.method.name")
    public List<Map<String, Object>> getProductionLineStatus() {
        // Fetch all data once
        List<EquipAsset> allAssets = equipAssetMapper.selectList(null, null, null, null, 0, MAX_FETCH);
        List<ProdWorkOrder> activeOrders = prodWorkOrderMapper.selectList(null, 2, 0, MAX_FETCH);
        List<ProdWip> allWip = prodWipMapper.selectList(0, MAX_FETCH);
        List<AndonRecord> activeAlarms = andonRecordMapper.selectActive();

        // Build lookups
        Map<String, Long> activeOrderCountByLine = activeOrders.stream()
                .filter(o -> o.getProductionLine() != null)
                .collect(Collectors.groupingBy(ProdWorkOrder::getProductionLine, Collectors.counting()));

        // workCenter → productionLine mapping from assets
        Map<String, String> wcToLine = allAssets.stream()
                .filter(a -> a.getWorkCenter() != null && a.getProductionLine() != null)
                .collect(Collectors.toMap(EquipAsset::getWorkCenter,
                        EquipAsset::getProductionLine, (e1, e2) -> e1));

        // WIP sum per productionLine (map workCenter → productionLine)
        Map<String, BigDecimal> wipQtyByLine = new HashMap<>();
        for (ProdWip wip : allWip) {
            if (wip.getWorkCenter() == null) continue;
            String line = wcToLine.get(wip.getWorkCenter());
            if (line != null) {
                wipQtyByLine.merge(line,
                        wip.getWipQty() != null ? wip.getWipQty() : BigDecimal.ZERO,
                        BigDecimal::add);
            }
        }

        // Active alarm count per productionLine
        Map<String, Long> alarmCountByLine = activeAlarms.stream()
                .filter(a -> a.getProductionLine() != null)
                .collect(Collectors.groupingBy(AndonRecord::getProductionLine, Collectors.counting()));

        // Group assets by productionLine
        Map<String, List<EquipAsset>> assetsByLine = allAssets.stream()
                .filter(a -> a.getProductionLine() != null)
                .collect(Collectors.groupingBy(EquipAsset::getProductionLine));

        // Build result per productionLine
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<EquipAsset>> entry : assetsByLine.entrySet()) {
            String line = entry.getKey();
            List<EquipAsset> lineAssets = entry.getValue();

            long totalEquip = lineAssets.size();
            long runningEquip = lineAssets.stream()
                    .filter(a -> "RUNNING".equalsIgnoreCase(a.getStatus()))
                    .count();

            Map<String, Object> lineMap = new LinkedHashMap<>();
            lineMap.put("lineId", line);
            lineMap.put("activeOrders", activeOrderCountByLine.getOrDefault(line, 0L));
            lineMap.put("runningEquipment", runningEquip);
            lineMap.put("totalEquipment", totalEquip);
            lineMap.put("wipQty", wipQtyByLine.getOrDefault(line, BigDecimal.ZERO));
            lineMap.put("activeAlarms", alarmCountByLine.getOrDefault(line, 0L));
            result.add(lineMap);
        }

        // Sort by line name for consistent output
        result.sort(Comparator.comparing(m -> (String) m.get("lineId")));
        return result;
    }

    // -------------------------------------------------------------------
    // 5. Today stats
    // -------------------------------------------------------------------

    @Override
    @Cacheable(value = CacheNames.DASHBOARD, key = "#root.method.name")
    public Map<String, Object> getTodayStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(LocalTime.MAX);

        // Fetch completions and filter by today in Java
        // (mapper has no date filter; production would add a date-scoped query)
        List<ProdReporting> allCompletions = prodReportingMapper.selectCompletions(0, MAX_FETCH);
        List<ProdReporting> todayReports = allCompletions.stream()
                .filter(r -> r.getCreateTime() != null
                        && !r.getCreateTime().isBefore(todayStart)
                        && !r.getCreateTime().isAfter(todayEnd))
                .collect(Collectors.toList());

        BigDecimal totalGood = todayReports.stream()
                .map(r -> r.getGoodQty() != null ? r.getGoodQty() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDefect = todayReports.stream()
                .map(r -> r.getDefectQty() != null ? r.getDefectQty() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // defectRate = defect / (good + defect)
        BigDecimal totalOutput = totalGood.add(totalDefect);
        BigDecimal defectRate = BigDecimal.ZERO;
        if (totalOutput.compareTo(BigDecimal.ZERO) > 0) {
            defectRate = totalDefect.divide(totalOutput, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        // Inspection pass rate — recent inspection orders filtered by today
        List<QualityInspectionOrder> allInspections = qualityInspectionOrderMapper.selectList(
                null, null, null, 0, MAX_FETCH);
        List<QualityInspectionOrder> todayInspections = allInspections.stream()
                .filter(o -> o.getInspectionTime() != null
                        && !o.getInspectionTime().isBefore(todayStart)
                        && !o.getInspectionTime().isAfter(todayEnd))
                .collect(Collectors.toList());

        long totalInspections = todayInspections.size();
        long passedInspections = todayInspections.stream()
                .filter(o -> "PASS".equalsIgnoreCase(o.getResult()))
                .count();
        BigDecimal inspectionPassRate = totalInspections > 0
                ? BigDecimal.valueOf(passedInspections)
                        .divide(BigDecimal.valueOf(totalInspections), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        // Active orders (status = 2 = in production)
        long activeOrders = prodWorkOrderMapper.countList(null, 2);

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("todayOutput", totalGood);
        stats.put("defectRate", defectRate);
        stats.put("inspectionPassRate", inspectionPassRate);
        stats.put("activeOrders", activeOrders);
        return stats;
    }

    // -------------------------------------------------------------------
    // 6. Equipment status summary
    // -------------------------------------------------------------------

    @Override
    @Cacheable(value = CacheNames.DASHBOARD, key = "#root.method.name")
    public Map<String, Object> getEquipmentStatusSummary() {
        List<EquipAsset> allAssets = equipAssetMapper.selectList(null, null, null, null, 0, MAX_FETCH);

        Map<String, Long> statusCounts = allAssets.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getStatus() != null ? a.getStatus().toUpperCase() : "UNKNOWN",
                        Collectors.counting()));

        // Ensure all known statuses are present
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("RUNNING", statusCounts.getOrDefault("RUNNING", 0L));
        summary.put("IDLE", statusCounts.getOrDefault("IDLE", 0L));
        summary.put("MAINTENANCE", statusCounts.getOrDefault("MAINTENANCE", 0L));
        summary.put("BROKEN", statusCounts.getOrDefault("BROKEN", 0L));
        // Include any other statuses found
        statusCounts.forEach((k, v) -> {
            if (!summary.containsKey(k)) {
                summary.put(k, v);
            }
        });
        return summary;
    }

    // -------------------------------------------------------------------
    // 7. Energy summary (today)
    // -------------------------------------------------------------------

    @Override
    @Cacheable(value = CacheNames.DASHBOARD, key = "#root.method.name")
    public Map<String, Object> getEnergySummary() {
        String todayStr = LocalDate.now().format(DATE_FMT);

        List<EquipEnergyRecord> todayRecords = equipEnergyRecordMapper.selectList(
                null, todayStr, todayStr, 0, MAX_FETCH);

        BigDecimal electricity = BigDecimal.ZERO;
        BigDecimal water = BigDecimal.ZERO;
        BigDecimal gas = BigDecimal.ZERO;
        BigDecimal compressedAir = BigDecimal.ZERO;
        BigDecimal steam = BigDecimal.ZERO;

        for (EquipEnergyRecord r : todayRecords) {
            if (r.getElectricity() != null) electricity = electricity.add(r.getElectricity());
            if (r.getWater() != null) water = water.add(r.getWater());
            if (r.getGas() != null) gas = gas.add(r.getGas());
            if (r.getCompressedAir() != null) compressedAir = compressedAir.add(r.getCompressedAir());
            if (r.getSteam() != null) steam = steam.add(r.getSteam());
        }

        Map<String, Object> energy = new LinkedHashMap<>();
        energy.put("electricity", electricity);
        energy.put("water", water);
        energy.put("gas", gas);
        energy.put("compressedAir", compressedAir);
        energy.put("steam", steam);
        return energy;
    }

    // -------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------

    private static <T> BigDecimal averageBigDecimal(List<T> items,
                                                     java.util.function.Function<T, BigDecimal> extractor) {
        if (items == null || items.isEmpty()) return BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (T item : items) {
            BigDecimal val = extractor.apply(item);
            if (val != null) {
                sum = sum.add(val);
                count++;
            }
        }
        return count > 0
                ? sum.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
    }
}
