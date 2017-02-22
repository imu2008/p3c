/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.alibaba.p3c.pmd.lang.java.rule.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTWhileStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】不允许出现任何魔法值（即未经定义的常量）直接出现在代码中。建议先灰度，不做强制
 * 
 * @author shengfang.gsf
 * @date 2016/12/13
 * 
 *
 */
public class UndefineMagicConstantRule extends AbstractJavaRule {

    /**
     * 魔法值去重，防止父类找子变量结点时存在重复
     */
    private List<ASTLiteral> currentLiterals = new ArrayList<ASTLiteral>();

    /**
     * 允许未定义变量白名单,需不断补充
     */
    private final static List<String> LITERAL_WHITE_LIST = Arrays.asList("0", "1", "\"\"", "0.0", "1.0", "-1", "0L", "1L");

    /**
     * 判断未定义变量是否在非循环体的if语句中
     * 
     * @param node
     * @param data
     */
    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        currentLiterals.clear();
        try {
            // 找未定义变量的父节点
            List<Node> parentNodes = node.findChildNodesWithXPath("//Literal/../../../../..[not(VariableInitializer)]");

            for (Node parentItem : parentNodes) {
                List<ASTLiteral> literals = parentItem.findDescendantsOfType(ASTLiteral.class);
                for (ASTLiteral literal : literals) {
                    if (inBlackList(literal) && !currentLiterals.contains(literal)) {
                        currentLiterals.add(literal);
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
     * 判断未定义变量是否属于黑名单中
     * 
     * @param literal
     * @return
     */
    private boolean inBlackList(ASTLiteral literal) {
        String name = literal.getImage();
        int lineNum = literal.getBeginLine();
        // name为空时，是null,bool变量，算白名单
        if (name == null) {
            return false;
        }
        // 变量名称白名单过滤
        for (String whiteItem : LITERAL_WHITE_LIST) { 
            if (whiteItem.equals(name)) {
                return false; 
            } 
        }
        // 判断变量是否在非循环体的if语句中
        ASTIfStatement ifStagtement = literal.getFirstParentOfType(ASTIfStatement.class);
        if (ifStagtement == null) {
            return false;
        }
        ASTForStatement forStateMent = ifStagtement.getFirstParentOfType(ASTForStatement.class);
        ASTWhileStatement whileStateMent = ifStagtement.getFirstParentOfType(ASTWhileStatement.class);
        if (forStateMent != null || whileStateMent != null) {
            return false;
        }
        if (lineNum == ifStagtement.getBeginLine()) {
            return true;
        }
        return false;
    }



}
