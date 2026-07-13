package com.example.mes.modules.quality.service.impl;

import com.example.mes.modules.quality.entity.QualityStandardDetail;
import com.example.mes.modules.quality.mapper.QualityStandardDetailMapper;
import com.example.mes.modules.quality.service.QualityStandardDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityStandardDetailServiceImpl implements QualityStandardDetailService {

    private final QualityStandardDetailMapper detailMapper;

    @Override
    @Transactional
    public QualityStandardDetail createDetail(QualityStandardDetail detail) {
        detailMapper.insert(detail);
        return detail;
    }

    @Override
    @Transactional
    public QualityStandardDetail updateDetail(QualityStandardDetail detail) {
        detailMapper.update(detail);
        return detailMapper.selectById(detail.getId());
    }

    @Override
    @Transactional
    public void deleteDetail(Long id) {
        detailMapper.deleteById(id);
    }

    @Override
    public QualityStandardDetail getDetailById(Long id) {
        return detailMapper.selectById(id);
    }

    @Override
    public List<QualityStandardDetail> getDetailsByStandardId(Long standardId) {
        return detailMapper.selectByStandardId(standardId);
    }

    @Override
    @Transactional
    public void batchSaveDetails(Long standardId, List<QualityStandardDetail> details) {
        detailMapper.deleteByStandardId(standardId);
        if (details != null && !details.isEmpty()) {
            details.forEach(d -> d.setStandardId(standardId));
            detailMapper.batchInsert(details);
        }
    }
}
