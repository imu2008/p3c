package com.alibaba.p3c.pmd.lang.java.rule.comments;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.alibaba.p3c.pmd.lang.java.rule.util.CommentUtils;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTEnumConstant;
import net.sourceforge.pmd.lang.java.ast.ASTEnumDeclaration;
import net.sourceforge.pmd.lang.java.rule.comments.AbstractCommentRule;

/**
 * 【强制】所有的枚举类型字段必须要有注释，说明每个数据项的用途。
 * 
 * @author keriezhang
 *
 */
public class EnumConstantsMustHaveCommentRule extends AbstractCommentRule {

    @Override
    public Object visit(ASTCompilationUnit cUnit, Object data) {
        SortedMap<Integer, Node> itemsByLineNumber = this.orderedCommentsAndEnumDeclarations(cUnit);

        // 判断ASTEnumDeclaration与ASTEnumConstant之间是否有注释
        boolean isPreviousEnumDecl = false;

        for (Entry<Integer, Node> entry : itemsByLineNumber.entrySet()) {
            Node value = entry.getValue();

            if (value instanceof ASTEnumDeclaration) {
                isPreviousEnumDecl = true;
            } else if (value instanceof ASTEnumConstant && isPreviousEnumDecl) {
                addViolation(data, value);
                isPreviousEnumDecl = false;
            } else {
                isPreviousEnumDecl = false;
            }
        }

        return super.visit(cUnit, data);
    }

    private SortedMap<Integer, Node> orderedCommentsAndEnumDeclarations(ASTCompilationUnit cUnit) {
        SortedMap<Integer, Node> itemsByLineNumber = new TreeMap<>();

        List<ASTEnumDeclaration> enumDecl = cUnit.findDescendantsOfType(ASTEnumDeclaration.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, enumDecl);

        List<ASTEnumConstant> contantDecl = cUnit.findDescendantsOfType(ASTEnumConstant.class);
        CommentUtils.addNodesToSortedMap(itemsByLineNumber, contantDecl);

        CommentUtils.addNodesToSortedMap(itemsByLineNumber, cUnit.getComments());

        return itemsByLineNumber;
    }

}
