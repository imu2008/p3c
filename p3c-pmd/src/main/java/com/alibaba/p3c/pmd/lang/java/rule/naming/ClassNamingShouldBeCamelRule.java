package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午4:24 1.3 【强制】类名使用UpperCamelCase风格，必须遵从驼峰形式，但以下情形例外：（领域模型的相关命名）DO / BO
 *         / DTO / VO / DAO等。
 *
 */
public class ClassNamingShouldBeCamelRule extends AbstractJavaRule {
    private Pattern pattern = Pattern.compile("^([A-Z][a-z0-9]+)+(DO|DTO|VO|DAO|BO)?$");
    
    private static final List<String> WHITE_LIST = new ArrayList<>();
    static {
        WHITE_LIST.add("DAOImpl");
    }
    
    // public ClassNamingShouldBeCamelRule() {
    // super(XPATH);
    // }
    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (pattern.matcher(node.getImage()).matches()) {
            return super.visit(node, data);
        }
        // 白名单判断
        for (String s : WHITE_LIST) {
            if (node.getImage().endsWith(s)) {
                return super.visit(node, data);
            }
        }
        addViolation(data, node);
        
        return super.visit(node, data);
    }
}
