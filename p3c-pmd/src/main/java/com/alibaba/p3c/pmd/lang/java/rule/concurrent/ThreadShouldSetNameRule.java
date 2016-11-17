package com.alibaba.p3c.pmd.lang.java.rule.concurrent;

import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTArgumentList;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 【强制】创建线程或线程池时请指定有意义的线程名称，方便出错时回溯。
 * 请不要直接使用 Executors.defaultThreadFactory() 创建线程池
 *
 * 检测规则
 * 1. 创建线程池时只能使用特定的几个构造方法
 * 2. 创建线程池的时候不能直接使用Executors.defaultThreadFactory()
 *
 * @author caikang.ck(骏烈)
 * @date 2016/11/16 下午6:05
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @see AvoidManuallyCreateThreadRule
 * @since 2016/11/16
 */
public class ThreadShouldSetNameRule extends AbstractJavaRule {
    private static final int ARGUMENT_LENGTH_2 = 2;
    private static final int ARGUMENT_LENGTH_6 = 6;

    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        ASTClassOrInterfaceType classOrInterfaceType =
                node.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
        if (node.getType() == null || !ExecutorService.class.isAssignableFrom(node.getType())) {
            if (!ThreadPoolExecutor.class.getSimpleName().equals(classOrInterfaceType.getImage())
                    && !ScheduledThreadPoolExecutor.class.getSimpleName()
                    .equals(classOrInterfaceType.getImage())) {
                return super.visit(node, data);
            }
        }
        if (ThreadPoolExecutor.class.getSimpleName().equals(classOrInterfaceType.getImage())) {
            return checkThreadPoolExecutor(node, data);
        }
        return checkSchedulePoolExecutor(node, data);
    }


    private Object checkThreadPoolExecutor(ASTAllocationExpression node, Object data) {
        ASTArgumentList argumentList = node.getFirstDescendantOfType(ASTArgumentList.class);
        if (argumentList.jjtGetNumChildren() < ARGUMENT_LENGTH_6) {
            addViolation(data, node);
            return super.visit(node, data);
        }
        return super.visit(node,data);
    }

    private Object checkSchedulePoolExecutor(ASTAllocationExpression node, Object data) {
        ASTArgumentList argumentList = node.getFirstDescendantOfType(ASTArgumentList.class);
        if (argumentList.jjtGetNumChildren() < ARGUMENT_LENGTH_2) {
            addViolation(data, node);
            return super.visit(node, data);
        }
        return super.visit(node,data);
    }
}
