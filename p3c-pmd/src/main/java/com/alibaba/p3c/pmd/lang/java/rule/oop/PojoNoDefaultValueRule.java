package com.alibaba.p3c.pmd.lang.java.rule.oop;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractPojoRule;
import com.alibaba.p3c.pmd.lang.java.rule.util.NodeUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * 强制】关于基本数据类型与包装数据类型的使用标准如下：<br>
 * 1） 所有的POJO类属性使用包装数据类型。<br>
 * 2） RPC方法的返回值和参数必须使用包装数据类型。<br>
 * 3） 所有的局部变量使用基本数据类型。
 *
 * @author zenghou.fw
 */
public class PojoNoDefaultValueRule extends AbstractPojoRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (isPojo(node)) {
            try {
                List<Node> fields = node.findChildNodesWithXPath(
                        "ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration/FieldDeclaration");

                for (Node fieldNode : fields) {
                    ASTFieldDeclaration field = (ASTFieldDeclaration)fieldNode;
                    if (!field.isPublic() && !field.isFinal() && !field.isStatic() && !field.isVolatile() &&
                            field.hasDescendantOfType(ASTVariableInitializer.class)) {
                        addViolation(data, field);
                    }
                }
            } catch (JaxenException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        return super.visit(node, data);
    }
}
