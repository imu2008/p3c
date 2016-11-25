package com.alibaba.p3c.pmd.lang.vm.rule.others;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * 【强制】后台输送给页面的变量必须加$!{var}——中间的感叹号。
 * 
 * @author keriezhang
 *
 */
public class UseQuietReferenceNotationRule extends XPathRule {
    /**
     * 赋值、条件判断、循环语句等，均不做判断。
     */
    private static final String XPATH =
            "//Reference[matches(@literal, \"\\$[^!]+\") and ./preceding-sibling::Text and ./following-sibling::Text]";

    public UseQuietReferenceNotationRule() {
        super(XPATH);
    }
}
