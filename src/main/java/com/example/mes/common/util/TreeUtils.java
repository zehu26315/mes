package com.example.mes.common.util;

import com.example.mes.common.response.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Generic tree-building utility for hierarchical entity structures.
 */
public class TreeUtils {

    /**
     * Build a tree structure from a flat list of nodes, wrapping each entity in a {@link TreeNode}.
     *
     * @param allNodes     all nodes in the flat list
     * @param rootParentId the root parent ID to start from (e.g. 0L for top level)
     * @param idGetter     function to get the node's own ID
     * @param parentGetter function to get the node's parent ID (return rootParentId for top-level nodes)
     * @param <T>  the entity type
     * @param <ID> the ID type
     * @return tree structure (list of root TreeNode wrappers with nested children)
     */
    public static <T, ID> List<TreeNode<T>> buildTree(List<T> allNodes,
                                                       ID rootParentId,
                                                       Function<T, ID> idGetter,
                                                       Function<T, ID> parentGetter) {
        List<TreeNode<T>> tree = new ArrayList<>();
        for (T node : allNodes) {
            ID pid = parentGetter.apply(node);
            if (pid == null) {
                pid = rootParentId;
            }
            if (pid.equals(rootParentId)) {
                ID nodeId = idGetter.apply(node);
                List<TreeNode<T>> children = buildTree(allNodes, nodeId, idGetter, parentGetter);
                tree.add(new TreeNode<>(node, children));
            }
        }
        return tree;
    }
}
