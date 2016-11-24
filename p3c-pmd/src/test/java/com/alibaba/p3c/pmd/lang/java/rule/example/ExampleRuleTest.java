package com.alibaba.p3c.pmd.lang.java.rule.example;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * 测试样例
 * 
 * @author xuantan.zym
 *
 */
public class ExampleRuleTest extends SimpleAggregatorTst{
    
    private static final String RULESET = "java-example";
    
    @Override
    public void setUp() {
        addRule(RULESET, "WhileLoopsMustUseBracesRule");
    }

}
