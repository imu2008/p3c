/*
 * SonarQube PMD Plugin
 * Copyright (C) 2012 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.pmd;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonar.plugins.java.Java;
import org.sonar.squidbridge.rules.ExternalDescriptionLoader;
import org.sonar.squidbridge.rules.PropertyFileLoader;
import org.sonar.squidbridge.rules.SqaleXmlLoader;

public final class PmdRulesDefinition implements RulesDefinition {

  public PmdRulesDefinition() {
    // do nothing
  }

  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(PmdConstants.REPOSITORY_KEY, Java.KEY)
      .setName(PmdConstants.REPOSITORY_NAME);
    //rules.xml为配置引用PMD jar包中哪些具体规则的文件
    //l10n/pmd/rules/pmd目录下存放的为在sonar服务端展现每条具体规则的描述片段
    extractRulesData(repository, "/org/sonar/plugins/pmd/rules.xml", "/org/sonar/l10n/pmd/rules/pmd");

    repository.done();
  }

  static void extractRulesData(NewRepository repository, String xmlRulesFilePath, String htmlDescriptionFolder) {
    RulesDefinitionXmlLoader ruleLoader = new RulesDefinitionXmlLoader();
    ruleLoader.load(repository, PmdRulesDefinition.class.getResourceAsStream(xmlRulesFilePath), "UTF-8");
    ExternalDescriptionLoader.loadHtmlDescriptions(repository, htmlDescriptionFolder);
    //sonar服务端每条规则显示名称的定义文件
    PropertyFileLoader.loadNames(repository, PmdRulesDefinition.class.getResourceAsStream("/org/sonar/l10n/pmd.properties"));
    //sonar中针对每条规则的质量调整预期寿命(Quality Adjusted Life Expectancy)定义文件
    SqaleXmlLoader.load(repository, "/com/sonar/sqale/pmd-model.xml");
  }
}
