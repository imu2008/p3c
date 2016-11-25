package com.alibaba.p3c.pmd.lang.java.rule.constants;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class ConstantsRulesTest extends SimpleAggregatorTst {

    private static final String RULESET = "java-ali-constants";
    
    @Override
    public void setUp() {
        addRule(RULESET, "UpperEllRule"); 
    }
}
