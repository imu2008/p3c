package com.alibaba.p3c.pmd.lang.java.rule.naming;


import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.regex.Pattern;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午5:55 1.4 【强制】方法名、参数名、成员变量、局部变量都统一使用lowerCamelCase，必须遵从驼峰形式。
 */
public class LowerCamelCaseVariableNamingRule extends AbstractJavaRule {
    
    Pattern pattern = Pattern.compile("^[a-z][a-z0-9]*([A-Z][a-z0-9]*)*(DO|DTO|VO|DAO)?$");


    @Override
    public Object visit(ASTVariableDeclaratorId node, Object data) {
        //常量命名不适用于该规则
        ASTFieldDeclaration astFieldDeclaration = node.getFirstParentOfType(ASTFieldDeclaration.class);
        if (astFieldDeclaration != null && astFieldDeclaration.isFinal() && astFieldDeclaration.isStatic()) {

            return super.visit(node, data);
        }
        
        if (!(pattern.matcher(node.getImage()).matches())) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }

    @Override

    public Object visit(ASTMethodDeclarator node, Object data) {
        if (!(pattern.matcher(node.getImage()).matches())) {
            addViolation(data, node);
        }
        return super.visit(node, data);
    }
}
