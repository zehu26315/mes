package com.example.mes.modules.dashboard.service;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    Map<String, Object> getSummary();

    List<Map<String, Object>> getRecentWorkOrders(int limit);

    List<Map<String, Object>> getRecentAlarms(int limit);

    List<Map<String, Object>> getProductionLineStatus();

    Map<String, Object> getTodayStats();

    Map<String, Object> getEquipmentStatusSummary();

    Map<String, Object> getEnergySummary();
}
