package com.alibaba.p3c.pmd.lang.java.rule.flowcontrol;

import net.sourceforge.pmd.lang.ast.xpath.DocumentNavigator;
import net.sourceforge.pmd.lang.java.ast.ASTBreakStatement;
import net.sourceforge.pmd.lang.java.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchLabel;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * 【强制】在一个switch块内，每个case要么通过break/return来终止，要么注释说明程序将继续执行到哪一个case为止；
 * 在一个switch块内，都必须包含一个default语句并且放在最后，即使它什么代码也没有。
 *
 * @author zenghou.fw
 */
public class SwitchStatementRule extends AbstractJavaRule {

    @Override
    public Object visit(ASTSwitchStatement node, Object data) {
        checkDefault(node, data);

        checkFallThrough(node, data);

        return super.visit(node, data);
    }

    // 检查switch语句中包含default分支
    private void checkDefault(ASTSwitchStatement node, Object data) {
        // TODO 嵌套检查逻辑
        if (! node.hasDescendantMatchingXPath("SwitchLabel[@Default='true']")) {
            addViolation(data, node);
        }
    }

    // 检查case语句中包含break, return, throw, continue
    private void checkFallThrough(ASTSwitchStatement node, Object data) {
        // 只检查直接子case,不嵌套检查
//        List<ASTSwitchLabel> caseStatements = node.findChildrenOfType(ASTSwitchLabel.class);
//        for (ASTSwitchLabel caseStatement : caseStatements) {
//            if (caseStatement.isDefault()) {
//                continue;
//            }
//            // TODO 按章XPath规则转换
//            if (!caseStatement.hasDescendantOfType(ASTBreakStatement.class)
//                    && !caseStatement.hasDescendantOfType(ASTReturnStatement.class)) {
//                addViolation(data, caseStatement);
//            }
//        }
    }
}
