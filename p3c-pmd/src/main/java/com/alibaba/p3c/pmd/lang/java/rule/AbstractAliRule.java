package com.alibaba.p3c.pmd.lang.java.rule;

import com.alibaba.p3c.pmd.fix.FixClassTypeResolver;
import com.alibaba.p3c.pmd.lang.java.rule.concurrent.ThreadShouldSetNameRule;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * @author caikang.ck(骏烈)
 * @date 2016/11/20 下午9:51
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @since 2016/11/20
 * 重新计算节点类型
 */
public abstract class AbstractAliRule extends AbstractJavaRule {
    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        FixClassTypeResolver classTypeResolver = new FixClassTypeResolver(ThreadShouldSetNameRule.class.getClassLoader());
        node.setClassTypeResolver(classTypeResolver);
        node.jjtAccept(classTypeResolver,data);
        return super.visit(node, data);
    }
}
