package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午11:30
 */
public class ServiceClassNamingRule extends XPathRule {
    private static final String XPATH = "//ClassOrInterfaceDeclaration\n"
        + "[ .[@Interface='false'] and ./ImplementsList/ClassOrInterfaceType[ ends-with(@Image, 'Service') or "
        + "ends-with(@Image, 'DAO')]]\n"
        + "[not(.[ ends-with(@Image, 'Impl')])]";

    public ServiceClassNamingRule() {
        super(XPATH);
    }
}
