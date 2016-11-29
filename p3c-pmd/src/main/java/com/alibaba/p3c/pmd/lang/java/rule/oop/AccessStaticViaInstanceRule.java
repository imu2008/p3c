package com.alibaba.p3c.pmd.lang.java.rule.oop;

import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】避免通过一个类的对象引用访问此类的静态变量或静态方法，无谓增加编译器解析成本，直接用类名来访问即可。
 *
 * @author zenghou.fw
 */
public class AccessStaticViaInstanceRule extends AbstractJavaRule {

    @Override
    public Object visit(ASTPrimaryExpression node, Object data) {
//        if (node.get)
        // TODO 需要跨文件访问
        return super.visit(node, data);
    }
}
