package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午12:02
 */
public class NamingRulesTest extends SimpleAggregatorTst {
    private static final String RULESET = "java-ali-naming";

    @Override
    public void setUp() {
        addRule(RULESET, "BooleanPropertyNamingRule");
        addRule(RULESET, "ArrayTypeStyleNamingRule");
        addRule(RULESET, "AvoidStartWithDollarAndUnderLineNamingRule");
        addRule(RULESET, "TestClassNamingRule");
        addRule(RULESET, "CamelClassNamingRule");
        addRule(RULESET, "AbstractNamingRule");
        addRule(RULESET, "ExceptionNamingRule");
        addRule(RULESET, "LowerCamelCaseVariableNamingRule");
        addRule(RULESET, "ConstantFieldNamingRule");
        addRule(RULESET, "ServiceClassNamingRule");
        addRule(RULESET, "PackageNamingRule");
    }
}
