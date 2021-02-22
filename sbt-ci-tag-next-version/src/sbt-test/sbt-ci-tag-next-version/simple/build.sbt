import sys.process._

TaskKey[Unit]("removeRemoteRepository") := {
  val sha = ("git rev-parse --short HEAD" !!).trim()

  Seq(
    "curl",
    "-sS",
    "-X",
    "DELETE",
    "-H",
    s"Authorization: token ${sys.env("GITHUB_TOKEN")}",
    s"https://api.github.com/repos/alejandrohdezma/sbt-ci-tag-next-version-$sha"
  ) !
}

TaskKey[Unit]("createRemoteRepository") := {
  val sha = ("git rev-parse --short HEAD" !!).trim()

  Seq(
    "curl",
    "-sS",
    "-H",
    "Accept: application/vnd.github.v3+json",
    "-H",
    s"Authorization: token ${sys.env("GITHUB_TOKEN")}",
    "https://api.github.com/user/repos",
    "-d",
    s"""{"name":"sbt-ci-tag-next-version-$sha", "private": true}"""
  ) !

  s"git remote add origin git@github.com:alejandrohdezma/sbt-ci-tag-next-version-$sha.git" !
}

TaskKey[Unit]("checkRemoteTags") := {
  val remoteTags = ("git ls-remote --tags origin" !!).trim()

  assert(remoteTags.nonEmpty, "No tags found in remote")

  val sha      = ("git rev-parse HEAD" !!).trim()
  val expected = s"$sha\trefs/tags/v1.1.0"

  assert(remoteTags == expected, s"Obtained: $remoteTags\nExpected: $expected")
}
