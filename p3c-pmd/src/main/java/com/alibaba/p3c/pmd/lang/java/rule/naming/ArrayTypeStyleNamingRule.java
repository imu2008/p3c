package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午3:20
 * 1.7 【强制】中括号是数组类型的一部分，数组定义如下：String[] args;
 */
public class ArrayTypeStyleNamingRule extends XPathRule {
    private static final String XPATH = "//VariableDeclaratorId\n" + "[../..[@Array = 'true']]\n"
        + "[../../Type/ReferenceType[@Array != 'true']]";

    public ArrayTypeStyleNamingRule() {
        super(XPATH);
    }
}
