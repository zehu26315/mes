package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.entity.ProdReporting;
import com.example.mes.modules.production.entity.ProdWip;
import com.example.mes.modules.production.mapper.ProdReportingMapper;
import com.example.mes.modules.production.mapper.ProdWipMapper;
import com.example.mes.modules.production.service.ProdReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdReportingServiceImpl implements ProdReportingService {

    private final ProdReportingMapper prodReportingMapper;
    private final ProdWipMapper prodWipMapper;

    @Override
    @Transactional
    public ProdReporting create(ProdReporting reporting) {
        // 1. Insert the reporting record
        prodReportingMapper.insert(reporting);

        // 2. Find or create WIP record for workOrderId + processCode
        ProdWip wip = findOrCreateWip(reporting);

        // 3. Update WIP quantity based on report type
        String reportType = reporting.getReportType();
        if ("START".equalsIgnoreCase(reportType)) {
            // Add totalQty to wipQty
            BigDecimal newWipQty = wip.getWipQty().add(reporting.getTotalQty());
            prodWipMapper.updateQty(wip.getId(), newWipQty);
        } else if ("COMPLETE".equalsIgnoreCase(reportType)) {
            // Subtract goodQty from wipQty
            BigDecimal newWipQty = wip.getWipQty().subtract(reporting.getGoodQty());
            prodWipMapper.updateQty(wip.getId(), newWipQty);
        }

        return reporting;
    }

    private ProdWip findOrCreateWip(ProdReporting reporting) {
        // Look up WIP by processCode (may return multiple across work orders)
        List<ProdWip> wipList = prodWipMapper.selectByProcessCode(reporting.getProcessCode());
        ProdWip wip = wipList.stream()
                .filter(w -> w.getWorkOrderId().equals(reporting.getWorkOrderId()))
                .findFirst()
                .orElse(null);

        if (wip == null) {
            // Create new WIP record
            wip = ProdWip.builder()
                    .workOrderId(reporting.getWorkOrderId())
                    .productCode(reporting.getProcessCode())
                    .processCode(reporting.getProcessCode())
                    .processName(reporting.getProcessName())
                    .workCenter(reporting.getWorkCenter())
                    .wipQty(BigDecimal.ZERO)
                    .holdQty(BigDecimal.ZERO)
                    .build();
            prodWipMapper.insert(wip);
        }

        return wip;
    }

    @Override
    public PageResult<ProdReporting> list(Long workOrderId, String processCode, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProdReporting> rows = prodReportingMapper.selectList(workOrderId, processCode, offset, pageSize);
        long total = prodReportingMapper.countList(workOrderId, processCode);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public PageResult<ProdReporting> listCompletions(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProdReporting> rows = prodReportingMapper.selectCompletions(offset, pageSize);
        long total = prodReportingMapper.countCompletions();
        return PageResult.of(total, page, pageSize, rows);
    }
}
