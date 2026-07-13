package com.example.mes.modules.quality.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.quality.entity.QualityInspectionStandard;
import com.example.mes.modules.quality.mapper.QualityInspectionStandardMapper;
import com.example.mes.modules.quality.service.QualityInspectionStandardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QualityInspectionStandardServiceImpl implements QualityInspectionStandardService {

    private final QualityInspectionStandardMapper standardMapper;

    @Override
    @Transactional
    public QualityInspectionStandard createStandard(QualityInspectionStandard standard) {
        if (standard.getStatus() == null) {
            standard.setStatus(1);
        }
        standardMapper.insert(standard);
        return standard;
    }

    @Override
    @Transactional
    public QualityInspectionStandard updateStandard(QualityInspectionStandard standard) {
        standardMapper.update(standard);
        return standardMapper.selectById(standard.getId());
    }

    @Override
    @Transactional
    public void deleteStandard(Long id) {
        standardMapper.deleteById(id);
    }

    @Override
    public QualityInspectionStandard getStandardById(Long id) {
        return standardMapper.selectById(id);
    }

    @Override
    public PageResult<QualityInspectionStandard> listStandards(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<QualityInspectionStandard> rows = standardMapper.selectList(keyword, status, offset, pageSize);
        long total = standardMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }
}
