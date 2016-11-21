/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.alibaba.p3c.pmd.lang.java.rule.sets;

import java.util.List;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】在subList场景中，高度注意对原列表的修改，会导致子列表的遍历、增加、删除均产生ConcurrentModificationException 异常。
 * 
 * @author shengfang.gsf
 * 
 *
 */
public class ConcurrentExceptionWithModifyOriginSubListRule extends AbstractJavaRule {

    private final static String ADD = ".add";
    private final static String REMOVE = ".remove";
    private final static String CLEAR = ".clear";

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        try {
            List<Node> nodes = node.findChildNodesWithXPath(
                    "//VariableDeclarator[../Type/ReferenceType/ClassOrInterfaceType[@Image='List']]/VariableInitializer/Expression/PrimaryExpression/PrimaryPrefix/Name[ends-with(@Image,'.subList')]");
            for (Node item : nodes) {
                if (!(item instanceof ASTName)) {
                    continue;
                }
                String valName = getBeforeSubListVal(item.getImage());
                ASTBlock blockNode = item.getFirstParentOfType(ASTBlock.class);
                if (blockNode == null || valName == null) {
                    continue;
                }
                List<Node> blockNodes = blockNode.findChildNodesWithXPath(
                        "BlockStatement/Statement/StatementExpression/PrimaryExpression/PrimaryPrefix/Name");

                for (Node blockItem : blockNodes) {
                    // 在subList指定位置之后，原list不允许add,removed,clear
                    if (blockItem.getBeginLine() < item.getBeginLine()) {
                        continue;
                    }
                    if (checkBlockNodesValid(valName, blockItem)) {
                        addViolation(data, blockItem);
                    }
                }

            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return super.visit(node, data);
    }


    /**
     * 找subList原变量名
     * 
     * @param image
     * @return
     */
    private String getBeforeSubListVal(String image) {
        if (image == null) {
            return null;
        }
        return image.substring(0, image.indexOf("."));
    }

    /**
     * 仅找对应方法作用域内是否有违规操作
     * 
     * @param name
     * @param blockNode
     * @return
     * @throws JaxenException
     */
    private boolean checkBlockNodesValid(String variableName, Node item) {
        if (item instanceof ASTName) {
            String name = item.getImage();
            if (judgeName(name, variableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断name 等于 t.add t.remove t.clear
     * 
     * @param name
     * @param volatileFields2
     * @return
     */
    private boolean judgeName(String name, String variableName) {
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
