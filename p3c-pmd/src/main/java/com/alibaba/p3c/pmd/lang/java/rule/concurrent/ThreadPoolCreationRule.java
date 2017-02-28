package com.alibaba.p3c.pmd.lang.java.rule.concurrent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.Token;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

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

    private static final String DOT = ".";
    private static final String COLON = ";";
    private static final String NEW = "new";
    private static final String EXECUTORS_NEW = Executors.class.getSimpleName() + DOT + NEW;
    private static final String FULL_EXECUTORS_NEW = Executors.class.getName() + DOT + NEW;
    private static final String BRACKETS = "()";

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        Object superResult = super.visit(node, data);

        Info info = new Info();
        List<ASTImportDeclaration> importDeclarations = node.findChildrenOfType(ASTImportDeclaration.class);
        for (ASTImportDeclaration importDeclaration : importDeclarations) {
            ASTName name = importDeclaration.getFirstChildOfType(ASTName.class);
            info.executorsUsed = info.executorsUsed
                || (name.getType() == Executors.class || Executors.class.getName().equals(name.getImage()));
            if (name.getImage().startsWith(Executors.class.getName() + DOT)) {
                info.importedExecutorsMethods.add(name.getImage());
            }
        }
        List<ASTPrimaryExpression> primaryExpressions = node.findDescendantsOfType(ASTPrimaryExpression.class);
        for(ASTPrimaryExpression primaryExpression : primaryExpressions){
            if (!info.executorsUsed && info.importedExecutorsMethods.isEmpty()) {
                continue;
            }

            Token initToken = (Token)primaryExpression.jjtGetFirstToken();
            if (!checkInitStatement(initToken, info)) {
                addViolation(data, primaryExpression);
            }
        }
        return superResult;
    }

    private boolean checkInitStatement(Token token, Info info) {
        String fullAssignStatement = getFullAssignStatement(token);
        if (fullAssignStatement.startsWith(EXECUTORS_NEW)) {
            return false;
        }
        if (!fullAssignStatement.startsWith(NEW) && !fullAssignStatement.startsWith(FULL_EXECUTORS_NEW)) {
            return true;
        }
        // 有lambda表达式的情况
        int index = fullAssignStatement.indexOf(BRACKETS);
        if (index == -1) {
            return true;
        }
        fullAssignStatement = fullAssignStatement.substring(0, index);
        // 考虑有人 犯二 java.util.concurrent.Executors.newxxxx
        if (info.importedExecutorsMethods.contains(fullAssignStatement)) {
            return false;
        }
        // 静态引入
        return !info.importedExecutorsMethods.contains(Executors.class.getName() + DOT + fullAssignStatement);
    }

    private String getFullAssignStatement(final Token token) {
        if (token == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(48);
        Token next = token;
        while (next.next != null && !COLON.equals(next.image)) {
            sb.append(next.image);
            next = next.next;
        }
        return sb.toString();
    }

    class Info {
        boolean executorsUsed;
        Set<String> importedExecutorsMethods = new HashSet<>();
    }
}
