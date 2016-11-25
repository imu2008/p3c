package com.alibaba.p3c.pmd.lang.java.rule.others;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * 其他规范测试类
 * 
 * @author keriezhang
 *
 */
public class OtherRulesTest extends SimpleAggregatorTst {

    private static final String RULESET = "java-ali-others";

    @Override
    public void setUp() {
        addRule(RULESET, "AvoidApacheBeanUtilsCopyRule");
        addRule(RULESET, "AvoidNewDateGetTimeRule");
        addRule(RULESET, "AvoidPatternCompileInMethodRule");
    }
}
