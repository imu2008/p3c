package com.alibaba.p3c.pmd.lang.java.rule.util;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.typeresolution.TypeHelper;

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
    // TODO 临时实现,需要根据PMD API优化
    public static boolean isWrapperType(ASTPrimaryExpression expression) {
        return TypeHelper.isA(expression, Integer.class)
                || TypeHelper.isA(expression, Long.class)
                || TypeHelper.isA(expression, Boolean.class)
                || TypeHelper.isA(expression, Byte.class)
                || TypeHelper.isA(expression, Double.class)
                || TypeHelper.isA(expression, Short.class)
                || TypeHelper.isA(expression, Float.class)
                || TypeHelper.isA(expression, Character.class);
    }
}
