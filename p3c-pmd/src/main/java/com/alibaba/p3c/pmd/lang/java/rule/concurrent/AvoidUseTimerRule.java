package com.alibaba.p3c.pmd.lang.java.rule.concurrent;

import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.Timer;

/**
 * 【强制】多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题。
 * 反例：阿里云平台产品技术部，域名更新具体产品信息保存到tair，Timer产生了RunTimeException异常后，定时任务不再执行，通过检查日志发现原因，改为ScheduledExecutorService方式。
 *
 * @author caikang.ck(骏烈)
 * @date 2016/11/15 下午5:36
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @since 2016/11/15
 */
public class AvoidUseTimerRule extends AbstractJavaRule {
    @Override
    public Object visit(ASTVariableDeclarator node, Object data) {
        checkType(node, data);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        ASTVariableDeclarator variableDeclarator = node.getFirstParentOfType(ASTVariableDeclarator.class);
        if (variableDeclarator != null && variableDeclarator.getType() == Timer.class) {
            return super.visit(node, data);
        }
        checkType(node, data);
        return super.visit(node, data);
    }

    private void checkType(AbstractJavaTypeNode node, Object data) {
        if (node.getType() == Timer.class) {
            addViolation(data, node);
        }
    }
}
