package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.rule.junit.AbstractJUnitRule;

import java.util.List;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/16 上午9:21
 * 测试类命名以它要测试的类的名称开始，以Test结尾
 */
public class TestClassShouldEndWithTestNamingRule extends AbstractJUnitRule {
    
    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isAbstract() || node.isInterface() || node.isNested()) {
            return super.visit(node, data);
        }
        
        List<ASTMethodDeclaration> m = node.findDescendantsOfType(ASTMethodDeclaration.class);
        boolean testsFound = false;
        
        if (m != null) {
            for (ASTMethodDeclaration md : m) {
                if (!isInInnerClassOrInterface(md) && isJUnitMethod(md, data)) {
                    testsFound = true;
                }
            }
        }
        
        if ((testsFound) && (!(node.getImage().endsWith("Test")))) {
            addViolation(data, node);
        }

        return super.visit(node, data);
    }
    
    private boolean isInInnerClassOrInterface(ASTMethodDeclaration md) {
        ASTClassOrInterfaceDeclaration p = md.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        return p != null && p.isNested();
    }
}
