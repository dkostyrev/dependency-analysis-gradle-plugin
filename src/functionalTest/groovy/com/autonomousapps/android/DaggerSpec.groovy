// Copyright (c) 2024. Tony Robalik.
// SPDX-License-Identifier: Apache-2.0
package com.autonomousapps.android

import com.autonomousapps.android.projects.DaggerProject
import com.autonomousapps.model.Advice

import static com.autonomousapps.advice.truth.BuildHealthSubject.buildHealth
import static com.autonomousapps.utils.Runner.build
import static com.google.common.truth.Truth.assertAbout

final class DaggerSpec extends AbstractAndroidSpec {

  // https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/issues/479
  @SuppressWarnings('GroovyAssignabilityCheck')
  def "can introspect dagger annotation processor (#gradleVersion AGP #agpVersion)"() {
    given:
    def projectName = 'lib'
    def project = new DaggerProject(agpVersion, projectName)
    gradleProject = project.gradleProject

    when:
    build(gradleVersion, gradleProject.rootDir, 'buildHealth')

    then:
    assertAbout(buildHealth())
      .that([actualProjectAdvice(projectName)] as Set<Advice>)
      .isEquivalentIgnoringModuleAdviceAndWarnings([project.expectedAdvice])

    where:
    [gradleVersion, agpVersion] << gradleAgpMatrix()
  }
}
