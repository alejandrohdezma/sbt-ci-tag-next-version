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

import scala.language.postfixOps
import scala.sys.process._

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

import sbtdynver.DynVerPlugin
import sbtdynver.DynVerPlugin.autoImport._

object CiTagNextVersionPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = JvmPlugin && DynVerPlugin

  object autoImport {

    val ciTagNextMajorVersion = taskKey[Unit]("Tags the next version by increasing the major coordinate and pushes it")

    val ciTagNextMinorVersion = taskKey[Unit]("Tags the next version by increasing the minor coordinate and pushes it")

    val ciTagNextPatchVersion = taskKey[Unit]("Tags the next version by increasing the patch coordinate and pushes it")

  }

  import autoImport._

  override def buildSettings: Seq[Def.Setting[_]] = Seq(
    ciTagNextMajorVersion := tagNextVersion(Increment.Major).value,
    ciTagNextMinorVersion := tagNextVersion(Increment.Minor).value,
    ciTagNextPatchVersion := tagNextVersion(Increment.Patch).value
  )

  def tagNextVersion(increment: Increment) = Def.task {
    val git    = dynverInstance.value
    val logger = streams.value.log

    if (git.isDirty()) {
      sys.error("Repository is dirty")
    }

    val previousVersion = git.previousVersion.getOrElse {
      sys.error("Unable to find previous version")
    }

    logger.info(s"Previous version found: $previousVersion")

    val newVersion = increment(previousVersion)

    val newTag = s"${git.tagPrefix}$newVersion"

    s"git tag $newTag" !! logger

    logger.info(s"Tag $newTag has been created")

    val remotes = ("git remote" !! logger).split("\n").toList

    if (remotes.size != 1) // scalafix:ok DisableSyntax.!=
      sys.error(s"Only repositories with one remote are supported. Remotes found: ${remotes.mkString(", ")}")

    val remote = remotes.head /* scalafix:ok */

    val remoteUri = {
      val url = s"git config --get remote.$remote.url" !!

      sys.env.get("GITHUB_TOKEN").filter(_ => url.startsWith("https://")).fold(url) { token =>
        s"https://$token@" + url.drop(8)
      }
    }

    val pushed = s"git push $remoteUri $newTag" !!

    logger.info(pushed)

    logger.info(s"Tag $newTag has been pushed to remote $remote")
  }

}
