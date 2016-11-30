package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午4:24
 * 1.3  【强制】类名使用UpperCamelCase，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / DTO / VO / DAO等。
 *
 */
public class CamelClassNamingRule extends XPathRule {
    private static final String XPATH = "//ClassOrInterfaceDeclaration\n"
        + "[not (matches(@Image,\"^([A-Z][a-z0-9]+)+(DO|DTO|VO|DAO)?$\"))]";

    public CamelClassNamingRule() {
        super(XPATH);
    }
}
