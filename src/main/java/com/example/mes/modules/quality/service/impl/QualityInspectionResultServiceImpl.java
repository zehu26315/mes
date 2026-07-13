package com.example.mes.modules.quality.service.impl;

import com.example.mes.modules.quality.entity.QualityInspectionResult;
import com.example.mes.modules.quality.mapper.QualityInspectionResultMapper;
import com.example.mes.modules.quality.service.QualityInspectionResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityInspectionResultServiceImpl implements QualityInspectionResultService {

    private final QualityInspectionResultMapper resultMapper;

    @Override
    @Transactional
    public QualityInspectionResult createResult(QualityInspectionResult result) {
        resultMapper.insert(result);
        return result;
    }

    @Override
    @Transactional
    public QualityInspectionResult updateResult(QualityInspectionResult result) {
        resultMapper.update(result);
        return resultMapper.selectById(result.getId());
    }

    @Override
    @Transactional
    public void deleteResult(Long id) {
        resultMapper.deleteById(id);
    }

    @Override
    public QualityInspectionResult getResultById(Long id) {
        return resultMapper.selectById(id);
    }

    @Override
    public List<QualityInspectionResult> getResultsByOrderId(Long orderId) {
        return resultMapper.selectByOrderId(orderId);
    }

    @Override
    @Transactional
    public void batchSaveResults(Long orderId, List<QualityInspectionResult> results) {
        resultMapper.deleteByOrderId(orderId);
        if (results != null && !results.isEmpty()) {
            results.forEach(r -> r.setOrderId(orderId));
            resultMapper.batchInsert(results);
        }
    }
}
