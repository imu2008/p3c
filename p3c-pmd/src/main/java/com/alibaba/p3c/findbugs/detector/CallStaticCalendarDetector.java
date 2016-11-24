package com.alibaba.p3c.findbugs.detector;

import org.apache.bcel.classfile.Field;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.detect.StaticCalendarDetector;

/**
 * @author caikang.ck(骏烈)
 * @date 2016/11/17 上午9:58
 * @email caikang.ck@alibaba-inc.com
 * @phone 15010667215
 * @since 2016/11/17
 */
public class CallStaticCalendarDetector extends StaticCalendarDetector {

    /**
     * Creates a new instance of this Detector.
     *
     * @param aReporter {@link BugReporter} instance to report found problems to.
     */
    public CallStaticCalendarDetector(BugReporter aReporter) {
        super(aReporter);
    }

    @Override
    public void visit(Field aField) {
        //按照规约要求，可以定义为静态字段，但是调用的时候必须加锁
    }
}
