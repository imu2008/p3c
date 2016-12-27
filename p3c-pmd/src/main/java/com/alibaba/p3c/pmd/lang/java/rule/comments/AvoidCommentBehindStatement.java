package com.alibaba.p3c.pmd.lang.java.rule.comments;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.alibaba.p3c.pmd.lang.java.rule.util.CommentUtils;

import java.util.Map.Entry;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTEnumConstant;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaNode;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.rule.comments.AbstractCommentRule;

/**
 * 【强制】方法内部单行注释，在被注释语句上方另起一行，使用//注释。方法内部多行注释使用javadoc注释。注意与代码对齐。
 * 
 * @author keriezhang
 * @date 2016年12月14日 上午11:08:29
 *
 */
public class AvoidCommentBehindStatement extends AbstractCommentRule {

    @Override
    public Object visit(ASTCompilationUnit cUnit, Object data) {
        SortedMap<Integer, Node> itemsByLineNumber = orderedCommentsAndExpressions(cUnit);
        AbstractJavaNode lastNode = null;

        for (Entry<Integer, Node> entry : itemsByLineNumber.entrySet()) {
            Node value = entry.getValue();
            if (value instanceof AbstractJavaNode) {
                AbstractJavaNode node = (AbstractJavaNode) value;
                lastNode = node;
            } else if (value instanceof Comment) {
                Comment comment = (Comment) value;
                if (lastNode != null && (comment.getBeginLine() == lastNode.getBeginLine())
                        && (comment.getEndColumn() > lastNode.getBeginColumn())) {
                    addViolation(data, lastNode);
                }
            }
        }

        return super.visit(cUnit, data);
    }

    /**
     * 目前只检测了表达式后面是否包含注释，比较复杂的地方，还没有做到
     * 
     * @param cUnit
     * @return
     */
    protected SortedMap<Integer, Node> orderedCommentsAndExpressions(ASTCompilationUnit cUnit) {

        SortedMap<Integer, Node> itemsByLineNumber = new TreeMap<>();

        List<ASTExpression> expressionNodes = cUnit.findDescendantsOfType(ASTExpression.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, expressionNodes);

        // 类属性同行注释的判断
        List<ASTFieldDeclaration> fieldNodes =
                cUnit.findDescendantsOfType(ASTFieldDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, fieldNodes);

        // 枚举字段同行注释的判断
        List<ASTEnumConstant> enumConstantNodes =
                cUnit.findDescendantsOfType(ASTEnumConstant.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, enumConstantNodes);

        CommentUtils.addNodesToSortedMap(itemsByLineNumber, cUnit.getComments());

        return itemsByLineNumber;
    }

}
