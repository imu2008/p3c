package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午5:39 异常类命名使用Exception结尾；
 */
public class ExceptionClassShouldEndWithExceptionRule extends AbstractJavaRule {
    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return super.visit(node, data);
        }
        ASTExtendsList astExtendsList =  node.getFirstChildOfType(ASTExtendsList.class);
        if (astExtendsList != null) {
            ASTClassOrInterfaceType  astClassOrInterfaceType = astExtendsList.getFirstChildOfType(ASTClassOrInterfaceType.class);
            if (astClassOrInterfaceType != null) {
                Class<?> astClassOrInterfaceTypeClass = astClassOrInterfaceType.getType();
                if (astClassOrInterfaceTypeClass.isAssignableFrom(Exception.class) || astClassOrInterfaceTypeClass.isAssignableFrom(Throwable.class)) {
                    if (!(node.getImage().endsWith("Exception"))) {
                        addViolation(data, node);
                    }
                }
            }
        }
        
        return super.visit(node, data);
    }
    
}
