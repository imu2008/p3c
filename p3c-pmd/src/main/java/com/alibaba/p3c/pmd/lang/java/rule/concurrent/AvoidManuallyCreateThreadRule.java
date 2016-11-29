package com.alibaba.p3c.pmd.lang.java.rule.concurrent;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;

import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTImplementsList;
import net.sourceforge.pmd.lang.java.ast.ASTLambdaExpression;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTResultType;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.Token;

import java.util.List;
import java.util.concurrent.ThreadFactory;

/**
 * 【强制】线程资源必须通过线程池提供，不允许在应用中自行显式创建线程。
 * 说明：使用线程池的好处是减少在创建和销毁线程上所花的时间以及系统资源的开销，解决资源不足的问题。如果不使用线程池，有可能造成系统创建大量同类线程而导致消耗完内存或者“过度切换”的问题。
 *
 * 检测规则
 * 线程不能在除自定义ThreadFactory.newThread之外的方法中创建
 * 或者不能在Runtime.getRuntime().addShutdownHook()之外的地方创建
 * @author caikang.ck(骏烈)
 * @date 2016/11/15 下午6:27
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @see ThreadShouldSetNameRule
 * @since 2016/11/15
 */
public class AvoidManuallyCreateThreadRule extends AbstractAliRule {

    @Override
    public Object visit(ASTAllocationExpression node, Object data) {
        if (node.getType() != Thread.class) {
            return super.visit(node, data);
        }
        if (isAddShutdownHook(node)) {
            return super.visit(node, data);
        }
        //lambda 表达式直接过了
        if (node.getFirstParentOfType(ASTLambdaExpression.class) != null) {
            return super.visit(node, data);
        }
        ASTFieldDeclaration fieldDeclaration = node.getFirstParentOfType(ASTFieldDeclaration.class);
        //在字段定义中新建线程
        if (fieldDeclaration != null && fieldDeclaration.getType() == Thread.class) {
            return addViolationAndReturn(node, data);
        }
        //通过Lambda方式定义一个ThreadFactory字段
        if (node.getDataFlowNode() == null) {
            if (fieldDeclaration == null || fieldDeclaration.getType() != ThreadFactory.class) {
                return addViolationAndReturn(node, data);
            }
            return super.visit(node, data);
        }

        //定义在 newThread(Runnable)方法里面的都算是对的吧
        if (isInNewThreadMethod(node)) {
            return super.visit(node, data);
        }
        //是否是匿名ThreadFactory字段或者 非匿名ThreadFactory实现
        if ((checkForNamingClass(node) || threadFactoryVariable(node)) && isInPrimaryOrProtectedMethod(node)) {
            return super.visit(node, data);
        }
        return addViolationAndReturn(node, data);
    }

    private boolean isAddShutdownHook(ASTAllocationExpression node) {
        ASTBlockStatement blockStatement = node.getFirstParentOfType(ASTBlockStatement.class);
        if (blockStatement == null) {
            return false;
        }
        Token token = (Token) blockStatement.jjtGetFirstToken();
        return Runtime.class.getSimpleName().equals(token.image);
    }

    private boolean threadFactoryVariable(ASTAllocationExpression node) {
        ASTMethodDeclaration methodDeclaration = node.getFirstParentOfType(ASTMethodDeclaration.class);
        if (methodDeclaration == null) {
            return false;
        }
        ASTVariableDeclarator variableDeclarator = methodDeclaration.getFirstParentOfType(ASTVariableDeclarator.class);
        return variableDeclarator != null && variableDeclarator.getType() == ThreadFactory.class;
    }

    private boolean isInNewThreadMethod(ASTAllocationExpression node) {
        ASTMethodDeclaration methodDeclaration = node.getFirstParentOfType(ASTMethodDeclaration.class);
        if (methodDeclaration == null) {
            return false;
        }
        if (!returnThread(methodDeclaration)) {
            return false;
        }
        if (!"newThread".equals(methodDeclaration.getMethodName())) {
            return false;
        }
        List<ASTFormalParameter> parameters = methodDeclaration.getFirstDescendantOfType(ASTFormalParameters.class)
                .findChildrenOfType(ASTFormalParameter.class);
        return parameters.size() == 1
                && parameters.get(0).getFirstChildOfType(ASTType.class).getType() == Runnable.class;
    }

    private boolean isInPrimaryOrProtectedMethod(ASTAllocationExpression node) {
        ASTMethodDeclaration methodDeclaration = node.getFirstParentOfType(ASTMethodDeclaration.class);
        return methodDeclaration != null && returnThread(methodDeclaration) && (methodDeclaration.isPrivate()
                || methodDeclaration.isProtected());
    }

    private boolean returnThread(ASTMethodDeclaration methodDeclaration) {
        ASTResultType resultType = methodDeclaration.getFirstChildOfType(ASTResultType.class);
        ASTType type = resultType.getFirstChildOfType(ASTType.class);
        return type != null && type.getType() == Thread.class;
    }

    private Object addViolationAndReturn(ASTAllocationExpression node, Object data) {
        addViolation(data, node);
        return super.visit(node, data);
    }

    private boolean checkForNamingClass(ASTAllocationExpression node) {
        ASTClassOrInterfaceDeclaration classOrInterfaceDeclaration =
                node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        if (classOrInterfaceDeclaration == null) {
            return false;
        }
        ASTImplementsList implementsList = classOrInterfaceDeclaration.getFirstChildOfType(ASTImplementsList.class);
        if (implementsList == null) {
            return false;
        }
        List<ASTClassOrInterfaceType> interfaceTypes = implementsList.findChildrenOfType(ASTClassOrInterfaceType.class);
        for (ASTClassOrInterfaceType type : interfaceTypes) {
            if (type.getType() == ThreadFactory.class) {
                return true;
            }
        }
        return false;
    }
}
