package com.alibaba.p3c.pmd.lang.java.rule.comments;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * ע�͹淶������
 * 
 * @author keriezhang
 *
 */
public class CommentRulesTest extends SimpleAggregatorTst {

    private static final String RULESET = "java-comments";

    @Override
    public void setUp() {
        addRule(RULESET, "CommentsMustBeJavadocFormatRule");
        addRule(RULESET, "AbstractMethodOrInterfaceMethodMustUseJavadocRule");
        addRule(RULESET, "ClassMustHaveAuthorRule");
    }
}
