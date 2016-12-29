package org.sonar.plugins.pmd.vm;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.server.rule.RulesDefinition.Rule;
import org.sonar.plugins.pmd.vm.PmdVmRulesDefinition;
import org.sonar.plugins.pmd.vm.Vm;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class PmdVmRulesDefinitionTest {

    @Test
    public void test() {
        PmdVmRulesDefinition vmDefinition = new PmdVmRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        vmDefinition.define(context);
        RulesDefinition.Repository repository = context.repository(PmdVmRulesDefinition.REPOSITORY_KEY);

        assertThat(repository.name()).isEqualTo(PmdVmRulesDefinition.REPOSITORY_VM_NAME);
        assertThat(repository.language()).isEqualTo(Vm.KEY);

        List<Rule> rules = repository.rules();
        assertThat(rules).hasSize(1);

        for (Rule rule : rules) {
            assertThat(rule.key()).isNotNull();
            assertThat(rule.internalKey()).isNotNull();
            assertThat(rule.name()).isNotNull();
            assertThat(rule.htmlDescription()).isNotNull();
            assertThat(rule.severity()).isNotNull();

            for (Param param : rule.params()) {
                assertThat(param.name()).isNotNull();
                assertThat(param.description())
                        .overridingErrorMessage("Description is not set for parameter '"
                                + param.name() + "' of rule '" + rule.key())
                        .isNotNull();
            }

            if (!"XPathRule".equals(rule.key())) {
                assertThat(rule.debtRemediationFunction())
                        .overridingErrorMessage(
                                "Sqale remediation function is not set for rule '" + rule.key())
                        .isNotNull();
                assertThat(rule.debtSubCharacteristic())
                        .overridingErrorMessage(
                                "Sqale characteristic is not set for rule '" + rule.key())
                        .isNotNull();
            }
        }
    }
}
