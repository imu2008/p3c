/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.alibaba.p3c.pmd.lang.java.rule.sets;

import java.util.List;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTPrimarySuffix;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】使用集合转数组的方法，必须使用集合的toArray(T[] array)，传入的是类型完全一样的数组，大小就是list.size()。
 * 
 * @author shengfang.gsf
 * 
 *
 */
public class ClassCastExceptionWithToArrayRule extends AbstractJavaRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        try {
            List<Node> nodes = node.findChildNodesWithXPath(
                    "//CastExpression[Type/ReferenceType/ClassOrInterfaceType[@Image !=\"Object\"]]/PrimaryExpression");
            for (Node item : nodes) {
                if (item instanceof ASTPrimaryExpression) {
                    ASTPrimaryExpression primaryExpression = (ASTPrimaryExpression) item;
                    List<ASTPrimaryPrefix> primaryPrefixs =
                            primaryExpression.findChildrenOfType(ASTPrimaryPrefix.class);
                    List<ASTPrimarySuffix> primarySuffixs =
                            primaryExpression.findChildrenOfType(ASTPrimarySuffix.class);
                    if (primaryPrefixs == null || primarySuffixs == null || primaryPrefixs.isEmpty()
                            || primarySuffixs.isEmpty()) {
                        continue;
                    }
                    ASTPrimaryPrefix prefix = primaryPrefixs.get(0);
                    ASTPrimarySuffix suffix = primarySuffixs.get(0);
 
                    String childName = prefix.jjtGetChild(0).getImage();
                    if(childName == null){
                        continue;
                    } 
                    if (childName.endsWith(".toArray") && suffix.getArgumentCount() == 0
                            && primarySuffixs.size() == 1) {
                        addViolation(data, node);
                    }
                }
            }

        } catch (JaxenException e) {
            e.printStackTrace();
        } 
        return super.visit(node, data);
    }


}
