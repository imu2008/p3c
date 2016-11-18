package com.alibaba.p3c.pmd.lang.java.rule.util;

import net.sourceforge.pmd.lang.ast.Node;

/**
 * @author caikang.ck(骏烈)
 * @date 2016/11/16 下午10:40
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @since 2016/11/16
 */
public class NodeUtils {
    public static boolean isParentOrSelf(Node descendant,Node ancestor){
        if(descendant == ancestor) {
            return true;
        }
        if(descendant == null || ancestor == null){
            return false;
        }
        Node parent = descendant.jjtGetParent();
        while(parent != ancestor && parent != null){
            parent = parent.jjtGetParent();
        }
        return parent == ancestor;
    }

}
