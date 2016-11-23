package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午4:41
 */
public class AbstractNamingRule extends XPathRule {
    private static final String XPATH = "//ClassOrInterfaceDeclaration\n"
        + " [@Abstract='true' and @Interface='false']\n" + " [not (matches(@Image,'^(Abstract|Base).*'))]";

    public AbstractNamingRule() {
        super(XPATH);
    }
}
