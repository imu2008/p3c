package com.alibaba.p3c.pmd.lang.java.rule.oop;

import com.alibaba.p3c.pmd.lang.java.rule.util.NodeUtils;
import com.alibaba.p3c.pmd.lang.java.util.PojoUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.XPathRule;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * 【强制】Object的equals方法容易抛空指针异常，应使用常量或确定有值的对象来调用equals。
 *
 * @author zenghou.fw
 */
public class EqualsAvoidNullRule extends AbstractJavaRule {

    private static final String XPATH = "//PrimaryExpression[" +
            "PrimaryPrefix[Name" +
            "[(ends-with(@Image, '.equals'))]" +
            "]" +
            "[" +
            "(../PrimarySuffix/Arguments/ArgumentList/Expression/PrimaryExpression/PrimaryPrefix)" +
            " and " +
            "( count(../PrimarySuffix/Arguments/ArgumentList/Expression) = 1 )" +
            "]" +
            "]" +
            "[not(ancestor::Expression/ConditionalAndExpression//EqualityExpression[@Image='!=']//NullLiteral)]" +
            "[not(ancestor::Expression/ConditionalOrExpression//EqualityExpression[@Image='==']//NullLiteral)]";


    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        try {
            List<Node> equalsInvocations = node.findChildNodesWithXPath(XPATH);
            if (equalsInvocations != null && equalsInvocations.size() > 0) {
                for (Node invocation : equalsInvocations) {
                    ASTPrimaryPrefix right = (ASTPrimaryPrefix) invocation.findChildNodesWithXPath(
                            "PrimarySuffix/Arguments/ArgumentList/Expression/PrimaryExpression/PrimaryPrefix")
                            .get(0);
                    if (right.getFirstChildOfType(ASTLiteral.class) != null) {
                        // 如果equals参数是字面量
                        addViolation(data, invocation);
                    } else {
                        // TODO 如果是在文件内申明的常量可以检测,跨文件的就无能为力了
                        ASTName name = right.getFirstChildOfType(ASTName.class);
                        Node nameNode = name.getNameDeclaration().getNode();
                        if ((nameNode instanceof ASTVariableDeclaratorId)
                                && (nameNode.getNthParent(2) instanceof  ASTFieldDeclaration)) {
                            ASTFieldDeclaration field = (ASTFieldDeclaration)nameNode.getNthParent(2);
                            if (NodeUtils.isConstant(field)) {
                                addViolation(data, invocation);
                            }
                        };
                    }
                }
            }
        } catch (JaxenException e) {
            throw new RuntimeException("XPath expression " + XPATH + " failed: "
                    + e.getLocalizedMessage(), e);
        }

        return super.visit(node, data);
    }

    private List<Node> findConstants(ASTClassOrInterfaceDeclaration node) {
        return null;
    }
}