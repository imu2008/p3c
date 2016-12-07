package com.alibaba.p3c.pmd.lang.java.rule.concurrent;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.dfa.DataFlowNode;
import net.sourceforge.pmd.lang.dfa.StartOrEndDataFlowNode;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTSynchronizedStatement;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.Token;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.Lock;

/**
 * @author caikang.ck(骏烈)
 * @date 2016/11/25 上午11:43
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @since 2016/11/25
 */
public class AvoidCallStaticSimpleDateFormatRule extends AbstractAliRule {
    private static final String FORMAT_METHOD_NAME = "format";
    private static final String LOCK_NAME = "lock";
    private static final String UN_LOCK_NAME = "unlock";

    @Override
    public Object visit(ASTMethodDeclaration node, Object data) {
        if (node.isSynchronized()) {
            return super.visit(node, data);
        }

        handleMethod(node, data);
        return super.visit(node, data);
    }

    private void handleMethod(ASTMethodDeclaration methodDeclaration, Object data) {
        DataFlowNode dataFlowNode = methodDeclaration.getDataFlowNode();
        if (dataFlowNode == null || dataFlowNode.getFlow() == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        Set<String> localSimpleDateFormatNames = new HashSet<>();
        for (DataFlowNode flowNode : dataFlowNode.getFlow()) {
            if (flowNode instanceof StartOrEndDataFlowNode || flowNode.getNode() instanceof ASTMethodDeclaration) {
                continue;
            }
            if (flowNode.getNode() instanceof ASTVariableDeclarator) {
                ASTVariableDeclarator variableDeclarator = (ASTVariableDeclarator) flowNode.getNode();
                if (variableDeclarator.getType() == SimpleDateFormat.class) {
                    ASTVariableDeclaratorId variableDeclaratorId =
                            variableDeclarator.getFirstChildOfType(ASTVariableDeclaratorId.class);
                    localSimpleDateFormatNames.add(variableDeclaratorId.getImage());
                    continue;
                }
            }
            if (flowNode.getNode() instanceof ASTStatementExpression) {
                ASTStatementExpression statementExpression = (ASTStatementExpression) flowNode.getNode();
                if (isLockStatementExpression(statementExpression)) {
                    stack.push(flowNode.getNode());
                    continue;
                } else if (isUnLockStatementExpression(statementExpression)) {
                    while (!stack.isEmpty()) {
                        Node node = stack.pop();
                        if (isLockNode(node)) {
                            break;
                        }
                    }
                    continue;
                }
            }
            AbstractJavaNode javaNode = (AbstractJavaNode) flowNode.getNode();
            ASTPrimaryExpression flowPrimaryExpression = javaNode.getFirstDescendantOfType(ASTPrimaryExpression.class);
            if (flowPrimaryExpression == null) {
                continue;
            }
            if (flowPrimaryExpression.getFirstParentOfType(ASTSynchronizedStatement.class) != null) {
                continue;
            }
            if (!isStaticSimpleDateFormatCall(flowPrimaryExpression, localSimpleDateFormatNames)) {
                continue;
            }
            stack.push(flowPrimaryExpression);
        }
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            if (node instanceof ASTPrimaryExpression) {
                super.addViolation(data, node);
            }
        }
    }

    private boolean isLockNode(Node node) {
        if (!(node instanceof ASTStatementExpression)) {
            return false;
        }
        ASTStatementExpression statementExpression = (ASTStatementExpression) node;
        return isLockStatementExpression(statementExpression);
    }

    private boolean isStaticSimpleDateFormatCall(ASTPrimaryExpression primaryExpression,
            Set<String> localSimpleDateFormatNames) {
        if (primaryExpression.jjtGetNumChildren() == 0) {
            return false;
        }
        ASTName name = primaryExpression.getFirstDescendantOfType(ASTName.class);
        if (name == null || name.getType() != SimpleDateFormat.class) {
            return false;
        }
        if (localSimpleDateFormatNames.contains(name.getNameDeclaration().getName())) {
            return false;
        }
        ASTPrimaryPrefix primaryPrefix = (ASTPrimaryPrefix) primaryExpression.jjtGetChild(0);
        if (primaryPrefix.getType() != SimpleDateFormat.class) {
            return false;
        }

        Token token = (Token) primaryPrefix.jjtGetLastToken();
        return FORMAT_METHOD_NAME.equals(token.image);
    }

    private boolean isLockStatementExpression(ASTStatementExpression statementExpression) {
        return isLockTypeAndMethod(statementExpression, LOCK_NAME);
    }

    private boolean isUnLockStatementExpression(ASTStatementExpression statementExpression) {
        return isLockTypeAndMethod(statementExpression, UN_LOCK_NAME);
    }

    private boolean isLockTypeAndMethod(ASTStatementExpression statementExpression, String methodName) {
        ASTName name = statementExpression.getFirstDescendantOfType(ASTName.class);
        if (name == null || name.getType() == null || !Lock.class.isAssignableFrom(name.getType())) {
            return false;
        }
        Token token = (Token) name.jjtGetLastToken();
        return methodName.equals(token.image);
    }
}
