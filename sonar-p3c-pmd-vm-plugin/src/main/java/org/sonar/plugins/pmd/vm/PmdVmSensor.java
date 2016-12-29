/*
 * SonarQube PMD Plugin Copyright (C) 2012 SonarSource sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02
 */
package org.sonar.plugins.pmd.vm;

import com.google.common.collect.Iterables;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.XmlParserException;

import java.io.File;

public class PmdVmSensor implements Sensor {

    private static final Logger LOG = LoggerFactory.getLogger(PmdVmSensor.class);

    private final RulesProfile profile;
    private final PmdVmExecutor executor;
    private final PmdVmViolationRecorder pmdVmViolationRecorder;
    private final FileSystem fs;

    public PmdVmSensor(RulesProfile profile, PmdVmExecutor executor,
            PmdVmViolationRecorder pmdVmViolationRecorder, FileSystem fs) {
        this.profile = profile;
        this.executor = executor;
        this.pmdVmViolationRecorder = pmdVmViolationRecorder;
        this.fs = fs;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        boolean result = hasFilesToCheck(Vm.KEY, PmdVmRulesDefinition.REPOSITORY_KEY);
        return result;
    }

    private boolean hasFilesToCheck(String language, String repositoryKey) {
        FilePredicates predicates = fs.predicates();
        Iterable<File> files = fs.files(predicates.hasLanguage(language));
        return !Iterables.isEmpty(files)
                && !profile.getActiveRulesByRepository(repositoryKey).isEmpty();
    }

    @Override
    public void analyse(Project project, SensorContext context) {
        try {
            Report report = executor.execute();
            for (RuleViolation violation : report) {
                pmdVmViolationRecorder.saveViolation(violation);
            }
        } catch (Exception e) {
            throw new XmlParserException(e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
