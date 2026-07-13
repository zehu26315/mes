package com.example.mes.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic tree node wrapper for building hierarchical (tree) responses from flat entities.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode<T> {

    private T node;
    @Builder.Default
    private List<TreeNode<T>> children = new ArrayList<>();
}
