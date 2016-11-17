/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.alibaba.p3c.pmd.lang.java.rule.sets;

import java.util.ArrayList;
import java.util.List;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】使用工具类Arrays.asList()把数组转换成集合时，不能使用其修改集合相关的方法，它的add/remove/
 * clear方法会抛出UnsupportedOperationException异常。
 * 
 * @author shengfang.gsf
 * 
 *
 */
public class UnsupportedExceptionWithModifyAsListRule extends AbstractJavaRule {

    // 存储当前方法内所有subList返回的变量名
    private List<String> volatileFields;

    private final static String ADD = ".add";
    private final static String REMOVE = ".remove";
    private final static String CLEAR = ".clear";


    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (this.volatileFields == null) {
            this.volatileFields = new ArrayList<>(0);
        } else {
            this.volatileFields.clear();
        }
        try {
            // 找Array.asList的变量，然后判断方法内，变量.add .removed 等不能操作。
            List<Node> nodes = node.findChildNodesWithXPath(
                    "//VariableDeclarator[../Type/ReferenceType/ClassOrInterfaceType[@Image='List']]/VariableInitializer/Expression/PrimaryExpression/PrimaryPrefix/Name[@Image='Arrays.asList']");
            for (Node item : nodes) {
                if (item instanceof ASTName) {
                    List<ASTVariableDeclarator> parents =
                            item.getParentsOfType(ASTVariableDeclarator.class);
                    if (parents != null && parents.size() == 1) {
                        ASTVariableDeclarator declarator = parents.get(0);
                        ASTVariableDeclaratorId variableName =
                                declarator.getFirstChildOfType(ASTVariableDeclaratorId.class);
                        // 存储变量名
                        this.volatileFields.add(variableName.getImage());
                    }
                }
            }
            // 或者直接xpath写到具体的 名字加add 操作
            List<Node> blockNodes = node.findChildNodesWithXPath(
                    "//BlockStatement/Statement/StatementExpression/PrimaryExpression/PrimaryPrefix/Name");
            for (Node item : blockNodes) {
                if (item instanceof ASTName) {
                    String name = item.getImage();
                    if (judgeName(name, volatileFields)) {
                        addViolation(data, node);
                    }
                }
            }

        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return super.visit(node, data);
    }

    /**
     * 判断name 等于 t.add t.remove t.clear开始
     * 
     * @param name
     * @param volatileFields2
     * @return
     */
    private boolean judgeName(String name, List<String> volatileFields) {
        for (String item : volatileFields) {
            if (name.equals(item + ADD)) {
                return true;
            }
            if (name.equals(item + REMOVE)) {
                return true;
            }
            if (name.equals(item + CLEAR)) {
                return true;
            }
        }
        return false;
    }


}
