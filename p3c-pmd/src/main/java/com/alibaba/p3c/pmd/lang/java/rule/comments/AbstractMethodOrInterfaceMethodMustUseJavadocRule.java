package com.alibaba.p3c.pmd.lang.java.rule.comments;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jaxen.JaxenException;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNameList;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.ast.FormalComment;
import net.sourceforge.pmd.lang.java.rule.comments.AbstractCommentRule;

/**
 * 【强制】所有的抽象方法（包括接口中的方法）必须要用javadoc注释、除了返回值、参数、异常说明外，还必须指出该方法做什么事情，实现什么功能。
 * 
 * @author keriezhang
 * @date 2016年12月14日 上午11:08:18
 *
 */
public class AbstractMethodOrInterfaceMethodMustUseJavadocRule extends AbstractCommentRule {

    private static final String METHOD_IN_INTERFACE_XPATH =
            "./ClassOrInterfaceBody/ClassOrInterfaceBodyDeclaration/MethodDeclaration";

    private static final Pattern EMPTY_CONTENT_PATTERN =
            Pattern.compile("[/*\\n\\r\\s]+(@.*)?", Pattern.DOTALL);
    private static final Pattern RETURN_PATTERN = Pattern.compile(".*@return.*", Pattern.DOTALL);

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration decl, Object data) {
        if (decl.isAbstract()) {
            List<ASTMethodDeclaration> methods =
                    decl.findDescendantsOfType(ASTMethodDeclaration.class);
            for (ASTMethodDeclaration method : methods) {
                if (method.isAbstract()) {
                    Comment comment = method.comment();
                    if (null == comment || !(comment instanceof FormalComment)) {
                        addViolationWithMessage(data, method, "所有的抽象方法必须要用javadoc注释");
                    } else {
                        this.checkMethodCommentFormat(method, data);
                    }
                }
            }
        }

        if (decl.isInterface()) {
            List<Node> methodNodes = new ArrayList<>();
            try {
                methodNodes = decl.findChildNodesWithXPath(METHOD_IN_INTERFACE_XPATH);
            } catch (JaxenException e) {
                throw new RuntimeException("XPath expression " + METHOD_IN_INTERFACE_XPATH
                        + " failed: " + e.getLocalizedMessage(), e);
            }

            for (Node node : methodNodes) {
                ASTMethodDeclaration method = (ASTMethodDeclaration) node;
                Comment comment = method.comment();
                if (null == comment || !(comment instanceof FormalComment)) {
                    addViolationWithMessage(data, method, "所有的接口方法必须要用javadoc注释");
                } else {
                    this.checkMethodCommentFormat(method, data);
                }
            }
        }
        return super.visit(decl, data);
    }

    public void checkMethodCommentFormat(ASTMethodDeclaration method, Object data) {
        Comment comment = method.comment();
        String commentContent = comment.getImage();

        // 指出该方法做什么事情，实现什么功能
        if (EMPTY_CONTENT_PATTERN.matcher(commentContent).matches()) {
            addViolationWithMessage(data, method, "需要指出该方法做什么事情，实现什么功能");
        }

        // 参数必须要有javadoc注释
        List<ASTVariableDeclaratorId> params =
                method.findDescendantsOfType(ASTVariableDeclaratorId.class);
        for (ASTVariableDeclaratorId param : params) {
            String paramName = param.getImage();
            Pattern paramNamePattern =
                    Pattern.compile(".*@param\\s+" + paramName + ".*", Pattern.DOTALL);

            if (!paramNamePattern.matcher(commentContent).matches()) {
                addViolationWithMessage(data, method, "参数\"" + paramName + "\"缺少javadoc注释");
            }
        }

        // 返回值必须要有javadoc注释
        if (!method.isVoid() && !RETURN_PATTERN.matcher(commentContent).matches()) {
            addViolationWithMessage(data, method, "返回值缺少javadoc注释");
        }

        // 异常必须要有javadoc注释
        ASTNameList nameList = method.getThrows();
        if (null != nameList) {
            List<ASTName> exceptions = nameList.findDescendantsOfType(ASTName.class);
            for (ASTName exception : exceptions) {
                String exceptionName = exception.getImage();
                Pattern exceptionPattern =
                        Pattern.compile(".*@throws\\s+" + exceptionName + ".*", Pattern.DOTALL);

                if (!exceptionPattern.matcher(commentContent).matches()) {
                    addViolationWithMessage(data, method, "异常\"" + exceptionName + "\"缺少javadoc注释");
                }
            }
        }
    }

    @Override
    public Object visit(ASTCompilationUnit cUnit, Object data) {
        assignCommentsToDeclarations(cUnit);

        return super.visit(cUnit, data);
    }

}
