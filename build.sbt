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

lazy val typequux = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("typequux"))
  .settings(commonSettings("typequux"))
  .settings(
    previewLaunchBrowser := false,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
      else Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
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
  .jsSettings(commonJSSettings)

lazy val typequuxJVM = typequux.jvm
lazy val typequuxJS = typequux.js

lazy val jvmJSTestSettings = Seq(
  )

lazy val typequuxtests = crossProject(JVMPlatform, JSPlatform)
  .in(file("typequuxtests"))
  .settings(
    commonSettings("typequuxtests"),
    libraryDependencies += "org.scalatest" %%% "scalatest" % Settings.versions.scalaTest % Test,
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
  )
  .jvmSettings(commonJVMSettings)
  .jsSettings(commonJSSettings)
  .jsSettings(
    coverageExcludedPackages := ".*"
  )
  .dependsOn(typequux)

lazy val typequuxtestsJVM = typequuxtests.jvm
lazy val typequuxtestsJS = typequuxtests.js

lazy val cleanAll = taskKey[Unit]("Cleans everything")
lazy val testAll = taskKey[Unit]("Tests everything")
lazy val buildCoverage = taskKey[Unit]("Generate coverage report")

lazy val Typequux = config("typequuxJVM")

lazy val releaseCommand = Command.command("release") { state =>
  "cleanAll" :: "+testAll" :: "+typequuxJVM/publishSigned" :: "+typequuxJS/publishSigned" :: "sonatypeRelease" :: "ghpagesPushSite" :: state
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
        },
        testAll := {
          test.in(typequuxtestsJVM, Test).value
          test.in(typequuxtestsJS, Test).value
        },
        buildCoverage := Def
          .sequential(
            clean.in(typequuxJVM),
            clean.in(typequuxtestsJVM),
            test.in(typequuxtestsJVM, Test),
            coverageReport.in(typequuxJVM)
          )
          .value,
        commands += releaseCommand
      ))
  )
  .enablePlugins(SiteScaladocPlugin, PamfletPlugin, GhpagesPlugin)
  .settings(
    SiteScaladocPlugin.scaladocSettings(Typequux, mappings in (Compile, packageDoc) in typequuxJVM, "api"),
    git.remoteRepo := "git@github.com:harshad-deo/typequux.git"
  )
