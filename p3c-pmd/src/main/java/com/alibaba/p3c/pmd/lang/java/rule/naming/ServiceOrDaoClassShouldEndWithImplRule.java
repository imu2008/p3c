package com.alibaba.p3c.pmd.lang.java.rule.naming;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/23 下午11:30
 * 1.13 【强制】对于Service和DAO类，基于SOA的理念，暴露出来的服务一定是接口，内部的实现类用Impl的后缀与接口区别。
 */
public class ServiceOrDaoClassShouldEndWithImplRule extends XPathRule {
    private static final String XPATH = "//ClassOrInterfaceDeclaration\n"
        + "[ .[@Interface='false'] and ./ImplementsList/ClassOrInterfaceType[ ends-with(@Image, 'Service') or "
        + "ends-with(@Image, 'DAO')]]\n"
        + "[not(.[ ends-with(@Image, 'Impl')])]";

    public ServiceOrDaoClassShouldEndWithImplRule() {
        super(XPATH);
    }
}
