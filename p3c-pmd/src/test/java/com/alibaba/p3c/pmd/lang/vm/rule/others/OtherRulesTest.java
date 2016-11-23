package com.alibaba.p3c.pmd.lang.vm.rule.others;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * vm其他规则测试类
 * 
 * @author keriezhang
 *
 */
public class OtherRulesTest extends SimpleAggregatorTst {

    private static final String RULESET = "vm-ali-others";

    @Override
    public void setUp() {
        addRule(RULESET, "UseQuietReferenceNotationRule");
    }

}
