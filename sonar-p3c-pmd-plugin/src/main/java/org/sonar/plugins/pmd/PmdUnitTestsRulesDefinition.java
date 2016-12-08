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
import org.sonar.plugins.java.Java;

public final class PmdUnitTestsRulesDefinition implements RulesDefinition {

  public PmdUnitTestsRulesDefinition() {
    // Do nothing
  }

  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(PmdConstants.TEST_REPOSITORY_KEY, Java.KEY)
      .setName(PmdConstants.TEST_REPOSITORY_NAME);
    //rules-unit-tests.xml为配置引用PMD jar包中哪些具体规则的文件
    //l10n/pmd/rules/pmd-unit-tests目录下存放的为在sonar服务端展现每条具体规则的描述片段
    PmdRulesDefinition.extractRulesData(repository, "/org/sonar/plugins/pmd/rules-unit-tests.xml", "/org/sonar/l10n/pmd/rules/pmd-unit-tests");

    repository.done();
  }
}
