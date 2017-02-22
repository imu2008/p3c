package com.alibaba.p3c.pmd;

import net.sourceforge.pmd.PMD;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地开发测试专用类，选择要检查的文件，以及对应的规则
 * 
 * 通过执行Run As Java Application或Debug As Java Application
 * 
 * @author keriezhang
 *
 */
public class AliPmd {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        list.add("-d");
        list.add("/Users/lq/project/gitrepos/collinafacade/collinafacade-common/src/main/java/com/alibaba/collinafacade/common/exception/CollinaFacadeCheckException.java");

        list.add("-R");
        list.add("/Users/lq/project/gitrepos/p3c/p3c-pmd/src/main/resources/rulesets/java/ali-naming.xml");

        // list.add("-f");
        // list.add("xml");

        String[] myArgs = list.toArray(new String[list.size()]);
        PMD.main(myArgs);
    }
}


