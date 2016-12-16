/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.alibaba.p3c.pmd.lang.java.rule.constants;

import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 【强制】long或者Long初始赋值时，必须使用大写的L，不能是小写的l，小写容易跟数字1混淆，造成误解。
 * 
 * @author shengfang.gsf
 * @date 2016/12/13
 *
 */
public class UpperEllRule extends AbstractJavaRule {
 
    @Override
    public Object visit(ASTLiteral node, Object data) {
        // 当前节点值
        String image = node.getImage();
        // 当前节点值类型如果是整型且以l结尾，则收集当前违规代码
        if (image != null && node.isLongLiteral()) {
            if (image.endsWith("l")) {
                addViolation(data, node);
            }
        }
        return super.visit(node, data);
    }
 
}
