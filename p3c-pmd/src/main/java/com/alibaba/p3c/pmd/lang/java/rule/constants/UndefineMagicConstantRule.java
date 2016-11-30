/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.alibaba.p3c.pmd.lang.java.rule.constants;

import java.util.Arrays;
import java.util.List;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】不允许出现任何魔法值（即未经定义的常量）直接出现在代码中。建议先灰度，不做强制
 * 
 * @author shengfang.gsf
 * 
 *
 */
public class UndefineMagicConstantRule extends AbstractJavaRule {

    // 允许未定义变量白名单,需不断补充
    private final static List<String> LITERAL_WHITE_LIST = Arrays.asList("0", "1", "");

    // 允许未定义变量所属于方法名内的白名单，即方法块内有调用，需不断补充
    private final static List<String> METHOD_WHITE_LIST = Arrays.asList("toString");

    // 允许未定义变量所属于调用方法名的白名单，即方法参数有调用,或包含，需要不断补充
    private final static List<String> CALL_METHOD_WHITE_LIST = Arrays.asList("log.info", "log.warn", "log.err",
            "LOG.info", "LOG.warn", "LOG.err", "System.out", "System.err");


    @Override
    public Object visit(ASTCompilationUnit node, Object data) {

        try {
            // 找未定义变量的父节点
            List<Node> parentNodes = node.findChildNodesWithXPath("//Literal/../../../../..[not(VariableInitializer)]");
            for (Node parentItem : parentNodes) {
                List<ASTLiteral> literals = parentItem.findDescendantsOfType(ASTLiteral.class);
                // 判断未定义变量是否在白名单模板
                for(ASTLiteral literal: literals){
                    if (!inWhiteList(literal)) {
                        addViolation(data, literal);
                    }
                }
                
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return super.visit(node, data); 
    }


    /**
     * 判断未定义变量是否属于白名单中
     * 
     * @param literal
     * @return
     */
    private boolean inWhiteList(ASTLiteral literal) {
        String name = literal.getImage();
        // 1. 变量名称白名单过滤
        for (String whiteItem : LITERAL_WHITE_LIST) {
            if (whiteItem.equals(name)) {
                return true;
            }
        }
        // 2. 变量归属方法名称白名单过滤
        ASTMethodDeclaration method = literal.getFirstParentOfType(ASTMethodDeclaration.class);
        if (method != null) {
            ASTMethodDeclarator methodDeclarator = method.getFirstChildOfType(ASTMethodDeclarator.class);
            String methodDeclaratorName = methodDeclarator.getImage();
            for (String methodItem : METHOD_WHITE_LIST) {
                if (methodItem.equals(methodDeclaratorName)) {
                    return true;
                }
            }
        }
        // 3. 变量被调用方法白名单过滤 
        ASTStatementExpression primaryExpression = literal.getFirstParentOfType(ASTStatementExpression.class);
        if (primaryExpression != null) {
            ASTPrimaryPrefix prefix = primaryExpression.getFirstDescendantOfType(ASTPrimaryPrefix.class);
            if (prefix == null) {
                return false;
            }
            ASTName astName = prefix.getFirstChildOfType(ASTName.class);
            if (astName == null) {
                return false;
            }
            String callMethodName = astName.getImage();
            for (String methoItem : CALL_METHOD_WHITE_LIST) {
                if (callMethodName.contains(methoItem)) {
                    return true;
                }
            }
        }
        return false;
    }

}
