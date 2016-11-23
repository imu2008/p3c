package com.alibaba.p3c.pmd.lang.java.rule.naming;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午7:34
 */
public class ConstantFieldNamingRule extends AbstractJavaRule {
    private static final String LOGGER_NAME = "Logger";
    public Object visit(ASTFieldDeclaration node, Object data) {
        if (node.isStatic() && node.isFinal()) {
            if (node.hasDecendantOfAnyType(ASTClassOrInterfaceType.class) && LOGGER_NAME.equals(node.getFirstDescendantOfType(ASTClassOrInterfaceType.class).getImage())){
                return data;
            }
            String constantName = node.jjtGetChild(1).jjtGetChild(0).getImage();
            if (StringUtils.isEmpty(constantName)){
                return data;
            }
            if (!(constantName.equals(constantName.toUpperCase()))){
                addViolation(data, node);
            }
            return data;
        }
        
        return super.visit(node, data);
    }
}
