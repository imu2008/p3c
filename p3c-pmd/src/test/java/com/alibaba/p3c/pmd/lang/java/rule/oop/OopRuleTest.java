package com.alibaba.p3c.pmd.lang.java.rule.oop;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author zenghou.fw
 */
public class OopRuleTest extends SimpleAggregatorTst {

    // 加载CLASSPATH下的rulesets/java/ali-oop.xml
    private static final String RULESET = "java-ali-oop";

    @Override
    public void setUp() {
        addRule(RULESET, "EqualsAvoidNullRule");
        addRule(RULESET, "WrapperTypeEqualityRule");
        addRule(RULESET, "PojoNoDefaultValueRule");
        // 由于业务逻辑不好界定，本规则先取消不进行检查
        // addRule(RULESET, "ConstructorNoBusinessLogicRule");
        addRule(RULESET, "PojoMustUsePrimitiveFieldRule");
        addRule(RULESET, "PojoMustOverrideToStringRule");
    }
}
