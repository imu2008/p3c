package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.java.typeresolution.TypeHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午5:39 异常类命名使用Exception结尾；
 */
public class ExceptionClassShouldEndWithExceptionRule extends AbstractJavaRule {
    
    private static final String EXCEPTION_END_SUFFIX = "Exception";
    
    @Override
    public Object visit(ASTExtendsList node, Object data) {
        ASTClassOrInterfaceType astClassOrInterfaceType = node.getFirstChildOfType(ASTClassOrInterfaceType.class);
        if ((astClassOrInterfaceType == null) || (!(TypeHelper.isA(astClassOrInterfaceType, Throwable.class)))) {
            return super.visit(node, data);
        }
        
        ASTClassOrInterfaceDeclaration astClassOrInterfaceDeclaration = node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        if (astClassOrInterfaceDeclaration != null
                && StringUtils.isNotEmpty(astClassOrInterfaceDeclaration.getImage())) {
            if (!(astClassOrInterfaceDeclaration.getImage().endsWith(EXCEPTION_END_SUFFIX))) {
                addViolation(data, astClassOrInterfaceDeclaration);
            }
        }
        
        return super.visit(node, data);
    }
    
}
