package com.alibaba.p3c.pmd.lang.java.rule.oop;

import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.XPathRule;

import java.util.List;

/**
 * 【强制】Object的equals方法容易抛空指针异常，应使用常量或确定有值的对象来调用equals。
 *
 * @author zenghou.fw
 */
public class EqualsAvoidNullRule extends XPathRule {

    private static final String XPATH = "//PrimaryExpression[" +
            "PrimaryPrefix[Name" +
            "[(ends-with(@Image, '.equals'))]" +
            "]" +
            "[" +
            "(../PrimarySuffix/Arguments/ArgumentList/Expression/PrimaryExpression/PrimaryPrefix/Literal[@StringLiteral='true'])" +
            " and " +
            "( count(../PrimarySuffix/Arguments/ArgumentList/Expression) = 1 )" +
            "]" +
            "]" +
            "[not(ancestor::Expression/ConditionalAndExpression//EqualityExpression[@Image='!=']//NullLiteral)]" +
            "[not(ancestor::Expression/ConditionalOrExpression//EqualityExpression[@Image='==']//NullLiteral)]";

    public EqualsAvoidNullRule() {
        super(XPATH);
    }

}
