import scala.util.Random
import sys.process._

val hash = settingKey[String]("")

Global / hash := Random.alphanumeric.take(5).mkString

TaskKey[Unit]("removeRemoteRepository") := {
  Seq(
    "curl",
    "-sS",
    "-X",
    "DELETE",
    "-H",
    s"Authorization: token ${sys.env("GITHUB_TOKEN")}",
    s"https://api.github.com/repos/alejandrohdezma/sbt-ci-tag-next-version-${hash.value}"
  ) !
}

TaskKey[Unit]("createRemoteRepository") := {
  Seq(
    "curl",
    "-sS",
    "-H",
    "Accept: application/vnd.github.v3+json",
    "-H",
    s"Authorization: token ${sys.env("GITHUB_TOKEN")}",
    "https://api.github.com/user/repos",
    "-d",
    s"""{"name":"sbt-ci-tag-next-version-${hash.value}", "private": true}"""
  ) !

  s"git remote add origin https://github.com/alejandrohdezma/sbt-ci-tag-next-version-${hash.value}.git" !

  s"git push -u https://${sys.env("GITHUB_TOKEN")}@github.com/alejandrohdezma/sbt-ci-tag-next-version-${hash.value}.git main" !
}

TaskKey[Unit]("checkRemoteTags") := {
  val remoteTags = ("git ls-remote --tags origin" !!).trim()

  assert(remoteTags.nonEmpty, "No tags found in remote")

  val sha      = ("git rev-parse HEAD" !!).trim()
  val expected = s"$sha\trefs/tags/v1.1.0"

  assert(remoteTags == expected, s"Obtained: $remoteTags\nExpected: $expected")
}
