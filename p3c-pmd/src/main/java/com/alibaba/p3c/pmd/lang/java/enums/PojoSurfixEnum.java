package com.alibaba.p3c.pmd.lang.java.enums;

/**
 * @author zenghou.fw
 */
public enum PojoSurfixEnum {
    DO,
    DTO,
    VO,
    DAO;

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
}
