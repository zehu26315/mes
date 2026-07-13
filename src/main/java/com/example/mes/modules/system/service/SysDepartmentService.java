package com.example.mes.modules.system.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.TreeNode;
import com.example.mes.modules.system.entity.SysDepartment;

import java.util.List;

public interface SysDepartmentService {

    SysDepartment createDepartment(SysDepartment dept);

    SysDepartment updateDepartment(SysDepartment dept);

    void deleteDepartment(Long id);

    SysDepartment getDepartmentById(Long id);

    PageResult<SysDepartment> listDepartments(String keyword, Integer status, int page, int pageSize);

    List<TreeNode<SysDepartment>> getDepartmentTree();
}
