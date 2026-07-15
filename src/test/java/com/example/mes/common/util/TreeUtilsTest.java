package com.example.mes.common.util;

import com.example.mes.common.response.TreeNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TreeUtils 树结构构建")
class TreeUtilsTest {

    /** 测试用实体：简单部门节点 */
    static class Dept {
        final Long id;
        final Long parentId;
        final String name;

        Dept(Long id, Long parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }

        Long getId() { return id; }
        Long getParentId() { return parentId; }
    }

    @Test
    @DisplayName("空列表 → 空树")
    void buildTree_emptyList() {
        List<TreeNode<Dept>> tree = TreeUtils.buildTree(
                Collections.emptyList(), 0L, Dept::getId, Dept::getParentId);
        assertTrue(tree.isEmpty());
    }

    @Test
    @DisplayName("单个根节点 → 一个根节点，无子节点")
    void buildTree_singleRoot() {
        Dept root = new Dept(1L, 0L, "总公司");

        List<TreeNode<Dept>> tree = TreeUtils.buildTree(
                List.of(root), 0L, Dept::getId, Dept::getParentId);

        assertEquals(1, tree.size());
        assertEquals(root, tree.get(0).getNode());
        assertTrue(tree.get(0).getChildren().isEmpty());
    }

    @Test
    @DisplayName("两层树：1个根 + 2个子节点")
    void buildTree_twoLevelTree() {
        Dept root = new Dept(1L, 0L, "总公司");
        Dept child1 = new Dept(2L, 1L, "研发部");
        Dept child2 = new Dept(3L, 1L, "市场部");

        List<TreeNode<Dept>> tree = TreeUtils.buildTree(
                List.of(root, child1, child2), 0L, Dept::getId, Dept::getParentId);

        assertEquals(1, tree.size());
        assertEquals(root, tree.get(0).getNode());
        assertEquals(2, tree.get(0).getChildren().size());
        assertEquals(child1, tree.get(0).getChildren().get(0).getNode());
        assertEquals(child2, tree.get(0).getChildren().get(1).getNode());
    }

    @Test
    @DisplayName("深层嵌套：祖 → 父 → 子")
    void buildTree_deepNesting() {
        Dept grandpa = new Dept(1L, 0L, "总公司");
        Dept parent = new Dept(2L, 1L, "分公司");
        Dept child = new Dept(3L, 2L, "部门");

        List<TreeNode<Dept>> tree = TreeUtils.buildTree(
                List.of(grandpa, parent, child), 0L, Dept::getId, Dept::getParentId);

        assertEquals(1, tree.size());
        TreeNode<Dept> rootNode = tree.get(0);
        assertEquals(1, rootNode.getChildren().size());
        assertEquals(parent, rootNode.getChildren().get(0).getNode());
        assertEquals(1, rootNode.getChildren().get(0).getChildren().size());
        assertEquals(child, rootNode.getChildren().get(0).getChildren().get(0).getNode());
    }

    @Test
    @DisplayName("多个根节点 → 多个顶层元素")
    void buildTree_multipleRoots() {
        Dept root1 = new Dept(1L, 0L, "A公司");
        Dept root2 = new Dept(2L, 0L, "B公司");

        List<TreeNode<Dept>> tree = TreeUtils.buildTree(
                List.of(root1, root2), 0L, Dept::getId, Dept::getParentId);

        assertEquals(2, tree.size());
        assertEquals(root1, tree.get(0).getNode());
        assertEquals(root2, tree.get(1).getNode());
    }

}
