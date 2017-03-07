package com.alibaba.p3c.pmd.lang.vm.rule.others;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.XPathRule;
import net.sourceforge.pmd.lang.vm.ast.ASTDirective;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 【强制】后台输送给页面的变量必须加$!{var}——中间的感叹号。
 *
 * @author keriezhang
 * @date 2016年12月14日 上午11:24:59
 */
public class UseQuietReferenceNotationRule extends XPathRule {
    /**
     * 执行扫描的文件路径
     */
    private static final Pattern ALLOW_FILE_PATTERN = Pattern.compile(".*(template|velocity).*");

    private static final String UT_FILE_NAME = "n/a";
    private static final String MACRO_NAME = "macro";

    /**
     * 仅当reference在两段文本之间时做判断，自动排除了set语句、括号中间的变量
     */
    private static final String XPATH =
            "//Reference[matches(@literal, \"^\\$[^!]+\") and ./preceding-sibling::Text and ./following-sibling::Text]";

    public UseQuietReferenceNotationRule() {
        super(XPATH);
    }

    @Override
    public void evaluate(Node node, RuleContext ctx) {
        // 排除template和velocity目录以外的文件
        String sourceCodeFilename = ctx.getSourceCodeFilename();

        // 文件名既不为n/a（单元测试），又不包含template、velocity，则排除。
        if (!UT_FILE_NAME.equals(sourceCodeFilename) && !ALLOW_FILE_PATTERN.matcher(sourceCodeFilename).matches()) {
            return;
        }

        // 排除宏定义下的reference
        if (checkMacro(node)) {
            return;
        }

        super.evaluate(node, ctx);
    }

    /**
     * 检测节点是否包含在宏定义中
     * @param node 节点
     * @return true/false
     */
    private boolean checkMacro(Node node) {
        List<ASTDirective> directiveParents = node.getParentsOfType(ASTDirective.class);

        for (ASTDirective directiveParent : directiveParents) {
            if (MACRO_NAME.equals(directiveParent.getDirectiveName())) {
                return true;
            }
        }

        return false;
    }

}
