package com.example.mes.modules.production.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.production.constant.WorkOrderStatus;
import com.example.mes.modules.production.entity.ProdWorkOrder;
import com.example.mes.modules.production.mapper.ProdWorkOrderMapper;
import com.example.mes.modules.production.service.ProdWorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdWorkOrderServiceImpl implements ProdWorkOrderService {

    private final ProdWorkOrderMapper prodWorkOrderMapper;

    @Override
    @Transactional
    public ProdWorkOrder create(ProdWorkOrder workOrder) {
        // 缺省状态置为「待生产」，避免显式插入 NULL 绕过 DB 默认值导致看板/报表漏统计
        if (workOrder.getStatus() == null) {
            workOrder.setStatus(WorkOrderStatus.PENDING.getCode());
        }
        validateStatus(workOrder.getStatus());
        prodWorkOrderMapper.insert(workOrder);
        return workOrder;
    }

    @Override
    @Transactional
    public ProdWorkOrder update(ProdWorkOrder workOrder) {
        validateStatus(workOrder.getStatus());
        prodWorkOrderMapper.update(workOrder);
        return prodWorkOrderMapper.selectById(workOrder.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        prodWorkOrderMapper.deleteById(id);
    }

    @Override
    public ProdWorkOrder getById(Long id) {
        return prodWorkOrderMapper.selectById(id);
    }

    @Override
    public ProdWorkOrder getByOrderNo(String orderNo) {
        return prodWorkOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult<ProdWorkOrder> list(String keyword, String status, String startDate,
                                           String endDate, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProdWorkOrder> rows = prodWorkOrderMapper.selectList(keyword, status,
                startDate, endDate, offset, pageSize);
        long total = prodWorkOrderMapper.countList(keyword, status, startDate, endDate);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String status) {
        if (!WorkOrderStatus.isValid(status)) {
            throw new IllegalArgumentException("非法的工单状态: " + status);
        }
        prodWorkOrderMapper.updateStatus(id, status);
    }

    /** 校验状态取值：null 视为「不修改」放行，非空则必须是合法标签。 */
    private void validateStatus(String status) {
        if (status != null && !WorkOrderStatus.isValid(status)) {
            throw new IllegalArgumentException("非法的工单状态: " + status);
        }
    }
}
