package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午7:34
 *         1.5 【强制】常量命名应该全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。
 */
public class ConstantFieldNamingRule extends AbstractJavaRule {
    private static final Set<String> WHITE_LIST = new HashSet<>();

    static {
        WHITE_LIST.add("Logger");
        WHITE_LIST.add("serialVersionUID");
    }

    private static final String LOGGER_NAME = "Logger";

    public Object visit(ASTFieldDeclaration node, Object data) {
        if (node.isStatic() && node.isFinal()) {
            if (node.hasDecendantOfAnyType(ASTClassOrInterfaceType.class) && LOGGER_NAME
                .equals(node.getFirstDescendantOfType(ASTClassOrInterfaceType.class).getImage())) {
                return data;
            }
            String constantName = node.jjtGetChild(1).jjtGetChild(0).getImage();
            if (StringUtils.isEmpty(constantName) || WHITE_LIST.contains(constantName)) {
                return data;
            }
            if (!(constantName.equals(constantName.toUpperCase()))) {
                addViolation(data, node);
            }
            return data;
        }

        return super.visit(node, data);
    }
}
