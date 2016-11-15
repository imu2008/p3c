package com.alibaba.p3c.pmd.lang.java.rule.concurrent;

import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.Token;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * 【强制】线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 *
 * @author caikang.ck(骏烈)
 * @date 2016/11/14 下午1:21
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @since 2016/11/14
 */
public class ThreadPoolCreationRule extends AbstractJavaRule {

    private boolean executorsUsed;

    private Set<String> importedExecutorsMethods = new HashSet<>();

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
        if (!executorsUsed && importedExecutorsMethods.isEmpty()) {
            return super.visit(node, data);
        }

        Token initToken = (Token) node.jjtGetFirstToken();
        if (!checkInitStatement(initToken)) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    private boolean checkInitStatement(Token token) {
        String fullAssignStatement = getFullAssignStatement(token);
        if (fullAssignStatement.startsWith("Executors.new")) {
            return false;
        }
        //有lambda表达式的情况
        int index = fullAssignStatement.indexOf("()");
        if(index == -1){
            return true;
        }
        fullAssignStatement = fullAssignStatement.substring(0,index);

        //考虑有人 犯二 java.util.concurrent.Executors.newxxxx
        if(importedExecutorsMethods.contains(fullAssignStatement)){
            return false;
        }
        //静态引入
        return !importedExecutorsMethods.contains(Executors.class.getName() + "." + fullAssignStatement);
    }

    private String getFullAssignStatement(final Token token) {
        if (token == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(48);
        Token next = token;
        while (next.next != null && !";".equals(next.image)) {
            sb.append(next.image);
            next = next.next;
        }
        return sb.toString();
    }

    @Override
    public Object visit(ASTImportDeclaration node, Object data) {
        ASTName name = node.getFirstChildOfType(ASTName.class);
        //考虑到有同学要静态引入方法的情况
        executorsUsed = executorsUsed || name.getType() == Executors.class;
        if (name.getImage().startsWith(Executors.class.getName() + ".")) {
            importedExecutorsMethods.add(name.getImage());
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTPackageDeclaration node, Object data) {
        return super.visit(node, data);
    }
}
