package com.alibaba.p3c.pmd.lang.java.rule.oop;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractPojoRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import org.jaxen.JaxenException;

import java.util.List;

/**
 *
 * 【强制】关于基本数据类型与包装数据类型的使用标准如下：
 * 1） 所有的POJO类属性使用包装数据类型。
 * 2） RPC方法的返回值和参数必须使用包装数据类型。
 * 3） 所有的局部变量使用基本数据类型。
 *
 * 只对1)进行检查
 *
 * @author zenghou.fw
 */
public class PojoMustUsePrimitiveFieldRule extends AbstractPojoRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (isPojo(node)) {
            try {
                List<Node> fields = node.findChildNodesWithXPath(
                        "ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration/FieldDeclaration");

                for (Node fieldNode : fields) {
                    ASTFieldDeclaration field = (ASTFieldDeclaration)fieldNode;
                    if (!field.isPublic() && !field.isStatic() && !field.isTransient()) {
                        Class type = field.getType();
                        // TODO 在文本件内能找到名称的才进行检查,为null时是跨文件的就无能为力了
                        if (type != null && type.isPrimitive()) {
                            addViolation(data, field);
                        }
                    }
                }
            } catch (JaxenException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        return super.visit(node, data);
    }
}
