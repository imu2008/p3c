package com.alibaba.p3c.pmd.lang.java.rule.concurrent;

import com.alibaba.p3c.pmd.lang.java.rule.util.NodeUtils;

import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTImplementsList;
import net.sourceforge.pmd.lang.java.ast.ASTLambdaExpression;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 【强制】线程资源必须通过线程池提供，不允许在应用中自行显式创建线程。
 * 说明：使用线程池的好处是减少在创建和销毁线程上所花的时间以及系统资源的开销，解决资源不足的问题。如果不使用线程池，有可能造成系统创建大量同类线程而导致消耗完内存或者“过度切换”的问题。
 *
 * 检测规则
 * 线程不能在除自定义ThreadFactory.newThread之外的方法中创建
 *
 * @author caikang.ck(骏烈)
 * @date 2016/11/15 下午6:27
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @see ThreadShouldSetNameRule
 * @since 2016/11/15
 */
public class AvoidManuallyCreateThreadRule extends AbstractJavaRule {
    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        if (node.getType() != Thread.class) {
            return super.visit(node, data);
        }

        Circumstance circumstance = getCircumstance(node);
        if (!circumstance.check(node)) {
            addViolation(data, node);
            return super.visit(node, data);
        }
        return super.visit(node, data);
    }

    private Circumstance getCircumstance(ASTAllocationExpression node) {
        ASTFieldDeclaration fieldDeclaration = node.getFirstParentOfType(ASTFieldDeclaration.class);
        //在字段定义中新建线程
        if (fieldDeclaration != null && fieldDeclaration.getType() == Thread.class) {
            return Circumstance.THREAD_MANUALLY_CREATED;
        }

        ASTMethodDeclaration methodDeclaration =
                node.getFirstParentOfType(ASTMethodDeclaration.class);

        ASTLambdaExpression descendantLambda = methodDeclaration == null ?
                null :
                methodDeclaration.getFirstDescendantOfType(ASTLambdaExpression.class);
        if (methodDeclaration == null || descendantLambda != null) {
            //多个 lambda 表达式就疼了
            ASTLambdaExpression lambdaExpression =
                    node.getFirstParentOfType(ASTLambdaExpression.class);
            if (descendantLambda != null && !NodeUtils
                    .isParentOrSelf(lambdaExpression, descendantLambda)) {
                return Circumstance.THREAD_MANUALLY_CREATED;
            }
            if (lambdaExpression == null) {
                return Circumstance.THREAD_MANUALLY_CREATED;
            }
            return lambdaExpression.getFirstParentOfType(ASTVariableDeclarator.class) == null ?
                    Circumstance.FACTORY_PARAMETER_LAMBDA :
                    Circumstance.FACTORY_FIELD_LAMBDA;
        }

        if (checkForNamingClass(node)) {
            return Circumstance.FACTORY_NAMING;
        }
        if (!"newThread".equals(methodDeclaration.getMethodName())) {
            return Circumstance.THREAD_MANUALLY_CREATED;
        }
        List<ASTFormalParameter> parameters =
                methodDeclaration.getFirstDescendantOfType(ASTFormalParameters.class)
                        .findChildrenOfType(ASTFormalParameter.class);
        if (parameters.size() != 1) {
            return Circumstance.THREAD_MANUALLY_CREATED;
        }
        if (parameters.get(0).getFirstChildOfType(ASTType.class).getType() != Runnable.class) {
            return Circumstance.THREAD_MANUALLY_CREATED;
        }
        ASTType returnType = methodDeclaration.getResultType().getFirstChildOfType(ASTType.class);
        if (returnType.getType() != Thread.class) {
            return Circumstance.THREAD_MANUALLY_CREATED;
        }
        return methodDeclaration.getFirstParentOfType(ASTVariableDeclarator.class) == null ?
                Circumstance.FACTORY_PARAMETER_ANONYMOUS :
                Circumstance.FACTORY_FIELD_ANONYMOUS;
    }

    private boolean checkForNamingClass(ASTAllocationExpression node) {
        ASTClassOrInterfaceDeclaration classOrInterfaceDeclaration =
                node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        if (classOrInterfaceDeclaration == null) {
            return false;
        }
        ASTImplementsList implementsList =
                classOrInterfaceDeclaration.getFirstChildOfType(ASTImplementsList.class);
        if (implementsList == null) {
            return false;
        }
        List<ASTClassOrInterfaceType> interfaceTypes =
                implementsList.findChildrenOfType(ASTClassOrInterfaceType.class);
        for (ASTClassOrInterfaceType type : interfaceTypes) {
            if (type.getType() == ThreadFactory.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object visit(ASTLambdaExpression node, Object data) {
        return super.visit(node, data);
    }

    enum Circumstance {
        THREAD_MANUALLY_CREATED {
            @Override
            public boolean check(ASTAllocationExpression node) {
                return false;
            }
        }, FACTORY_PARAMETER_LAMBDA {
            @Override
            public boolean check(ASTAllocationExpression node) {
                ASTLambdaExpression lambdaExpression =
                        node.getFirstParentOfType(ASTLambdaExpression.class);
                ASTAllocationExpression allocationExpression =
                        lambdaExpression.getFirstParentOfType(ASTAllocationExpression.class);
                return allocationExpression != null
                        && allocationExpression.getType().getSuperclass()
                        == AbstractExecutorService.class;
            }
        }, FACTORY_PARAMETER_ANONYMOUS {
            @Override
            public boolean check(ASTAllocationExpression node) {
                ASTMethodDeclaration methodDeclaration =
                        node.getFirstParentOfType(ASTMethodDeclaration.class);
                ASTAllocationExpression allocationExpression =
                        methodDeclaration.getFirstParentOfType(ASTAllocationExpression.class);
                if (allocationExpression == null
                        || allocationExpression.getFirstParentOfType(ASTAllocationExpression.class)
                        == null) {
                    return false;
                }
                ASTClassOrInterfaceType classOrInterfaceType =
                        allocationExpression.getFirstChildOfType(ASTClassOrInterfaceType.class);
                return classOrInterfaceType != null && ThreadFactory.class.getSimpleName()
                        .equals(classOrInterfaceType.getImage());
            }
        }, FACTORY_FIELD_LAMBDA {
            @Override
            public boolean check(ASTAllocationExpression node) {
                ASTLambdaExpression lambdaExpression =
                        node.getFirstParentOfType(ASTLambdaExpression.class);
                ASTVariableDeclarator variableDeclarator =
                        lambdaExpression.getFirstParentOfType(ASTVariableDeclarator.class);
                return variableDeclarator.getType() == ThreadFactory.class;
            }
        }, FACTORY_FIELD_ANONYMOUS {
            @Override
            public boolean check(ASTAllocationExpression node) {
                ASTMethodDeclaration methodDeclaration =
                        node.getFirstParentOfType(ASTMethodDeclaration.class);
                ASTVariableDeclarator variableDeclarator =
                        methodDeclaration.getFirstParentOfType(ASTVariableDeclarator.class);
                return variableDeclarator.getType() == ThreadFactory.class;
            }
        }, FACTORY_NAMING {
            @Override
            public boolean check(ASTAllocationExpression node) {
                return true;
            }
        };

        public abstract boolean check(ASTAllocationExpression node);
    }
}
