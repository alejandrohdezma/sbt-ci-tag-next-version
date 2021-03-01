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

class IncrementSuite extends munit.FunSuite {

  test("Increment.apply respects previous version scheme") {
    assertEquals(Increment.Major("1"), "2")
    assertEquals(Increment.Minor("1"), "1.1")
    assertEquals(Increment.Patch("1"), "1.0.1")
    assertEquals(Increment.Major("1.2"), "2.0")
    assertEquals(Increment.Minor("1.2"), "1.3")
    assertEquals(Increment.Patch("1.2"), "1.2.1")
    assertEquals(Increment.Major("1.3.1"), "2.0.0")
    assertEquals(Increment.Minor("1.3.1"), "1.4.0")
    assertEquals(Increment.Patch("1.3.1"), "1.3.2")
  }

  test("Increment.apply fails if version cannot be extracted") {
    interceptMessage[RuntimeException]("Unable to extract version from 1.1.1-patch") {
      Increment.Major("1.1.1-patch")
    }
  }

}
