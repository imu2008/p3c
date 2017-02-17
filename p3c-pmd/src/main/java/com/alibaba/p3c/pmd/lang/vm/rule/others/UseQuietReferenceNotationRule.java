package com.alibaba.p3c.pmd.lang.vm.rule.others;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.vm.ast.ASTDirective;
import net.sourceforge.pmd.lang.vm.ast.ASTReference;
import net.sourceforge.pmd.lang.vm.ast.ASTSetDirective;
import net.sourceforge.pmd.lang.vm.rule.AbstractVmRule;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 【强制】后台输送给页面的变量必须加$!{var}——中间的感叹号。
 *
 * @author keriezhang
 * @date 2016年12月14日 上午11:24:59
 */
public class UseQuietReferenceNotationRule extends AbstractVmRule {
    /**
     * 执行扫描的文件路径
     */
    private static final Pattern ALLOW_FILE_PATTERN = Pattern.compile(".*(template|velocity).*");

    @Override
    public Object visit(ASTReference node, Object data) {

        // 排除template和velocity目录以外的文件
        RuleContext ruleContext = (RuleContext) data;
        String sourceCodeFilename = ruleContext.getSourceCodeFilename();

        // 文件名既不为n/a（单元测试），又不包含template、velocity，则排除。
        if (!"n/a".equals(sourceCodeFilename) && !ALLOW_FILE_PATTERN.matcher(sourceCodeFilename).matches()) {
            return super.visit(node, data);
        }

        // 排除set语句
        List<ASTSetDirective> setParents = node.getParentsOfType(ASTSetDirective.class);
        boolean hasSetParent = !setParents.isEmpty();

        // 排除macro宏定义
        List<ASTDirective> directiveParents = node.getParentsOfType(ASTDirective.class);
        boolean hasMacroParent = false;

        for (ASTDirective directiveParent : directiveParents) {
            if ("macro".equals(directiveParent.getDirectiveName())) {
                hasMacroParent = true;
                break;
            }
        }

        // 既不在set语句下，也不在macro语句下，添加错误消息
        if (!hasSetParent && !hasMacroParent) {
            String literal = node.literal();
            if (!literal.startsWith("$!")) {
                addViolation(data, node);
            }
        }

        return super.visit(node, data);
    }
}
