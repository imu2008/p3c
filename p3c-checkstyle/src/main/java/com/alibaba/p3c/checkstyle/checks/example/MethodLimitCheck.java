package com.alibaba.p3c.checkstyle.checks.example;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class MethodLimitCheck extends AbstractCheck{
	
	   private int max = 13;
	   
	    public int[] getDefaultTokens()
	    {
	        return new int[]{TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF};
	    }
	 
	    public void visitToken(DetailAST ast)
	    {
	        // find the OBJBLOCK node below the CLASS_DEF/INTERFACE_DEF
	        DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
	        // count the number of direct children of the OBJBLOCK
	        // that are METHOD_DEFS
	        int methodDefs = objBlock.getChildCount(TokenTypes.METHOD_DEF);
	        // report error if limit is reached
	        if (methodDefs > max) {
	            log(ast.getLineNo(),
	                "too many methods, only " + max + " are allowed");
	        }
	   }

}
