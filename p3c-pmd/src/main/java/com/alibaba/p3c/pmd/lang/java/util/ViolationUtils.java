package com.alibaba.p3c.pmd.lang.java.util;

import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * @author caikang.ck(骏烈)
 * @date 2017/1/14 下午9:43
 * @email caikang.ck@alibaba-inc.com
 * @since 2017/1/14
 */
public class ViolationUtils {
    public static void addViolationWithPrecisePosition(AbstractJavaRule rule, AbstractJavaNode node, Object data) {
        addViolationWithPrecisePosition(rule, node, data, null);
    }

    public static void addViolationWithPrecisePosition(AbstractJavaRule rule, AbstractJavaNode node, Object data,
        String message) {
        if (node instanceof ASTFieldDeclaration) {
            ASTVariableDeclaratorId variableDeclaratorId = node.getFirstDescendantOfType(ASTVariableDeclaratorId.class);
            addViolation(rule, variableDeclaratorId, data, message);
            return;
        }
        if (node instanceof ASTMethodDeclaration) {
            ASTMethodDeclarator declarator = node.getFirstChildOfType(ASTMethodDeclarator.class);
            addViolation(rule, declarator, data, message);
            return;
        }
        addViolation(rule, node, data, message);
    }

    private static void addViolation(AbstractJavaRule rule, AbstractJavaNode node, Object data, String message) {
        if (message == null) {
            rule.addViolation(data, node);
        } else {
            rule.addViolationWithMessage(data, node, message);
        }
    }
}
