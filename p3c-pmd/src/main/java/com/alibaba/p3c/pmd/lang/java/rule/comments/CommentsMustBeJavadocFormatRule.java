package com.alibaba.p3c.pmd.lang.java.rule.comments;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.alibaba.p3c.pmd.lang.java.rule.util.CommentUtils;
import com.alibaba.p3c.pmd.lang.java.util.ViolationUtils;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTEnumDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessNode;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.ast.MultiLineComment;
import net.sourceforge.pmd.lang.java.ast.SingleLineComment;
import net.sourceforge.pmd.lang.java.rule.comments.AbstractCommentRule;

/**
 * 【强制】类、类属性、类方法的注释必须使用javadoc规范，不得使用//xxx方式和/*xxx*\/方式。
 * 
 * @author keriezhang
 * @date 2016年12月14日 上午11:08:39
 *
 */
public class CommentsMustBeJavadocFormatRule extends AbstractCommentRule {

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
            ViolationUtils.addViolationWithPrecisePosition(this, decl, data);
        }
    }

    @Override
    protected void assignCommentsToDeclarations(ASTCompilationUnit cUnit) {

        SortedMap<Integer, Node> itemsByLineNumber = orderedComments(cUnit);
        Comment lastComment = null;
        AbstractJavaNode lastNode = null;

        for (Entry<Integer, Node> entry : itemsByLineNumber.entrySet()) {
            Node value = entry.getValue();

            if (value instanceof AbstractJavaNode) {
                AbstractJavaNode node = (AbstractJavaNode)value;

                // 检测评论是否在类、属性、方法、枚举的上一行
                if (lastComment != null && isCommentOneLineBefore(lastComment, lastNode, node)) {
                    node.comment(lastComment);
                    lastComment = null;
                }

                lastNode = node;
            } else if (value instanceof Comment) {
                lastComment = (Comment)value;
            }
        }
    }

    protected SortedMap<Integer, Node> orderedComments(ASTCompilationUnit cUnit) {

        SortedMap<Integer, Node> itemsByLineNumber = new TreeMap<>();

        CommentUtils.addNodesToSortedMap(itemsByLineNumber, cUnit.getComments());

        List<ASTClassOrInterfaceDeclaration> classDecl =
            cUnit.findDescendantsOfType(ASTClassOrInterfaceDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, classDecl);

        List<ASTFieldDeclaration> fields = cUnit.findDescendantsOfType(ASTFieldDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, fields);

        List<ASTMethodDeclaration> methods = cUnit.findDescendantsOfType(ASTMethodDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, methods);

        List<ASTConstructorDeclaration> constructors = cUnit.findDescendantsOfType(ASTConstructorDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, constructors);

        List<ASTEnumDeclaration> enumDecl = cUnit.findDescendantsOfType(ASTEnumDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, enumDecl);

        return itemsByLineNumber;
    }

    private boolean isCommentOneLineBefore(Comment lastComment, Node lastNode, Node node) {
        ASTClassOrInterfaceBodyDeclaration parentClass =
            node.getFirstParentOfType(ASTClassOrInterfaceBodyDeclaration.class);

        // 如果节点在匿名类内部，一概不做检查
        if (parentClass != null && parentClass.isAnonymousInnerClass()) {
            return false;
        }

        // 如果上一行注释，与上一个节点结尾在同一行，不作为本节点的注释
        if (lastNode != null && lastNode.getEndLine() == lastComment.getEndLine()) {
            return false;
        }

        return lastComment.getEndLine() + 1 == node.getBeginLine();
    }
}
