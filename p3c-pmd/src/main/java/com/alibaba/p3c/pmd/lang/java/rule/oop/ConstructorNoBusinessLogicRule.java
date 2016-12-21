package com.alibaba.p3c.pmd.lang.java.rule.oop;

import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】构造方法里面禁止加入任何业务逻辑，如果有初始化逻辑，请放在init方法中。
 *
 * @author zenghou.fw
 */
public class ConstructorNoBusinessLogicRule extends AbstractJavaRule {

    // TODO 由于业务逻辑不好界定，本规则先取消不进行检查，已从配置文件拿掉，代码暂时保留
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
