/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.alibaba.p3c.pmd.lang.java.rule.sets;

import java.util.List;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】不要在foreach循环里进行元素的remove/add操作。remove元素请使用Iterator方式
 * 
 * @author shengfang.gsf
 * @date 2016/12/13
 *
 */
public class DontModifyInForeachCircleRule extends AbstractJavaRule {

    private final static String ADD = ".add";
    private final static String REMOVE = ".remove";
    private final static String CLEAR = ".clear";

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        try {
            List<Node> nodes =
                    node.findChildNodesWithXPath("//ForStatement/Expression/PrimaryExpression/PrimaryPrefix/Name");
            for (Node item : nodes) {
                if (!(item instanceof ASTName)) {
                    continue;
                }
                String variableName = item.getImage();
                if (variableName == null) {
                    continue;
                }
                ASTForStatement forStatement = item.getFirstParentOfType(ASTForStatement.class);
                List<Node> blockNodes = forStatement.findChildNodesWithXPath(
                        "Statement/Block/BlockStatement/Statement/StatementExpression/PrimaryExpression/PrimaryPrefix/Name");
                for (Node blockItem : blockNodes) {
                    if (!(blockItem instanceof ASTName)) {
                        continue;
                    }
                    if (judgeName(blockItem.getImage(), variableName)) {
                        addViolation(data, blockItem);
                    }
                }
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return super.visit(node, data);
    }

    private boolean judgeName(String name, String variableName) {
        if(name == null){
            return false;
        }
        if (name.equals(variableName + ADD)) {
            return true;
        }
        if (name.equals(variableName + REMOVE)) {
            return true;
        }
        if (name.equals(variableName + CLEAR)) {
            return true;
        }
        return false;
    }

}
