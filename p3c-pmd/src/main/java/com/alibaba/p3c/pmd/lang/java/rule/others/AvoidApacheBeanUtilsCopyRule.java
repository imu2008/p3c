package com.alibaba.p3c.pmd.lang.java.rule.others;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * 【强制】避免用Apache Beanutils进行属性的copy。
 * 
 * @author keriezhang
 * @author xuantan.zym
 *
 */
public class AvoidApacheBeanUtilsCopyRule extends XPathRule {
    private static final String XPATH =
            "//PrimaryPrefix/Name[@Image='BeanUtils.copyProperties' and "
            + "//ImportDeclaration[@ImportedName='org.apache.commons.beanutils.BeanUtils']]";

    public AvoidApacheBeanUtilsCopyRule() {
        super(XPATH);
    }

}
