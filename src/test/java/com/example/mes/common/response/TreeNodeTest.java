package com.example.mes.common.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TreeNode 树节点")
class TreeNodeTest {

    @Test
    @DisplayName("Builder 默认 children 为空列表（非 null）")
    void builder_defaultChildren() {
        TreeNode<String> node = TreeNode.<String>builder()
                .node("root")
                .build();

        assertEquals("root", node.getNode());
        assertNotNull(node.getChildren());
        assertTrue(node.getChildren().isEmpty());
    }

    @Test
    @DisplayName("Builder 显式设置 children")
    void builder_withChildren() {
        TreeNode<String> child = TreeNode.<String>builder().node("child").build();

        TreeNode<String> parent = TreeNode.<String>builder()
                .node("root")
                .children(List.of(child))
                .build();

        assertEquals("root", parent.getNode());
        assertEquals(1, parent.getChildren().size());
        assertEquals("child", parent.getChildren().get(0).getNode());
    }
}
