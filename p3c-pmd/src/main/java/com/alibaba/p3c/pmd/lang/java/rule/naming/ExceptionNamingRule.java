package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午5:39
 * 异常类命名使用Exception结尾；
 */
public class ExceptionNamingRule extends XPathRule {
    private static final String XPATH = "//ClassOrInterfaceDeclaration\n"
        + "[//ClassOrInterfaceType[pmd-java:typeof(@Image, 'Exception')]]\n" + "[not (ends-with(@Image, 'Exception'))]";

    public ExceptionNamingRule() {
        super(XPATH);
    }
}
