package com.example.mes.modules.andon.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.TreeNode;
import com.example.mes.common.util.TreeUtils;
import com.example.mes.modules.andon.entity.AndonReason;
import com.example.mes.modules.andon.mapper.AndonReasonMapper;
import com.example.mes.modules.andon.service.AndonReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AndonReasonServiceImpl implements AndonReasonService {

    private final AndonReasonMapper andonReasonMapper;

    @Override
    @Transactional
    public AndonReason create(AndonReason andonReason) {
        if (andonReason.getStatus() == null) {
            andonReason.setStatus(1);
        }
        if (andonReason.getSortOrder() == null) {
            andonReason.setSortOrder(0);
        }
        andonReasonMapper.insert(andonReason);
        return andonReason;
    }

    @Override
    @Transactional
    public AndonReason update(AndonReason andonReason) {
        andonReasonMapper.update(andonReason);
        return andonReasonMapper.selectById(andonReason.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        andonReasonMapper.deleteById(id);
    }

    @Override
    public AndonReason getById(Long id) {
        return andonReasonMapper.selectById(id);
    }

    @Override
    public PageResult<AndonReason> list(String keyword, Long andonTypeId, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<AndonReason> rows = andonReasonMapper.selectList(keyword, andonTypeId, status, offset, pageSize);
        long total = andonReasonMapper.countList(keyword, andonTypeId, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    public List<TreeNode<AndonReason>> getTree() {
        List<AndonReason> all = andonReasonMapper.selectAll();
        return TreeUtils.buildTree(all, 0L, AndonReason::getId, AndonReason::getParentId);
    }
}
