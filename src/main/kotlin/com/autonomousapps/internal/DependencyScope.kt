// Copyright (c) 2024. Tony Robalik.
// SPDX-License-Identifier: Apache-2.0
package com.autonomousapps.internal

import com.autonomousapps.internal.utils.lowercase

/** Well-known dependency scopes such as `"implementation"`, `"api"`, etc.. */
internal enum class DependencyScope(val value: String) {

  /*
   * IMPORTANT: The declaration order below is necessary for correct function. We compare the `value` strings by suffix,
   * so we need to look at the longest suffix first (compileOnlyApi -> compileOnly -> api).
   */

  COMPILE_ONLY_API("compileOnlyApi"),
  COMPILE_ONLY("compileOnly"),
  API("api"),
  IMPLEMENTATION("implementation"),
  RUNTIME_ONLY("runtimeOnly");

  companion object {
    /**
     * Returns the source set name for [configurationName]. For example, if [configurationName] is
     * `"testImplementation"`, returns `"test"`. If [configurationName] has an unknown [DependencyScope], returns
     * `null`.
     */
    fun sourceSetName(configurationName: String): String? {
      val scope = getScope(configurationName) ?: return null
      val sourceSet = configurationName.substring(0, configurationName.length - scope.value.length)
      return sourceSet.ifEmpty { "main" }
    }

    /** Returns true if [sourceSetName] ends with "test" or "Test". */
    fun isTestRelated(sourceSetName: String): Boolean {
      return sourceSetName.endsWith("test") || sourceSetName.endsWith("Test")
    }

    private fun getScope(configurationName: String): DependencyScope? {
      return values().firstOrNull { scope ->
        configurationName.lowercase().endsWith(scope.value.lowercase())
      }
    }
  }
}
