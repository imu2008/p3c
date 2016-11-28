package com.alibaba.p3c.pmd.lang.java.util;

import com.alibaba.p3c.pmd.lang.java.enums.PojoSurfixEnum;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;

/**
 * POJO工具类
 * @author zenghou.fw
 */
public class PojoUtils {

    private PojoUtils() {
    }

    public static boolean isPojo(String klass) {
        if (klass == null) {
            return false;
        }
        for (PojoSurfixEnum surfix : PojoSurfixEnum.values()) {
            if (klass.endsWith(surfix.name())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPojo(ASTClassOrInterfaceDeclaration node) {
        return node != null && isPojo(node.getImage());
    }

}
