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

class VersionSuite extends munit.FunSuite {

  test("Version.unapply extracts version with different coordinates") {
    assertEquals(Version.unapply("1"), Some((Some(1), None, None)))
    assertEquals(Version.unapply("1.2"), Some((Some(1), Some(2), None)))
    assertEquals(Version.unapply("1.2.3"), Some((Some(1), Some(2), Some(3))))
  }

  test("Version.unapply returns None if version doesn't match") {
    assertEquals(Version.unapply("1-patch"), None)
    assertEquals(Version.unapply("1.2-patch"), None)
    assertEquals(Version.unapply("1.2.3-patch"), None)
  }

}
