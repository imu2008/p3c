package com.alibaba.p3c.pmd.lang.java.rule.flowcontrol;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】在if/for/while/switch语句中必须使用大括号，即使只有一行代码，避免使用下面的形式：
 * <pre>
 * if (condition) statements;
 * </pre>
 *
 * @author zenghou.fw
 */
public class NeedBraceRule extends AbstractJavaRule {

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        Node firstStmt = node.jjtGetChild(1);
        // TODO else
        if (!hasBlockAsFirstChild(firstStmt)) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTForStatement node, Object data) {
        Node firstStmt = node.jjtGetChild(1);
        if (!hasBlockAsFirstChild(firstStmt)) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTWhileStatement node, Object data) {
        Node firstStmt = node.jjtGetChild(1);
        if (!hasBlockAsFirstChild(firstStmt)) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTSwitchStatement node, Object data) {
        Node firstStmt = node.jjtGetChild(1);
        if (!hasBlockAsFirstChild(firstStmt)) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    private boolean hasBlockAsFirstChild(Node node) {
        return (node.jjtGetNumChildren() != 0 && (node.jjtGetChild(0) instanceof ASTBlock));
    }

}
