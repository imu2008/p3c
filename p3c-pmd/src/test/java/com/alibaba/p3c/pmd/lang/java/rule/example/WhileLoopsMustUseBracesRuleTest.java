package com.alibaba.p3c.pmd.lang.java.rule.example;

/**
 * @author changle.lq@alibaba-inc.com 2016/11/15 下午7:18
 */
public class WhileLoopsMustUseBracesRuleTest {
    public void doSomething(int x) {
        while (true)
            x++;
    }
}
