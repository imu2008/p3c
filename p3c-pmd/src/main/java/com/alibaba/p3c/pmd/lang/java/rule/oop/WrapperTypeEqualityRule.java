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
        // ==两边可能的类型有PrimaryExpression或UnaryExpression
        List<ASTPrimaryExpression> expressions = node.findChildrenOfType(ASTPrimaryExpression.class);

        for (ASTPrimaryExpression expression : expressions) {
            if (NodeUtils.isWrapperType(expression)) {
                addViolation(data, node);
                break;
            }
        }

        return super.visit(node, data);
    }

}
