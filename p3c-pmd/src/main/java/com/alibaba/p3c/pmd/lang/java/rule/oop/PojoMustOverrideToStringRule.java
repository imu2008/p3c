package com.alibaba.p3c.pmd.lang.java.rule.oop;

import com.alibaba.p3c.pmd.lang.java.enums.PojoSurfixEnum;
import com.alibaba.p3c.pmd.lang.java.rule.AbstractPojoRule;
import com.alibaba.p3c.pmd.lang.java.util.PojoUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * 【强制】POJO类必须写toString方法。使用工具类source> generate toString时，
 * 如果继承了另一个POJO类，注意在前面加一下super.toString。
 *
 *  @author zenghou.fw
 */
public class PojoMustOverrideToStringRule extends AbstractPojoRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (isPojo(node)) {
            if (!node.hasDescendantMatchingXPath("ClassOrInterfaceBody" +
                    "/ClassOrInterfaceBodyDeclaration/MethodDeclaration" +
                    "[" +
                    "@Public='true' and " +
                    "MethodDeclarator[@Image='toString'] and " +
                    "MethodDeclarator[@Image='toString' and @ParameterCount='0']" +
                    "]")) {
                addViolation(data, node);
            } else {
                // 如果继承了另一个POJO类，注意在前面加一下super.toString。
                ASTExtendsList extendsList = node.getFirstChildOfType(ASTExtendsList.class);
                if (extendsList != null) {
                    String baseName = extendsList.getFirstChildOfType(ASTClassOrInterfaceType.class).getImage();
                    if (PojoUtils.isPojo(baseName)) {
                        // TODO
                    }
                }
            }

        }
        return super.visit(node, data);
    }
}
