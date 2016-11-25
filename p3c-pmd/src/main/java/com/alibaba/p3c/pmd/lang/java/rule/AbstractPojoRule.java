package com.alibaba.p3c.pmd.lang.java.rule;

import com.alibaba.p3c.pmd.fix.FixClassTypeResolver;
import com.alibaba.p3c.pmd.lang.java.enums.PojoSurfixEnum;
import com.alibaba.p3c.pmd.lang.java.rule.concurrent.ThreadShouldSetNameRule;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * POJO类所需规则的基类
 * @author zenghou.fw
 */
public abstract class AbstractPojoRule extends AbstractJavaRule {

//    @Override
//    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
//        if (isPojo(node)) {
//            return super.visit(node, data);
//        }
//        // 非POJO类不进行子节点的检查,这样处理有问题,会跳过内部类的检查
//        return data;
//    }

    protected boolean isPojo(ASTClassOrInterfaceDeclaration node) {
        return PojoSurfixEnum.isPojo(node.getImage());
    }
}
