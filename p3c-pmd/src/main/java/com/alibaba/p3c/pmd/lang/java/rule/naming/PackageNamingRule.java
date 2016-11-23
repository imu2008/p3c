package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午11:39
 * 1.9 【强制】包名统一使用小写，点分隔符之间有且仅有一个自然语义的英语单词。包名统一使用单数形式，但是类名如果有复数含义，类名可以使用复数形式。
 */
public class PackageNamingRule extends XPathRule {
    private static final String XPATH = "//PackageDeclaration/Name\n"
        + "[not (matches(@Image, '^[a-z]+(.[a-z][a-z0-9])$'))]";

    public PackageNamingRule() {
        super(XPATH);
    }
}
