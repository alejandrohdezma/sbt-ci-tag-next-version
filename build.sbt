ThisBuild / scalaVersion                  := "2.12.12"
ThisBuild / organization                  := "com.alejandrohdezma"
ThisBuild / skip in publish               := true
ThisBuild / testFrameworks                += new TestFramework("munit.Framework")
ThisBuild / parallelExecution in Test     := false
ThisBuild / pluginCrossBuild / sbtVersion := "1.2.8"

addCommandAlias("ci-test", "fix --check; mdoc; test; scripted")
addCommandAlias("ci-docs", "github; mdoc; headerCreateAll")
addCommandAlias("ci-publish", "github; ci-release")

lazy val documentation = project
  .enablePlugins(MdocPlugin)
  .settings(mdocOut := file("."))

lazy val `sbt-ci-tag-next-version` = project
  .enablePlugins(SbtPlugin)
  .settings(skip in publish := false)
  .settings(scriptedLaunchOpts += s"-Dplugin.version=${version.value}")
  .settings(scriptedBufferLog := false)
  .settings(addSbtPlugin("com.dwijnand" % "sbt-dynver" % "4.1.1"))
  .settings(libraryDependencies += "org.scalameta" %% "munit" % "0.7.22" % Test)
