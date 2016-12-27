package com.alibaba.p3c.pmd.lang.java.rule.flowcontrol;

import net.sourceforge.pmd.lang.java.ast.ASTSwitchStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】在一个switch块内，每个case要么通过break/return等来终止，要么注释说明程序将继续执行到哪一个case为止；
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
        if (! node.hasDescendantMatchingXPath("SwitchLabel[@Default='true']")) {
            addViolation(data, node);
        }
    }

    // 检查case语句中包含break, return, throw, continue
    private void checkFallThrough(ASTSwitchStatement node, Object data) {
        // 参考PMD MissingBreakInSwitch的XPth规则
        final String XPATH = "../SwitchStatement[(count(.//BreakStatement)" +
                " + count(BlockStatement//Statement/ReturnStatement)" +
                " + count(BlockStatement//Statement/ThrowStatement)" +
                " + count(BlockStatement//Statement/IfStatement[@Else='true' and Statement[2][ReturnStatement|ThrowStatement]]" +
                "/Statement[1][ReturnStatement|ThrowStatement])" +
                " + count(SwitchLabel[name(following-sibling::node()) = 'SwitchLabel'])" +
                " + count(SwitchLabel[count(following-sibling::node()) = 0])" +
                "  < count (SwitchLabel))]";

        if (node.hasDescendantMatchingXPath(XPATH)) {
            addViolation(data, node);
        }
    }
}
