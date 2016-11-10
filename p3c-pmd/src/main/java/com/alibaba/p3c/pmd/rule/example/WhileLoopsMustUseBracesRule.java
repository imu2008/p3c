package com.alibaba.p3c.pmd.rule.example;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTWhileStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class WhileLoopsMustUseBracesRule extends AbstractJavaRule{
	
	@Override
	public Object visit(ASTWhileStatement node, Object data) {
		Node firstStmt = node.jjtGetChild(1);
		if (!hasBlockAsFirstChild(firstStmt)) {
			addViolation(data, node);
		}
		return super.visit(node, data);
	}
	
	private boolean hasBlockAsFirstChild(Node node) {
		return (node.jjtGetNumChildren() != 0 && (node.jjtGetChild(0) instanceof ASTBlock));
	}

}
