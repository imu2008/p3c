package com.alibaba.p3c.pmd.lang.java.rule.concurrent;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;

import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTArgumentList;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTLambdaExpression;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
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
public class ThreadShouldSetNameRule extends AbstractAliRule {
    private static final int ARGUMENT_LENGTH_2 = 2;
    private static final int ARGUMENT_LENGTH_6 = 6;

    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        //用户自定义类
        if (node.getType() == null) {
            return super.visit(node, data);
        }
        if (!ExecutorService.class.isAssignableFrom(node.getType())) {
            return super.visit(node, data);
        }
        if (ThreadPoolExecutor.class == node.getType()) {
            return checkThreadPoolExecutor(node, data);
        }
        if (ScheduledExecutorService.class == node.getType()) {
            return checkSchedulePoolExecutor(node, data);
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        return super.visit(node, data);
    }


    private Object checkThreadPoolExecutor(ASTAllocationExpression node, Object data) {
        ASTArgumentList argumentList = node.getFirstDescendantOfType(ASTArgumentList.class);
        if (argumentList.jjtGetNumChildren() < ARGUMENT_LENGTH_6) {
            addViolation(data, node);
            return super.visit(node, data);
        }
        if (!checkThreadFactoryArgument((ASTExpression) argumentList.jjtGetChild(ARGUMENT_LENGTH_6 - 1))) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    private Object checkSchedulePoolExecutor(ASTAllocationExpression node, Object data) {
        ASTArgumentList argumentList = node.getFirstDescendantOfType(ASTArgumentList.class);
        if (argumentList.jjtGetNumChildren() < ARGUMENT_LENGTH_2) {
            addViolation(data, node);
            return super.visit(node, data);
        }
        if (!checkThreadFactoryArgument((ASTExpression) argumentList.jjtGetChild(ARGUMENT_LENGTH_2 - 1))) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    private boolean checkThreadFactoryArgument(ASTExpression expression) {
        if (expression.getType() != null && ThreadFactory.class.isAssignableFrom(expression.getType())) {
            return true;
        }
        ASTName name = expression.getFirstDescendantOfType(ASTName.class);
        if (name != null && name.getType() == Executors.class) {
            return false;
        }
        ASTLambdaExpression lambdaExpression = expression.getFirstDescendantOfType(ASTLambdaExpression.class);
        if (lambdaExpression != null) {
            List<ASTVariableDeclaratorId> variableDeclaratorIds =
                    lambdaExpression.findChildrenOfType(ASTVariableDeclaratorId.class);
            if (variableDeclaratorIds == null || variableDeclaratorIds.size() != 1) {
                return false;
            }
        } else if (expression.getType() != null && RejectedExecutionHandler.class
                .isAssignableFrom(expression.getType())) {
            return false;
        }
        return true;
    }
}
