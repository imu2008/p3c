package com.alibaba.p3c.pmd.lang.java.rule.flowcontrol;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author zenghou.fw
 */
public class FlowControlRuleTest extends SimpleAggregatorTst {

    // 加载CLASSPATH下的rulesets/java/flowcontrol.xml
    private static final String RULESET = "java-flowcontrol";

    @Override
    public void setUp() {
        addRule(RULESET, "SwitchStatementRule");
        addRule(RULESET, "NeedBraceRule");
    }
}
