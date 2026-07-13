package com.example.mes.modules.quality.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityPatrol;
import com.example.mes.modules.quality.mapper.QualityPatrolMapper;
import com.example.mes.modules.quality.service.QualityPatrolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityPatrolServiceImpl implements QualityPatrolService {

    private final QualityPatrolMapper patrolMapper;

    @Override
    @Transactional
    public QualityPatrol createPatrol(QualityPatrol patrol) {
        patrolMapper.insert(patrol);
        return patrol;
    }

    @Override
    @Transactional
    public QualityPatrol updatePatrol(QualityPatrol patrol) {
        patrolMapper.update(patrol);
        return patrolMapper.selectById(patrol.getId());
    }

    @Override
    @Transactional
    public void deletePatrol(Long id) {
        patrolMapper.deleteById(id);
    }

    @Override
    public QualityPatrol getPatrolById(Long id) {
        return patrolMapper.selectById(id);
    }

    @Override
    public PageResult<QualityPatrol> listPatrols(String keyword, Long workOrderId, String result, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<QualityPatrol> rows = patrolMapper.selectList(keyword, workOrderId, result, offset, pageSize);
        long total = patrolMapper.countList(keyword, workOrderId, result);
        return PageResult.of(total, page, pageSize, rows);
    }
}
