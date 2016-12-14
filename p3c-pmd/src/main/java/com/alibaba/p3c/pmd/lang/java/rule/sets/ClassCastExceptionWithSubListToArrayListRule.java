/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.alibaba.p3c.pmd.lang.java.rule.sets;

import java.util.List;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】ArrayList的subList结果不可强转成ArrayList，否则会抛出ClassCastException异常：java.util.RandomAccessSubList
 * cannot be cast to java.util.ArrayList ;
 * 
 * @author shengfang.gsf
 * @date 2016/12/13
 * 
 *
 */
public class ClassCastExceptionWithSubListToArrayListRule extends AbstractJavaRule {


    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        } 
        try {
            List<Node> nodes = node.findChildNodesWithXPath(
                    "//CastExpression[Type/ReferenceType/ClassOrInterfaceType[@Image = \"ArrayList\"]]/PrimaryExpression/PrimaryPrefix/Name[ends-with(@Image,'.subList')]");
            for (Node item : nodes) {
                if (!(item instanceof ASTName)) {
                    continue;
                }
                addViolation(data, item); 
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return super.visit(node, data);
    }



}
