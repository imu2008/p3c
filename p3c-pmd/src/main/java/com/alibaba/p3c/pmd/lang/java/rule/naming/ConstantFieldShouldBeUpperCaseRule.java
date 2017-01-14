package com.alibaba.p3c.pmd.lang.java.rule.naming;

import com.alibaba.p3c.pmd.lang.java.util.ViolationUtils;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午7:34 1.5 【强制】常量命名应该全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。
 */
public class ConstantFieldShouldBeUpperCaseRule extends AbstractJavaRule {
    private static final String LOGGER_NAME_TYPE = "Logger";
    private static final String LOG_NAME_TYPE = "Log";
    private static final String SERIAL_VERSION_UID = "serialVersionUID";
    private static final Set<String> LOG_VARIABLE_TYPE_SET = new HashSet<>();
    private static final Set<String> WHITE_LIST = new HashSet<>();
    static {
        LOG_VARIABLE_TYPE_SET.add(LOG_NAME_TYPE);
        LOG_VARIABLE_TYPE_SET.add(LOGGER_NAME_TYPE);
        WHITE_LIST.add(SERIAL_VERSION_UID);
    }
    
    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        if (node.isStatic() && node.isFinal()) {
            //如果变量的类型是日志Log或Logger，直接放过不做常量检查
            if (node.hasDecendantOfAnyType(ASTClassOrInterfaceType.class)) {
                if (LOG_VARIABLE_TYPE_SET.contains(node.getFirstDescendantOfType(ASTClassOrInterfaceType.class)
                                                       .getImage())) {
                    return super.visit(node, data);
                }
            }
            //常量检查中的白名单判断，如serialVersionUID
            String constantName = node.jjtGetChild(1).jjtGetChild(0).getImage();
            if (StringUtils.isEmpty(constantName) || WHITE_LIST.contains(constantName)) {
                return super.visit(node, data);
            }
            //常量必须要大写
            if (!(constantName.equals(constantName.toUpperCase()))) {
                ViolationUtils.addViolationWithPrecisePosition(this,node,data);
            }
            return super.visit(node, data);
        }
        
        return super.visit(node, data);
    }
}
