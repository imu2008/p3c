package com.alibaba.p3c.pmd.lang.java.rule.comments;

import java.util.regex.Pattern;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.rule.comments.AbstractCommentRule;

/**
 * 【强制】所有的类都必须添加创建者信息。
 * 
 * @author keriezhang
 *
 */
public class ClassMustHaveAuthorRule extends AbstractCommentRule {

    private static final Pattern AUTHOR_PATTERN = Pattern.compile(".*@author.*", Pattern.DOTALL);

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration decl, Object data) {
        Comment comment = decl.comment();
        if (null == comment) {
            addViolation(data, decl);
        } else {
            String commentContent = comment.getImage();
            if (!AUTHOR_PATTERN.matcher(commentContent).matches()) {
                addViolation(data, decl);
            }
        }
        return super.visit(decl, data);
    }

    @Override
    public Object visit(ASTCompilationUnit cUnit, Object data) {
        assignCommentsToDeclarations(cUnit);

        return super.visit(cUnit, data);
    }
}
