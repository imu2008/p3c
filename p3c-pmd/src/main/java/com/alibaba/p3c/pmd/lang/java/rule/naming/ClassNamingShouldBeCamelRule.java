package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.regex.Pattern;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午4:24 1.3 【强制】类名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO
 *         / DTO / VO / DAO等。
 *
 */
public class ClassNamingShouldBeCamelRule extends AbstractJavaRule {
    /**
     * 对命名的合法性做校验的正则表达式，类命名结尾可以是DO|DTO|VO|DAO|BO|DAOImpl|YunOS这样的特殊标志，也可以是一个大写的字母
     */
    public static Pattern PATTERN = Pattern.compile("^I?([A-Z][a-z0-9]+)+(([A-Z])|(DO|DTO|VO|DAO|BO|DAOImpl|YunOS))?$");

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (PATTERN.matcher(node.getImage()).matches()) {
            return super.visit(node, data);
        }
        addViolation(data, node);
        
        return super.visit(node, data);
    }
}
