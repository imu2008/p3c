package com.alibaba.p3c.pmd.lang.java.rule;

import com.alibaba.p3c.pmd.fix.FixClassTypeResolver;
import com.alibaba.p3c.pmd.lang.java.enums.PojoSurfixEnum;
import com.alibaba.p3c.pmd.lang.java.rule.concurrent.ThreadShouldSetNameRule;
import com.alibaba.p3c.pmd.lang.java.util.PojoUtils;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.List;

/**
 * POJO类所需规则的基类
 *
 * @author zenghou.fw
 */
public abstract class AbstractPojoRule extends AbstractJavaRule {

    /**
     * 对所有POJO的检查先作一下过滤,如文件中不包含POJO,直接跳过检查.
     * 需要考虑内部类
     *
     * @param node
     * @param data
     * @return
     */
    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        // 如果java文件包含POJO,继续访问
        if (hasPojoInJavaFile(node)) {
            return super.visit(node, data);
        }
        // 不包含POJO则直接返回
        return data;
    }

    // java文件中是否包含POJO
    private boolean hasPojoInJavaFile(ASTCompilationUnit node) {
        List<ASTClassOrInterfaceDeclaration> klasses = node.findDescendantsOfType(
                ASTClassOrInterfaceDeclaration.class);
        for (ASTClassOrInterfaceDeclaration klass : klasses) {
            if (isPojo(klass)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isPojo(ASTClassOrInterfaceDeclaration node) {
        return PojoUtils.isPojo(node);
    }
}
