package com.alibaba.p3c.pmd.lang.java.rule.flowcontrol;

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
    // switch语句没有{}编译不通过,因此不作检查

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        if (node.hasElse()) {
            // 带else的IfStatement一定包含两个Statement分支
            ASTStatement elseStms = node.findChildrenOfType(ASTStatement.class).get(1);

            if (!elseStms.hasDecendantOfAnyType(ASTBlock.class, ASTIfStatement.class)) {
                addViolation(data, node);
            }
        } else {
            if (!node.hasDescendantMatchingXPath("Statement/Block")) {
                addViolation(data, node);
            }
        }

        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTForStatement node, Object data) {
        if (!node.hasDescendantMatchingXPath("Statement/Block")) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTWhileStatement node, Object data) {
        if (!node.hasDescendantMatchingXPath("Statement/Block")) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }
}
