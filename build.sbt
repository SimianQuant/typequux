import sbtcrossproject.{crossProject, CrossType}

def commonSettings(nameStr: String) = Seq(
  name := nameStr,
  organization := "com.simianquant",
  version := Settings.versions.project,
  scalaVersion := Settings.versions.scala,
  crossScalaVersions := Settings.crossScalaVersions,
  incOptions := incOptions.value.withLogRecompileOnMacro(false),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  wartremoverErrors in (Compile, compile) ++= Seq(
    Wart.ArrayEquals,
    Wart.Any,
    Wart.AnyVal,
    Wart.EitherProjectionPartial,
    Wart.Enumeration,
    Wart.Equals,
    Wart.ExplicitImplicitTypes,
    Wart.JavaConversions,
    Wart.JavaSerializable,
    Wart.Null,
    Wart.Option2Iterable,
    Wart.OptionPartial,
    Wart.Product,
    Wart.PublicInference,
    Wart.Return,
    Wart.Serializable,
    Wart.StringPlusAny,
    Wart.Throw,
    Wart.TryPartial
  ),
  scalacOptions in (Compile) ++= Settings.commonScalacOptions(scalaVersion.value),
  scalacOptions in (Compile, doc) ++= Settings.scalacDocOptions
)

lazy val commonJVMSettings = List(
  fork := true,
  scalacOptions ++= Seq(
    "-Ywarn-dead-code",
    "-Ywarn-value-discard"
  ),
  scalacOptions += (if (scalaVersion.value.startsWith("2.11")) "-target:jvm-1.6" else "-target:jvm-1.8")
)

lazy val commonJSSettings = List(
  coverageExcludedPackages := ".*"
)

lazy val commonNativeSettings = List(
  nativeMode := "release",
  coverageExcludedPackages := ".*",
  scalaVersion := "2.11.11",
  crossScalaVersions -= Settings.versions.scala
)

lazy val publishLocalCross = taskKey[Unit]("Publishes library locally for all scala versions")
lazy val publishSignedCross = taskKey[Unit]("Publishes library signed for all scala versions")

lazy val typequux = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("typequux"))
  .settings(commonSettings("typequux"))
  .settings(
    previewLaunchBrowser := false,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo := Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
    pomIncludeRepository := { _ =>
      false
    },
    pomExtra := (
      <url>https://harshad-deo.github.io/typequux/TypeQuux.html</url>
      <licenses>
        <license>
          <name>Apache-2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <connection>scm:git:git@github.com:harshad-deo/typequux.git</connection>
        <developerConnection>scm:git:git@github.com:harshad-deo/typequux.git</developerConnection>
        <url>git@github.com:harshad-deo/typequux.git</url>
      </scm>
      <developers>
        <developer>
          <id>harshad-deo</id>
          <name>Harshad Deo</name>
          <url>https://github.com/harshad-deo</url>
        </developer>
      </developers>
    )
  )
  .jvmSettings(commonJVMSettings)
  .jvmSettings(
    initialCommands := """|class Witness1[T](val x: T)
                          |object Witness1{
                          |  def apply[T](x: T): Witness1[T] = new Witness1(x)
                          |}
                          |class Witness2[T]
                          |import typequux._
                          |import Typequux._""".stripMargin,
    publishLocalCross := {
      runCommandAndRemaining("+typequuxJVM/publishLocal")(state.value)
    },
    publishSignedCross := {
      runCommandAndRemaining("+typequuxJVM/publishSigned")(state.value)
    }
  )
  .jsSettings(commonJSSettings)
  .jsSettings(
    publishLocalCross := {
      runCommandAndRemaining("+typequuxJS/publishLocal")(state.value)
    },
    publishSignedCross := {
      runCommandAndRemaining("+typequuxJS/publishSigned")(state.value)
    }
  )
  .nativeSettings(commonNativeSettings)
  .nativeSettings(
    publishSignedCross := {
      runCommandAndRemaining("typequuxNative/publishSigned")(state.value)
    }
  )

lazy val typequuxJVM = typequux.jvm
lazy val typequuxJS = typequux.js
lazy val typequuxNative = typequux.native

// taken from https://stackoverflow.com/questions/40741244/in-sbt-how-to-execute-a-command-in-task
def runCommandAndRemaining(command: String): State => State = { st: State =>
  import sbt.complete.Parser
  @annotation.tailrec
  def runCommand(command: String, state: State): State = {
    val nextState = Parser.parse(command, state.combinedParser) match {
      case Right(cmd) => cmd()
      case Left(msg)  => throw sys.error(s"Invalid programmatic input:\n$msg")
    }
    nextState.remainingCommands.toList match {
      case Nil          => nextState
      case head :: tail => runCommand(head, nextState.copy(remainingCommands = tail))
    }
  }
  runCommand(command, st.copy(remainingCommands = Nil)).copy(remainingCommands = st.remainingCommands)
}

lazy val crossTest = taskKey[Unit]("Test library against all scala versions")

lazy val jvmJSTestSettings = Seq(
  libraryDependencies += "org.scalatest" %%% "scalatest" % Settings.versions.scalaTest % Test,
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
)

lazy val typequuxtests = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .in(file("typequuxtests"))
  .settings(commonSettings("typequuxtests"))
  .jvmSettings(commonJVMSettings)
  .jvmSettings(jvmJSTestSettings)
  .jvmSettings(
    (crossTest in Test) := {
      runCommandAndRemaining("+typequuxtestsJVM/test")(state.value)
    }
  )
  .jsSettings(commonJSSettings)
  .jsSettings(jvmJSTestSettings)
  .jsSettings(
    scalaJSStage in Test := FullOptStage,
    coverageExcludedPackages := ".*",
    (crossTest in Test) := {
      runCommandAndRemaining("+typequuxtestsJS/test")(state.value)
    }
  )
  .nativeSettings(commonNativeSettings)
  .nativeSettings(
    coverageExcludedPackages := ".*"
  )
  .dependsOn(typequux)

lazy val typequuxtestsJVM = typequuxtests.jvm
lazy val typequuxtestsJS = typequuxtests.js
lazy val typequuxtestsNative = typequuxtests.native

lazy val cleanAll = taskKey[Unit]("Cleans everything")
lazy val testJVMJS = taskKey[Unit]("Tests JVM and JS")
lazy val testAll = taskKey[Unit]("Tests everything")
lazy val buildCoverage = taskKey[Unit]("Generate coverage report")
lazy val publishLibLocal = taskKey[Unit]("Publishes the library locally")
lazy val publishLibSigned = taskKey[Unit]("Publishes the library signed")

lazy val Typequux = config("typequuxJVM")

lazy val releaseCommand = Command.command("release") { state =>
  "publishLibSigned" :: "sonatypeRelease" :: "ghpagesPushSite" :: state
}

lazy val root = project
  .in(file("."))
  .settings(commonSettings("root"))
  .settings(
    inThisBuild(
      List(
        cleanAll := {
          clean.in(typequuxJVM).value
          clean.in(typequuxtestsJVM).value

          clean.in(typequuxJS).value
          clean.in(typequuxtestsJS).value

          clean.in(typequuxNative).value
          clean.in(typequuxtestsNative).value
        },
        testJVMJS := {
          crossTest.in(typequuxtestsJVM, Test).value
          crossTest.in(typequuxtestsJS, Test).value
        },
        testAll := {
          testJVMJS.value
          test.in(typequuxtestsNative, Test).value
        },
        buildCoverage := Def
          .sequential(
            clean in typequuxJVM,
            clean in typequuxtestsJVM,
            test in (typequuxtestsJVM, Test),
            coverageReport in typequuxJVM
          )
          .value,
        publishLibLocal := Def
          .sequential(
            cleanAll,
            testAll,
            publishLocalCross in typequuxJVM,
            publishLocalCross in typequuxJS,
            publishLocal in typequuxNative
          )
          .value,
        publishLibSigned := Def
          .sequential(
            cleanAll,
            testAll,
            publishSignedCross in typequuxJVM,
            publishSignedCross in typequuxJS,
            publishSignedCross in typequuxNative // dunno why normal publish signed is not working
          )
          .value,
        commands += releaseCommand
      ))
  )
  .enablePlugins(SiteScaladocPlugin, PamfletPlugin)
  .settings(
    SiteScaladocPlugin.scaladocSettings(Typequux, mappings in (Compile, packageDoc) in typequuxJVM, "api"),
    ghpages.settings,
    git.remoteRepo := "git@github.com:harshad-deo/typequux.git"
  )
