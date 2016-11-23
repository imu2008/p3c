package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午11:39
 */
public class PackageNamingRule extends XPathRule {
    private static final String XPATH = "//PackageDeclaration/Name\n"
        + "[not (matches(@Image, '^[a-z]+(.[a-z][a-z0-9])$'))]";

    public PackageNamingRule() {
        super(XPATH);
    }
}
