/**
 * Copyright © 2024 Apple Inc. and the Pkl project authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pkl.server

import java.nio.file.Path
import kotlin.Pair
import kotlin.reflect.KClass
import org.junit.platform.commons.annotation.Testable
import org.pkl.commons.test.AbstractSnippetTestsEngine
import org.pkl.core.*
import org.pkl.core.http.HttpClient
import org.pkl.core.module.ModuleKeyFactories

@Testable class BinaryEvaluatorSnippetTests

class BinaryEvaluatorSnippetTestsEngine : AbstractSnippetTestsEngine() {
  override val testClass: KClass<*> = BinaryEvaluatorSnippetTests::class

  override val snippetsDir: Path = rootProjectDir.resolve("pkl-server/src/test/files/SnippetTests")

  private val evaluator =
    BinaryEvaluator(
      StackFrameTransformers.empty,
      SecurityManagers.defaultManager,
      HttpClient.dummyClient(),
      Loggers.stdErr(),
      listOf(ModuleKeyFactories.file),
      listOf(),
      mapOf(),
      mapOf(),
      null,
      null,
      null,
      null
    )

  override fun generateOutputFor(inputFile: Path): Pair<Boolean, String> {
    val bytes = evaluator.evaluate(ModuleSource.path(inputFile), null)
    return true to bytes.debugRendering.stripFilePaths().addLeadingCommentAndTrailingNewline()
  }

  private fun String.addLeadingCommentAndTrailingNewline(): String =
    "#file: noinspection YAMLIncompatibleTypes\n$this\n"
}
