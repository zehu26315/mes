package com.example.mes.modules.equipment.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.equipment.entity.EquipRepair;
import com.example.mes.modules.equipment.mapper.EquipRepairMapper;
import com.example.mes.modules.equipment.service.EquipRepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipRepairServiceImpl implements EquipRepairService {
    private final EquipRepairMapper mapper;

    @Override @Transactional
    public EquipRepair create(EquipRepair e) { e.setStatus("REPORTED"); mapper.insert(e); return e; }

    @Override public EquipRepair getById(Long id) { return mapper.selectById(id); }

    @Override
    public PageResult<EquipRepair> list(Long equipId, String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<EquipRepair> rows = mapper.selectList(equipId, status, offset, pageSize);
        long total = mapper.countList(equipId, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override @Transactional
    public EquipRepair complete(Long id) {
        EquipRepair repair = mapper.selectById(id);
        if (repair == null) throw new IllegalArgumentException("Repair not found: " + id);
        LocalDateTime endTime = LocalDateTime.now();
        long downtime = ChronoUnit.MINUTES.between(repair.getStartTime(), endTime);
        mapper.complete(id, endTime, downtime);
        return mapper.selectById(id);
    }
}
