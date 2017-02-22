package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/16 下午2:11
 * 1.1 【强制】所有编程相关的命名均不能以下划线或美元符号开始；
 */
public class AvoidStartWithDollarAndUnderLineNamingRule extends AbstractJavaRule {
    private static final String DOLLAR = "$";
    private static final String UNDERSCORE = "_";

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.getImage().startsWith(DOLLAR) || node.getImage().startsWith(UNDERSCORE) ) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTVariableDeclaratorId node, Object data) {
        if (node.getImage().startsWith(DOLLAR) || node.getImage().startsWith(UNDERSCORE)) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTMethodDeclarator node, Object data) {
        if (node.getImage().startsWith(DOLLAR) || node.getImage().startsWith(UNDERSCORE)) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }
}
