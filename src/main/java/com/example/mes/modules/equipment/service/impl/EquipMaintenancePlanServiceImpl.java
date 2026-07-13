package com.example.mes.modules.equipment.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.equipment.entity.EquipMaintenancePlan;
import com.example.mes.modules.equipment.entity.EquipMaintenanceRecord;
import com.example.mes.modules.equipment.mapper.EquipMaintenancePlanMapper;
import com.example.mes.modules.equipment.mapper.EquipMaintenanceRecordMapper;
import com.example.mes.modules.equipment.service.EquipMaintenancePlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipMaintenancePlanServiceImpl implements EquipMaintenancePlanService {
    private final EquipMaintenancePlanMapper planMapper;
    private final EquipMaintenanceRecordMapper recordMapper;

    @Override @Transactional
    public EquipMaintenancePlan create(EquipMaintenancePlan e) { planMapper.insert(e); return e; }

    @Override public EquipMaintenancePlan getById(Long id) { return planMapper.selectById(id); }

    @Override
    public PageResult<EquipMaintenancePlan> list(Long equipId, String status, String startDate, String endDate, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<EquipMaintenancePlan> rows = planMapper.selectList(equipId, status, startDate, endDate, offset, pageSize);
        long total = planMapper.countList(equipId, status, startDate, endDate);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override @Transactional
    public EquipMaintenanceRecord createRecord(EquipMaintenanceRecord record) {
        recordMapper.insert(record);
        if (record.getPlanId() != null) recordMapper.completePlan(record.getPlanId());
        return record;
    }
}
