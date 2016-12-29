package org.sonar.plugins.pmd.vm;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.squidbridge.rules.ExternalDescriptionLoader;
import org.sonar.squidbridge.rules.PropertyFileLoader;
import org.sonar.squidbridge.rules.SqaleXmlLoader;

/**
 * Vm规则定义
 * 
 * @author keriezhang
 * @date 2016年12月28日 上午10:31:39
 *
 */
public class PmdVmRulesDefinition implements RulesDefinition {

    public static final String REPOSITORY_KEY = "pmd-vm";
    public static final String REPOSITORY_VM_NAME = "PMD vm rule";

    @Override
    public void define(Context context) {
        NewRepository repositoryVm =
                context.createRepository(REPOSITORY_KEY, Vm.KEY).setName(REPOSITORY_VM_NAME);

        RulesDefinitionXmlLoader ruleLoaderVm = new RulesDefinitionXmlLoader();
        ruleLoaderVm.load(repositoryVm, PmdVmRulesDefinition.class
                .getResourceAsStream("/org/sonar/plugins/pmd/vm/rules-vm.xml"), "UTF-8");
        
        ExternalDescriptionLoader.loadHtmlDescriptions(repositoryVm, "/org/sonar/l10n/pmd/rules/pmd/vm");
        
        //sonar服务端每条规则显示名称的定义文件
        PropertyFileLoader.loadNames(repositoryVm, PmdVmRulesDefinition.class.getResourceAsStream("/org/sonar/l10n/pmd-vm.properties"));
        
        //sonar中针对每条规则的质量调整预期寿命(Quality Adjusted Life Expectancy)定义文件
        SqaleXmlLoader.load(repositoryVm, "/com/sonar/sqale/pmd-vm-model.xml");
        
        repositoryVm.done();
    }
}
