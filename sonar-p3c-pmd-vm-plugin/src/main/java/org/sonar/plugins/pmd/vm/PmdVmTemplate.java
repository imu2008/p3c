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
package org.sonar.plugins.pmd.vm;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Closeables;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.SourceCodeProcessor;
import net.sourceforge.pmd.lang.vm.VmLanguageModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class PmdVmTemplate {

  private static final Logger LOG = LoggerFactory.getLogger(PmdVmTemplate.class);

  private SourceCodeProcessor processor;
  private PMDConfiguration configuration;

  public static PmdVmTemplate create(ClassLoader classloader, Charset charset) {
    PMDConfiguration configuration = new PMDConfiguration();
    VmLanguageModule vmModule = new VmLanguageModule();
    configuration.setDefaultLanguageVersion(vmModule.getDefaultVersion());
    configuration.setClassLoader(classloader);
    configuration.setSourceEncoding(charset.name());
    SourceCodeProcessor processor = new SourceCodeProcessor(configuration);
    return new PmdVmTemplate(configuration, processor);
  }

  @VisibleForTesting
  PmdVmTemplate(PMDConfiguration configuration, SourceCodeProcessor processor) {
    this.configuration = configuration;
    this.processor = processor;
  }

  @VisibleForTesting
  PMDConfiguration configuration() {
    return configuration;
  }

  public void process(File file, RuleSets rulesets, RuleContext ruleContext) {
    ruleContext.setSourceCodeFilename(file.getAbsolutePath());
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      processor.processSourceCode(inputStream, rulesets, ruleContext);
    } catch (Exception e) {
      LOG.error("Fail to execute PMD. Following file is ignored: " + file, e);
    } finally {
      Closeables.closeQuietly(inputStream);
    }
  }
}
