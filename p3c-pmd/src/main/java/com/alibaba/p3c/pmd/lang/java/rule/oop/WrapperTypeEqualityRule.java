package com.alibaba.p3c.pmd.lang.java.rule.oop;

import com.alibaba.p3c.pmd.lang.java.rule.util.NodeUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.java.typeresolution.TypeHelper;

import java.util.List;

/**
 * 【强制】所有的包装类对象之间值的比较，全部使用equals方法比较。
 *
 * @author zenghou.fw
 */
public class WrapperTypeEqualityRule extends AbstractJavaRule {

    @Override
    public Object visit(ASTEqualityExpression node, Object data) {
        List<ASTPrimaryExpression> expressions = node.findChildrenOfType(ASTPrimaryExpression.class);
        if (2 != expressions.size()) {
            addViolation(data, node);
            return super.visit(node, data);
        }

        // 表达式左值和右值
        ASTPrimaryExpression left = expressions.get(0);
        ASTPrimaryExpression right = expressions.get(1);

        if (NodeUtils.isWrapperType(left) || NodeUtils.isWrapperType(right)) {
            addViolation(data, node);
        }

        return super.visit(node, data);
    }

}
