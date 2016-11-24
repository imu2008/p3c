package com.alibaba.p3c.pmd.lang.java.rule.util;

import java.util.List;
import java.util.SortedMap;

import net.sourceforge.pmd.lang.ast.Node;

/**
 * 注释工具类
 * 
 * @author keriezhang
 *
 */
public class CommentUtils {

    /**
     * 将节点按照出现的顺序，添加到sortedmap里，主要用于检查注释位置
     * 
     * 从原pmd规则文件中拷贝，因原方法是private的，所以只能拷贝过来了。
     * 
     * @param map
     * @param nodes
     */
    public static void addNodesToSortedMap(SortedMap<Integer, Node> map, List<? extends Node> nodes) {
        for (Node node : nodes) {
            // 按照行和列的顺序添加
            map.put((node.getBeginLine() << 16) + node.getBeginColumn(), node);
        }
    }
}
