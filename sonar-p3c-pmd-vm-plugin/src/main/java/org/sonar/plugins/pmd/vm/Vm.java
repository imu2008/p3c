package org.sonar.plugins.pmd.vm;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.AbstractLanguage;

/**
 * Vm模板语言定义
 * 
 * @author keriezhang
 * @date 2016年12月28日 上午10:19:31
 *
 */
public class Vm extends AbstractLanguage {
    /**
     * VM key
     */
    public static final String KEY = "vm";

    /**
     * VM template name
     */
    public static final String NAME = "VM";


    /**
     * Key of the file suffix parameter
     */
    public static final String FILE_SUFFIXES_KEY = "sonar.vm.file.suffixes";

    /**
     * Default Java files knows suffixes
     */
    public static final String DEFAULT_FILE_SUFFIXES = ".vm";

    /**
     * Settings of the plugin.
     */
    private final Settings settings;

    /**
     * Default constructor
     */
    public Vm(Settings settings) {
        super(KEY, NAME);
        this.settings = settings;
    }

    @Override
    public String[] getFileSuffixes() {
        String[] suffixes = StringUtils.split(DEFAULT_FILE_SUFFIXES, ",");
        return suffixes;
    }

}
