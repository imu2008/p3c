package com.alibaba.p3c.pmd.lang.java.rule.oop;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractPojoRule;
import com.alibaba.p3c.pmd.lang.java.rule.util.NodeUtils;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import org.jaxen.JaxenException;

import java.util.List;

/**
 * 【强制】定义DO/DTO/VO等POJO类时，不要加任何属性默认值。
 *
 * @author zenghou.fw
 */
public class PojoNoDefaultValueRule extends AbstractPojoRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (isPojo(node)) {
            try {
                List<Node> fields = node.findChildNodesWithXPath(
                        "ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration" +
                                "/FieldDeclaration");

                for (Node fieldNode : fields) {
                    ASTFieldDeclaration field = (ASTFieldDeclaration)fieldNode;
                    if (!field.isPublic() && !field.isStatic() && !field.isTransient() &&
                            field.hasDescendantOfType(ASTVariableInitializer.class)) {
                        addViolation(data, field);
                    }
                }
            } catch (JaxenException e) {
                e.printStackTrace();
            }
        }


        return super.visit(node, data);
    }

}
