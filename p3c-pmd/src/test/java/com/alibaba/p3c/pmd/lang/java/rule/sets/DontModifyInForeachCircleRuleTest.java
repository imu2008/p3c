package com.alibaba.p3c.pmd.lang.java.rule.sets;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class DontModifyInForeachCircleRuleTest extends SimpleAggregatorTst {

    private static final String RULESET = "java-sets";

    @Override
    public void setUp() {
        addRule(RULESET, "DontModifyInForeachCircleRule");
    }
}
