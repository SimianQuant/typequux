import sbtcrossproject.{crossProject, CrossType}

def commonSettings(nameStr: String) = Seq(
  name := nameStr,
  organization := "com.simianquant",
  organizationHomepage := Some(url("https://simianquant.com/")),
  version := Settings.versions.project,
  scalaVersion := Settings.versions.scala,
  incOptions := incOptions.value.withLogRecompileOnMacro(false),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  scalacOptions in (Compile) ++= Settings.scalacOptions,
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
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/SimianQuant/typequux"),
        "scm:git@github.com:SimianQuant/typequux.git"
      )
    ),
    developers := List(
      Developer(
        id = "harshad-deo",
        name = "Harshad Deo",
        email = "harshad@simianquant.com",
        url = url("https://github.com/harshad-deo")
      )
    ),
    description := "A hackable library for typelevel programming in Scala",
    licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    homepage := Some(url("https://github.com/SimianQuant/typequux")),
    pomIncludeRepository := { _ => false },
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
      else Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true
  )
  .jvmSettings(commonJVMSettings)
  .jsSettings(commonJSSettings)

lazy val typequuxJVM = typequux.jvm
lazy val typequuxJS = typequux.js

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
      )
    )
  )
  .enablePlugins(SiteScaladocPlugin, PamfletPlugin, GhpagesPlugin)
  .settings(
    SiteScaladocPlugin.scaladocSettings(Typequux, mappings in (Compile, packageDoc) in typequuxJVM, "api"),
    git.remoteRepo := "git@github.com:simianquant/typequux.git"
  )
