package com.alibaba.p3c.pmd.lang.vm.rule.others;

import net.sourceforge.pmd.lang.rule.XPathRule;

/**
 * 【强制】后台输送给页面的变量必须加$!{var}——中间的感叹号。
 * 
 * @author keriezhang
 * @date 2016年12月14日 上午11:24:59
 *
 */
public class UseQuietReferenceNotationRule extends XPathRule {
    /**
     * set语句排除，其他语句均做判断。
     */
    private static final String XPATH =
            "//Reference[matches(@literal, \"\\$[^!]+\") and not(ancestor::SetDirective)]";

    public UseQuietReferenceNotationRule() {
        super(XPATH);
    }
}
