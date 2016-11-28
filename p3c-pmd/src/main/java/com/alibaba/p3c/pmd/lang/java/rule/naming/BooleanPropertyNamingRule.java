package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 上午11:15
 * 1.8 【强制】POJO类中的任何布尔类型的变量，都不要加is，否则部分框架解析会引起序列化错误
 * POJO的判定标准：以DO/DTO/VO/DAO结尾的JAVA类
 */
public class BooleanPropertyNamingRule extends XPathRule {
    private static final String XPATH = "//VariableDeclaratorId\n" + "[(ancestor::ClassOrInterfaceDeclaration)[\n"
            + "@Interface='false'\n" + "or ends-with(@Image, 'DO')\n" + "or ends-with(@Image, 'DTO')\n"
            + "or ends-with(@Image, 'VO')\n" + "or ends-with(@Image, 'DAO')\n" + "]]\n"
            + "[../../../FieldDeclaration/Type/PrimitiveType[@Image = 'boolean']]\n"
            + "[.[ starts-with(@Image, 'is')]]";

    public BooleanPropertyNamingRule() {
        super(XPATH);
    }
}
