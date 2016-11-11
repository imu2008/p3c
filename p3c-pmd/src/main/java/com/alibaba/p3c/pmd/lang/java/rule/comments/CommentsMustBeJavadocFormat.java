package com.alibaba.p3c.pmd.lang.java.rule.comments;

import java.util.SortedMap;
import java.util.Map.Entry;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTEnumDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessNode;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessTypeNode;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.ast.MultiLineComment;
import net.sourceforge.pmd.lang.java.ast.SingleLineComment;
import net.sourceforge.pmd.lang.java.rule.comments.AbstractCommentRule;

/**
 * 类、类属性、类方法的注释必须使用javadoc规范
 * 
 * @author keriezhang
 *
 */
public class CommentsMustBeJavadocFormat extends AbstractCommentRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration decl, Object data) {
        checkComment(decl, data);
        return super.visit(decl, data);
    }
    
    @Override
    public Object visit(ASTConstructorDeclaration decl, Object data) {
        checkComment(decl, data);
        return super.visit(decl, data);
    }

    @Override
    public Object visit(ASTMethodDeclaration decl, Object data) {
        checkComment(decl, data);
        return super.visit(decl, data);
    }
    
    @Override
    public Object visit(ASTFieldDeclaration decl, Object data) {
        checkComment(decl, data);
        return super.visit(decl, data);
    }
    
    @Override
    public Object visit(ASTEnumDeclaration decl, Object data) {
        checkComment(decl, data);
        return super.visit(decl, data);
    }

    @Override
    public Object visit(ASTCompilationUnit cUnit, Object data) {
        assignCommentsToDeclarations(cUnit);

        return super.visit(cUnit, data);
    }
    
    private void checkComment(AbstractJavaAccessNode decl, Object data) {
        Comment comment = decl.comment();
        if (comment instanceof SingleLineComment || comment instanceof MultiLineComment) {
            addViolation(data, decl);
        }
    }

    @Override
    protected void assignCommentsToDeclarations(ASTCompilationUnit cUnit) {

        SortedMap<Integer, Node> itemsByLineNumber = orderedCommentsAndDeclarations(cUnit);
        Comment lastComment = null;
        AbstractJavaAccessNode lastNode = null;

        for (Entry<Integer, Node> entry : itemsByLineNumber.entrySet()) {
            Node value = entry.getValue();

            if (value instanceof AbstractJavaAccessNode) {
                AbstractJavaAccessNode node = (AbstractJavaAccessNode) value;

                // maybe the last comment is within the last node
                if (lastComment != null && isCommentNotWithin(lastComment, lastNode, node)
                        && isCommentBefore(lastComment, node)) {
                    node.comment(lastComment);
                    lastComment = null;
                }
                if (!(node instanceof AbstractJavaAccessTypeNode)) {
                    lastNode = node;
                }
            } else if (value instanceof Comment) {
                lastComment = (Comment) value;
            }
        }
    }

    private boolean isCommentNotWithin(Comment n1, Node n2, Node node) {
        if (n1 == null || n2 == null || node == null) {
            return true;
        }
        boolean isNotWithinNode2 = !(n1.getEndLine() < n2.getEndLine()
                || n1.getEndLine() == n2.getEndLine() && n1.getEndColumn() < n2.getEndColumn());
        boolean isNotSameClass = node.getFirstParentOfType(ASTClassOrInterfaceBody.class) != n2
                .getFirstParentOfType(ASTClassOrInterfaceBody.class);
        boolean isNodeWithinNode2 = (node.getEndLine() < n2.getEndLine()
                || node.getEndLine() == n2.getEndLine() && node.getEndColumn() < n2.getEndColumn());
        return isNotWithinNode2 || isNotSameClass || isNodeWithinNode2;
    }

    private boolean isCommentBefore(Comment n1, Node n2) {
        return n1.getEndLine() < n2.getBeginLine()
                || n1.getEndLine() == n2.getBeginLine() && n1.getEndColumn() < n2.getBeginColumn();
    }
}
