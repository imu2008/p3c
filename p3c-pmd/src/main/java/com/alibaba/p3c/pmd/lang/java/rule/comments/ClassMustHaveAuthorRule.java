package com.alibaba.p3c.pmd.lang.java.rule.comments;

import java.util.regex.Pattern;

import net.sourceforge.pmd.lang.java.ast.*;
import net.sourceforge.pmd.lang.java.rule.comments.AbstractCommentRule;

/**
 * 【强制】所有的类都必须添加创建者信息。
 * 
 * @author keriezhang
 * @date 2016年12月14日 上午11:07:45
 *
 */
public class ClassMustHaveAuthorRule extends AbstractCommentRule {

    private static final Pattern AUTHOR_PATTERN = Pattern.compile(".*@author.*", Pattern.DOTALL);

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration decl, Object data) {
        // 排除嵌套类
        if (decl.isNested()) {
            return super.visit(decl, data);
        }

        // 排除内部类
        if (!decl.isPublic()) {
            return super.visit(decl, data);
        }

        checkAuthorComment(decl, data);

        return super.visit(decl, data);
    }

    @Override
    public Object visit(ASTEnumDeclaration decl, Object data) {
        // 排除内部枚举
        if (!decl.isPublic()) {
            return super.visit(decl, data);
        }

        // 排除内部定义的枚举，内部定义枚举已在所属类上表明author
        ASTClassOrInterfaceDeclaration parent = decl.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
        if (parent != null) {
            return super.visit(decl, data);
        }

        checkAuthorComment(decl, data);

        return super.visit(decl, data);
    }

    @Override
    public Object visit(ASTCompilationUnit cUnit, Object data) {
        assignCommentsToDeclarations(cUnit);

        return super.visit(cUnit, data);
    }

    /**
     * 检查节点是否包含author注释
     * @param decl 节点名称
     * @param data ruleContext
     */
    public void checkAuthorComment(AbstractJavaNode decl, Object data) {
        Comment comment = decl.comment();
        if (null == comment) {
            addViolation(data, decl);
        } else {
            String commentContent = comment.getImage();
            boolean hasAuthor = AUTHOR_PATTERN.matcher(commentContent).matches();
            if (!hasAuthor) {
                addViolation(data, decl);
            }
        }
    }
}
