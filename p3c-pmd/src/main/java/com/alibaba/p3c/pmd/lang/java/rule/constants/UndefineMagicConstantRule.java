/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.alibaba.p3c.pmd.lang.java.rule.constants;

import java.util.Arrays;
import java.util.List;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
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
 
 

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {

        try {
            // 找未定义变量的父节点
            List<Node> parentNodes = node.findChildNodesWithXPath("//Literal/../../../../..[not(VariableInitializer)]");
            for (Node parentItem : parentNodes) {
                List<ASTLiteral> literals = parentItem.findDescendantsOfType(ASTLiteral.class);
                // 判断未定义变量是否在白名单模板
                for(ASTLiteral literal: literals){
                    if (inBlackList(literal)) {
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
        //name为空时，是null,bool变量，算白名单
        if(name == null){
            return false;
        }
        //判断变量是否在非循环体的if语句中
        ASTIfStatement ifStagtement = literal.getFirstParentOfType(ASTIfStatement.class); 
        if(ifStagtement == null){
            return false;
        }
        ASTForStatement forStateMent = ifStagtement.getFirstParentOfType(ASTForStatement.class);
        ASTWhileStatement whileStateMent = ifStagtement.getFirstParentOfType(ASTWhileStatement.class);
        if(forStateMent != null || whileStateMent != null){
            return false;
        }  
        if(lineNum == ifStagtement.getBeginLine()){
            return true;
        } 
        return false;
    }

    
  

}
