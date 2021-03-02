# SBT companion plugin for [sbt-ci-release](https://github.com/olafurpg/sbt-ci-release) for fully automated releases

[![][github-action-badge]][github-action] [![][maven-badge]][maven] [![][steward-badge]][steward]

This plugin provides a set of SBT tasks that can be used to auto-increase the version number by using git tags.

## Installation

Add the following line to your `plugins.sbt` file:

```sbt
addSbtPlugin("com.alejandrohdezma" % "sbt-ci-tag-next-version" % "@VERSION@")
```

## Usage

This plugin adds a set of tasks to your SBT project to allow auto-tagging next releases. Each of them detects the last version from the repository git tags and automatically tags the following version tag (both locally and remote).

- `ciTagNextMajorVersion`: tags the next version by increasing the major coordinate and pushes it. For example: if the previous tag is `v1.1.0`, the next one will be `v2.0.0`.
- `ciTagNextMinorVersion`: tags the next version by increasing the minor coordinate and pushes it. For example: if the previous tag is `v1.1.0`, the next one will be `v1.2.0`.
- `ciTagNextPatchVersion`: tags the next version by increasing the patch coordinate and pushes it. For example: if the previous tag is `v1.1.0`, the next one will be `v1.1.1`.

This plugin uses [sbt-dynver](https://github.com/dwijnand/sbt-dynver) under the hood for fetching the last stable release, and thus it will be added as a dependency to your build.

> **Note on SemVer**
>
> The plugin will try to honor the version system your project is using. For example, if you are using `v1`, `v2`... and run `sbt ciTagNextMajorVersion`, a `v3` tag will be created. But please take into account that if you choose any of the other commands a new coordinate will be added (`v2.1` or `v2.0.1`).

## Tag requirements (copied from [sbt-dynver](https://github.com/dwijnand/sbt-dynver/blob/master/README.md#tag-requirements))

In order to be recognized by [sbt-dynver](https://github.com/dwijnand/sbt-dynver), by default tags must begin with the lowercase letter 'v' followed by a digit.

If you're not seeing what you expect, then either start with this:

```bash
git tag -a v0.0.1 -m "Initial version tag for sbt-dynver"
```

or change the value of `ThisBuild / dynverVTagPrefix` to remove the requirement for the v-prefix:

```sbt
ThisBuild / dynverVTagPrefix := false
```

or, more generally, use `ThisBuild / dynverTagPrefix` to fully customising tag prefixes, for example:

```sbt
ThisBuild / dynverTagPrefix := "foo-" // our tags have the format foo-<version>, e.g. foo-1.2.3
```

[github-action]: https://github.com/alejandrohdezma/sbt-ci-tag-next-version/actions
[github-action-badge]: https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2Falejandrohdezma%2Fsbt-ci-tag-next-version%2Fbadge%3Fref%3Dmaster&style=flat
[maven]: https://search.maven.org/search?q=g:%20com.alejandrohdezma%20AND%20a:sbt-ci-tag-next-version
[maven-badge]: https://maven-badges.herokuapp.com/maven-central/com.alejandrohdezma/sbt-ci-tag-next-version/badge.svg?kill_cache=1
[steward]: https://scala-steward.org
[steward-badge]: https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=
