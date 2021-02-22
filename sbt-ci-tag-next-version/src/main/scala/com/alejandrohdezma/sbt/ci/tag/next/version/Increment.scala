/*
 * Copyright 2021 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.sbt.ci.tag.next.version

sealed abstract class Increment(private val increment: PartialFunction[String, String]) {

  /**
   * Increments the provided version with this specific increment.
   */
  final def apply(version: String): String =
    increment.applyOrElse(version, (version: String) => sys.error(s"Unable to extract version from $version"))

}

object Increment {

  case object Major
      extends Increment({
        case Version(Some(major), Some(_), Some(_)) => s"${major + 1}.0.0"
        case Version(Some(major), Some(_), None)    => s"${major + 1}.0"
        case Version(Some(major), None, None)       => s"${major + 1}"
      })

  case object Minor
      extends Increment({
        case Version(Some(major), Some(minor), Some(_)) => s"$major.${minor + 1}.0"
        case Version(Some(major), Some(minor), None)    => s"$major.${minor + 1}"
        case Version(Some(major), None, None)           => s"$major.1"
      })

  case object Patch
      extends Increment({
        case Version(Some(major), Some(minor), Some(patch)) => s"$major.$minor.${patch + 1}"
        case Version(Some(major), Some(minor), None)        => s"$major.$minor.1"
        case Version(Some(major), None, None)               => s"$major.0.1"
      })

}
