package com.alibaba.p3c.pmd.lang.java.rule.oop;

import com.alibaba.p3c.pmd.lang.java.enums.PojoSurfixEnum;
import com.alibaba.p3c.pmd.lang.java.rule.AbstractPojoRule;
import com.alibaba.p3c.pmd.lang.java.util.PojoUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * 【强制】POJO类必须写toString方法。使用工具类source> generate toString时，
 * 如果继承了另一个POJO类，注意在前面加一下super.toString。
 *
 *  @author zenghou.fw
 */
public class PojoMustOverrideToStringRule extends AbstractPojoRule {

    private static final String XPATH =
            "ClassOrInterfaceBody" +
            "/ClassOrInterfaceBodyDeclaration/MethodDeclaration" +
            "[" +
            "@Public='true' and " +
            "MethodDeclarator[@Image='toString'] and " +
            "MethodDeclarator[@Image='toString' and @ParameterCount='0']" +
            "]";

    private static final String TOSTRING_XPATH = "//PrimaryExpression[" +
            "PrimaryPrefix[Name" +
            "[(ends-with(@Image, '.toString'))]" +
            "]" +
            "[" +
            "(../PrimarySuffix/Arguments/ArgumentList/Expression/PrimaryExpression/PrimaryPrefix/Literal[@StringLiteral='true'])" +
            " and " +
            "( count(../PrimarySuffix/Arguments/ArgumentList/Expression) = 1 )" +
            "]" +
            "]" +
            "[not(ancestor::Expression/ConditionalAndExpression//EqualityExpression[@Image='!=']//NullLiteral)]" +
            "[not(ancestor::Expression/ConditionalOrExpression//EqualityExpression[@Image='==']//NullLiteral)]";

    private static final String LOMBOK_XPATH ="../Annotation/MarkerAnnotation/Name[" +
            "(@Image='Data' and //ImportDeclaration[@ImportedName='lombok.Data' or @ImportedName='lombok'])" +
            " or (@Image='ToString' and //ImportDeclaration[@ImportedName='lombok.ToString' or @ImportedName='lombok'])" +
            " or (@Image='lombok.Data')" +
            " or (@Image='lombok.ToString')" +
            "]";

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (isPojo(node)) {
            if (withLombokAnnotation(node)) {
                return super.visit(node, data);
            }

            if (!node.hasDescendantMatchingXPath(XPATH)) {
                addViolation(data, node);
            } else {
                // TODO 如果继承了另一个POJO类，注意在前面加一下super.toString。跟继承成POJO类有啥关系????
                ASTExtendsList extendsList = node.getFirstChildOfType(ASTExtendsList.class);
                if (extendsList != null) {
                    String baseName = extendsList.getFirstChildOfType(ASTClassOrInterfaceType.class).getImage();
                    if (PojoUtils.isPojo(baseName)) {
                        try {
                            // toString()方法定义
                            ASTMethodDeclaration toStringMethod =
                                    (ASTMethodDeclaration)node.findChildNodesWithXPath(XPATH).get(0);
                            ASTBlock block = toStringMethod.getBlock();
                            if (block.hasDescendantMatchingXPath(TOSTRING_XPATH)) {
                                addViolation(data, block);
                            }

                        } catch (JaxenException e) {
                            throw new RuntimeException("XPath expression " + XPATH + " failed: "
                                    + e.getLocalizedMessage(), e);
                        }
                    }
                }
            }

        }
        return super.visit(node, data);
    }

    /**
     * 是否是lombok @Data注解类
     * @param node
     * @return
     */
    private boolean withLombokAnnotation(ASTClassOrInterfaceDeclaration node) {
        return node.hasDescendantMatchingXPath(LOMBOK_XPATH);
    }
}
