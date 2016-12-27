package com.alibaba.p3c.pmd.lang.java.rule.comments;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.alibaba.p3c.pmd.lang.java.rule.util.CommentUtils;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTEnumDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessNode;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.ast.MultiLineComment;
import net.sourceforge.pmd.lang.java.ast.SingleLineComment;
import net.sourceforge.pmd.lang.java.rule.comments.AbstractCommentRule;

/**
 * 【强制】类、类属性、类方法的注释必须使用javadoc规范，不得使用//xxx方式。
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
            addViolation(data, decl);
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
                AbstractJavaNode node = (AbstractJavaNode) value;

                // maybe the last comment is within the last node
                if (lastComment != null && isCommentNotWithin(lastComment, lastNode, node)
                        && isCommentBefore(lastComment, node)) {
                    node.comment(lastComment);
                    lastComment = null;
                }

                lastNode = node;
            } else if (value instanceof Comment) {
                lastComment = (Comment) value;
            }
        }
    }

    protected SortedMap<Integer, Node> orderedComments(ASTCompilationUnit cUnit) {

        SortedMap<Integer, Node> itemsByLineNumber = new TreeMap<>();

        CommentUtils.addNodesToSortedMap(itemsByLineNumber, cUnit.getComments());

        // 排除package前面的注释
        List<ASTPackageDeclaration> packageDecl =
                cUnit.findDescendantsOfType(ASTPackageDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, packageDecl);

        // 排除import前面的注释
        List<ASTImportDeclaration> importDecl =
                cUnit.findDescendantsOfType(ASTImportDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, importDecl);

        List<ASTClassOrInterfaceDeclaration> classDecl =
                cUnit.findDescendantsOfType(ASTClassOrInterfaceDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, classDecl);

        List<ASTFieldDeclaration> fields = cUnit.findDescendantsOfType(ASTFieldDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, fields);

        List<ASTMethodDeclaration> methods =
                cUnit.findDescendantsOfType(ASTMethodDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, methods);

        List<ASTConstructorDeclaration> constructors =
                cUnit.findDescendantsOfType(ASTConstructorDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, constructors);

        List<ASTEnumDeclaration> enumDecl = cUnit.findDescendantsOfType(ASTEnumDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, enumDecl);

        return itemsByLineNumber;
    }

    private boolean isCommentNotWithin(Comment n1, Node n2, Node node) {
        if (n1 == null || n2 == null || node == null) {
            return true;
        }

        // 如果上一个节点是package或import声明，则评论必然不是包含在上一个节点中的
        if (n2 instanceof ASTPackageDeclaration || n2 instanceof ASTImportDeclaration) {
            return true;
        }

        // 对于匿名类中的方法，不做javadoc格式约定，匿名类加javadoc注释不会给IDE自动提示带来好处
        ASTClassOrInterfaceBodyDeclaration nodeClassDecl =
                node.getFirstParentOfType(ASTClassOrInterfaceBodyDeclaration.class);
        if (nodeClassDecl.isAnonymousInnerClass()) {
            return false;
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
