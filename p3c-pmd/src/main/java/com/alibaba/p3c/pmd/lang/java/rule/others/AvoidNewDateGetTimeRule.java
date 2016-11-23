package com.alibaba.p3c.pmd.lang.java.rule.others;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * 【强制】获取当前毫秒数：System.currentTimeMillis(); 而不是new Date().getTime();
 * 
 * @author keriezhang
 * @author xuantan.zym
 *
 */
public class AvoidNewDateGetTimeRule extends XPathRule {

    private static final String XPATH =
            "//PrimaryExpression"
            + "["
            + "PrimaryPrefix/AllocationExpression/ClassOrInterfaceType[@Image='Date'] and "
            + "PrimaryPrefix/AllocationExpression/Arguments[@ArgumentCount=0] and "
            + "PrimarySuffix[@Image='getTime'] and "
            + "PrimarySuffix/Arguments[@ArgumentCount=0]"
            + "]";
    
    public AvoidNewDateGetTimeRule() {
        super(XPATH);
    }

}
