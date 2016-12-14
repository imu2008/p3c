package com.alibaba.p3c.pmd.lang.java.rule.others;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * 【强制】在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。
 * 
 * @author keriezhang
 * @date 2016年12月14日 上午11:09:13
 *
 */
public class AvoidPatternCompileInMethodRule extends XPathRule {
    // 不允许方法里的Pattern.compile的参数是字面量，参数是变量或表达式不会违反该规则
    private static final String XPATH = "//MethodDeclaration//PrimaryExpression["
            + "PrimaryPrefix/Name[@Image='Pattern.compile'] and "
            + "PrimarySuffix/Arguments/ArgumentList/Expression/"
            + "PrimaryExpression/PrimaryPrefix/Literal[@StringLiteral='true']]";

    public AvoidPatternCompileInMethodRule() {
        super(XPATH);
    }
}
