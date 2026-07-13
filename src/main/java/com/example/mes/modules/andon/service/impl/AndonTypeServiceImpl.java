package com.example.mes.modules.andon.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.modules.andon.entity.AndonType;
import com.example.mes.modules.andon.mapper.AndonTypeMapper;
import com.example.mes.modules.andon.service.AndonTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AndonTypeServiceImpl implements AndonTypeService {

    private final AndonTypeMapper andonTypeMapper;

    @Override
    @Transactional
    public AndonType create(AndonType andonType) {
        andonTypeMapper.insert(andonType);
        return andonType;
    }

    @Override
    @Transactional
    public AndonType update(AndonType andonType) {
        andonTypeMapper.update(andonType);
        return andonTypeMapper.selectById(andonType.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        andonTypeMapper.deleteById(id);
    }

    @Override
    public AndonType getById(Long id) {
        return andonTypeMapper.selectById(id);
    }

    @Override
    public PageResult<AndonType> list(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<AndonType> rows = andonTypeMapper.selectList(keyword, offset, pageSize);
        long total = andonTypeMapper.countList(keyword);
        return PageResult.of(total, page, pageSize, rows);
    }
}
