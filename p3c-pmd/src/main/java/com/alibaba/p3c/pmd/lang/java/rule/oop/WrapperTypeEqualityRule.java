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

        // 如果"=="或"!="两边有null,则不检查包装类型
        if (node.hasDescendantMatchingXPath("PrimaryExpression/PrimaryPrefix/Literal")
                || node.hasDescendantMatchingXPath("UnaryExpression")) {
            return super.visit(node, data);
        }

        // "=="两边可能的类型有PrimaryExpression或UnaryExpression(如a == -2)
        List<ASTPrimaryExpression> expressions = node.findChildrenOfType(ASTPrimaryExpression.class);
        if (expressions.size() == 2) {
            if (NodeUtils.isWrapperType(expressions.get(0)) &&
                    NodeUtils.isWrapperType(expressions.get(1))) {
                addViolation(data, node);
            }
        }

        return super.visit(node, data);
    }

}
