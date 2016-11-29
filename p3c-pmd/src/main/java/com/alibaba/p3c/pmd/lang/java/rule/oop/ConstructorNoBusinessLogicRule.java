package com.alibaba.p3c.pmd.lang.java.rule.oop;

import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】构造方法里面禁止加入任何业务逻辑，如果有初始化逻辑，请放在init方法中。
 *
 * @author zenghou.fw
 */
public class ConstructorNoBusinessLogicRule extends AbstractJavaRule {

    // TODO 根据之前的讨论,构造函数中不能有任何控制语句.这样的判断太粗暴,构造函数完全废了,是否可酌情放开?
    @Override
    public Object visit(ASTConstructorDeclaration node, Object data) {
        if (node.hasDecendantOfAnyType(
                ASTIfStatement.class,
                ASTSwitchStatement.class,
                ASTForStatement.class,
                ASTWhileStatement.class,
                ASTDoStatement.class,
                ASTAllocationExpression.class)) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }
}
