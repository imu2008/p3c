package com.alibaba.p3c.pmd.lang.java.rule.concurrent;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author caikang.ck(骏烈)
 * @date 2016/11/14 下午8:11
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @since 2016/11/14
 */
public class ConcurrentRuleTest extends SimpleAggregatorTst {
    private static final String RULE_NAME = "java-ali-concurrent";

    @Override
    public void setUp() {
        addRule(RULE_NAME, "ThreadPoolCreationRule");
        addRule(RULE_NAME, "AvoidUseTimerRule");
        addRule(RULE_NAME, "AvoidManuallyCreateThreadRule");
        addRule(RULE_NAME, "ThreadShouldSetNameRule");
    }
}
