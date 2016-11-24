package com.alibaba.p3c.pmd.lang.java.rule.others;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * 【强制】在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。
 * 
 * @author keriezhang
 *
 */
public class AvoidPatternCompileInMethodRule extends XPathRule {
    private static final String XPATH =
            "//MethodDeclaration//PrimaryExpression/PrimaryPrefix/Name[@Image='Pattern.compile']";

    public AvoidPatternCompileInMethodRule() {
        super(XPATH);
    }
}
