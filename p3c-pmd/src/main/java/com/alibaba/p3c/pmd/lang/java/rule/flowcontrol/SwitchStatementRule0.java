package com.alibaba.p3c.pmd.lang.java.rule.flowcontrol;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.XPathRule;

import java.util.List;

/**
 * 【强制】在一个switch块内，每个case要么通过break/return来终止，要么注释说明程序将继续执行到哪一个case为止；
 * 在一个switch块内，都必须包含一个default语句并且放在最后，即使它什么代码也没有。
 *
 * @author zenghou.fw
 */
public class SwitchStatementRule0 extends XPathRule {

    private static final String XPATH = "//SwitchStatement[(count(.//BreakStatement)" +
            " + count(BlockStatement//Statement/ReturnStatement)" +
            " + count(BlockStatement//Statement/ThrowStatement)" +
            " + count(BlockStatement//Statement/IfStatement[@Else='true' and Statement[2][ReturnStatement|ThrowStatement]]/Statement[1][ReturnStatement|ThrowStatement])" +
            " + count(SwitchLabel[name(following-sibling::node()) = 'SwitchLabel'])" +
            " + count(SwitchLabel[count(following-sibling::node()) = 0])" +
            "  < count (SwitchLabel))]";

    public SwitchStatementRule0() {
        super(XPATH);
    }

}
