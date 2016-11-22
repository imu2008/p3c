package com.alibaba.p3c.pmd.lang.java.rule.oop;

import com.alibaba.p3c.pmd.lang.java.rule.util.NodeUtils;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import java.util.List;

/**
 * 【强制】定义DO/DTO/VO等POJO类时，不要加任何属性默认值。
 *
 * @author zenghou.fw
 */
public class PojoNoDefaultValueRule extends AbstractJavaRule {

    @Override
    public Object visit(ASTFieldDeclaration node, Object data) {
        // TODO 判断是否为POJO
        if (node.hasDescendantOfType(ASTVariableInitializer.class)) {
            addViolation(data, node);
        }

        return super.visit(node, data);
    }

}
