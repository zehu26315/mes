package com.example.mes.modules.system.service;

import com.example.mes.common.response.PageResult;
import com.example.mes.common.response.TreeNode;
import com.example.mes.modules.system.entity.SysDepartment;
import com.example.mes.modules.system.mapper.SysDepartmentMapper;
import com.example.mes.modules.system.service.impl.SysDepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysDepartmentService 部门服务")
class SysDepartmentServiceImplTest {

    @Mock
    private SysDepartmentMapper sysDepartmentMapper;

    @InjectMocks
    private SysDepartmentServiceImpl service;

    private SysDepartment dept;

    @BeforeEach
    void setUp() {
        dept = SysDepartment.builder()
                .id(1L)
                .parentId(0L)
                .deptName("总公司")
                .deptCode("HQ")
                .status(1)
                .isDeleted(0)
                .build();
    }

    @Test
    @DisplayName("创建部门 status=null → 默认 status=1")
    void createDepartment_defaultStatus() {
        SysDepartment input = SysDepartment.builder()
                .deptName("新部门")
                .deptCode("NEW")
                .status(null)
                .build();

        service.createDepartment(input);

        ArgumentCaptor<SysDepartment> captor = ArgumentCaptor.forClass(SysDepartment.class);
        verify(sysDepartmentMapper).insert(captor.capture());
        assertEquals(1, captor.getValue().getStatus());
    }

    @Test
    @DisplayName("创建部门 status 已设 → 保留原值")
    void createDepartment_preserveStatus() {
        SysDepartment input = SysDepartment.builder()
                .deptName("新部门")
                .status(0)
                .build();

        service.createDepartment(input);

        ArgumentCaptor<SysDepartment> captor = ArgumentCaptor.forClass(SysDepartment.class);
        verify(sysDepartmentMapper).insert(captor.capture());
        assertEquals(0, captor.getValue().getStatus());
    }

    @Test
    @DisplayName("更新部门 → deptCode 置 null → 返回刷新实体")
    void updateDepartment() {
        SysDepartment input = SysDepartment.builder()
                .id(1L)
                .deptName("新名称")
                .deptCode("SHOULD_BE_NULLED")
                .build();

        SysDepartment refreshed = SysDepartment.builder()
                .id(1L)
                .deptName("新名称")
                .deptCode(null)
                .build();

        when(sysDepartmentMapper.selectById(1L)).thenReturn(refreshed);

        SysDepartment result = service.updateDepartment(input);

        assertNull(input.getDeptCode());
        verify(sysDepartmentMapper).update(input);
        assertSame(refreshed, result);
    }

    @Test
    @DisplayName("删除部门 → 调用 mapper.deleteById")
    void deleteDepartment() {
        service.deleteDepartment(1L);
        verify(sysDepartmentMapper).deleteById(1L);
    }

    @Test
    @DisplayName("getDepartmentById 存在 → 返回实体")
    void getDepartmentById_found() {
        when(sysDepartmentMapper.selectById(1L)).thenReturn(dept);

        SysDepartment result = service.getDepartmentById(1L);

        assertSame(dept, result);
    }

    @Test
    @DisplayName("分页列表 → offset 正确计算")
    void listDepartments_pagination() {
        when(sysDepartmentMapper.selectList("", 1, 0, 10)).thenReturn(List.of(dept));
        when(sysDepartmentMapper.countList("", 1)).thenReturn(1L);

        PageResult<SysDepartment> result = service.listDepartments("", 1, 1, 10);

        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getPage());
        assertEquals(10, result.getPageSize());
        assertEquals(1, result.getRows().size());
    }

    @Test
    @DisplayName("getDepartmentTree → 调用 TreeUtils.buildTree 构建树结构")
    void getDepartmentTree() {
        SysDepartment root = SysDepartment.builder().id(1L).parentId(0L).deptName("总公司").build();
        SysDepartment child = SysDepartment.builder().id(2L).parentId(1L).deptName("研发部").build();

        when(sysDepartmentMapper.selectList(null, null, 0, Integer.MAX_VALUE))
                .thenReturn(List.of(root, child));

        List<TreeNode<SysDepartment>> tree = service.getDepartmentTree();

        assertEquals(1, tree.size());
        assertEquals(root, tree.get(0).getNode());
        assertEquals(1, tree.get(0).getChildren().size());
        assertEquals(child, tree.get(0).getChildren().get(0).getNode());
    }
}
