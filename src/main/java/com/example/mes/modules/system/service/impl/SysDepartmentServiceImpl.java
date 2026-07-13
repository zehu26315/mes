package com.example.mes.modules.system.service.impl;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.TreeNode;
import com.example.mes.common.util.TreeUtils;
import com.example.mes.config.CacheNames;
import com.example.mes.modules.system.entity.SysDepartment;
import com.example.mes.modules.system.mapper.SysDepartmentMapper;
import com.example.mes.modules.system.service.SysDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDepartmentServiceImpl implements SysDepartmentService {

    private final SysDepartmentMapper sysDepartmentMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.SYS_DEPT, CacheNames.SYS_DEPT_TREE, CacheNames.SYS_DEPT_LIST}, allEntries = true)
    public SysDepartment createDepartment(SysDepartment dept) {
        if (dept.getStatus() == null) {
            dept.setStatus(1);
        }
        sysDepartmentMapper.insert(dept);
        return dept;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.SYS_DEPT, CacheNames.SYS_DEPT_TREE, CacheNames.SYS_DEPT_LIST}, allEntries = true)
    public SysDepartment updateDepartment(SysDepartment dept) {
        dept.setDeptCode(null);
        sysDepartmentMapper.update(dept);
        return sysDepartmentMapper.selectById(dept.getId());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {CacheNames.SYS_DEPT, CacheNames.SYS_DEPT_TREE, CacheNames.SYS_DEPT_LIST}, allEntries = true)
    public void deleteDepartment(Long id) {
        sysDepartmentMapper.deleteById(id);
    }

    @Override
    @Cacheable(value = CacheNames.SYS_DEPT, key = "#id", unless = "#result == null")
    public SysDepartment getDepartmentById(Long id) {
        return sysDepartmentMapper.selectById(id);
    }

    @Override
    public PageResult<SysDepartment> listDepartments(String keyword, Integer status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<SysDepartment> rows = sysDepartmentMapper.selectList(keyword, status, offset, pageSize);
        long total = sysDepartmentMapper.countList(keyword, status);
        return PageResult.of(total, page, pageSize, rows);
    }

    @Override
    @Cacheable(value = CacheNames.SYS_DEPT_TREE, unless = "#result == null || #result.isEmpty()")
    public List<TreeNode<SysDepartment>> getDepartmentTree() {
        List<SysDepartment> allDepts = sysDepartmentMapper.selectList(null, null, 0, Integer.MAX_VALUE);
        return TreeUtils.buildTree(allDepts, 0L,
                SysDepartment::getId,
                d -> d.getParentId() == null ? 0L : d.getParentId());
    }
}
