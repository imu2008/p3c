package com.alibaba.p3c.pmd.lang.java.rule.oop;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author zenghou.fw
 */
public class OopRuleTest extends SimpleAggregatorTst {

    // 加载CLASSPATH下的rulesets/java/flowcontrol.xml
    private static final String RULESET = "java-oop";

    @Override
    public void setUp() {
        addRule(RULESET, "WrapTypeEqualityRule");
    }
}
