package com.alibaba.p3c.pmd.lang.java.rule.oop;

import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】构造方法里面禁止加入任何业务逻辑，如果有初始化逻辑，请放在init方法中。
 *
 * @author zenghou.fw
 */
public class ConstructorNoBusinessLogicRule extends AbstractJavaRule {

    @Override
    public Object visit(ASTConstructorDeclaration node, Object data) {
        return super.visit(node, data);
    }
}
