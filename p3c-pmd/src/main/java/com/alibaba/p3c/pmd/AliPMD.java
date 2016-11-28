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
public class AliPMD {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        list.add("-d");
        list.add("/Users/keriezhang/Documents/program/gitlab/"
                + "p3c/alibaba-checkstyle-checks/src/test/java/com/alibaba/pmd/comments/"
                + "MethodOfInterfaceMustUseJavaDocTest.java");

        list.add("-R");
        list.add("/Users/keriezhang/Documents/program/gitlab/"
                + "p3c/alibaba-checkstyle-checks/src/main/resources/com/alibaba/pmd/"
                + "abstract-method-and-interface-must-use-java-doc.xml");

        // list.add("-f");
        // list.add("xml");

        String[] myArgs = list.toArray(new String[list.size()]);
        PMD.main(myArgs);
    }
}


