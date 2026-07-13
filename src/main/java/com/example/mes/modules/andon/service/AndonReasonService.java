package com.example.mes.modules.andon.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.TreeNode;
import com.example.mes.modules.andon.entity.AndonReason;

import java.util.List;

public interface AndonReasonService {

    AndonReason create(AndonReason andonReason);

    AndonReason update(AndonReason andonReason);

    void delete(Long id);

    AndonReason getById(Long id);

    PageResult<AndonReason> list(String keyword, Long andonTypeId, Integer status, int page, int pageSize);

    List<TreeNode<AndonReason>> getTree();
}
