import sbtcrossproject.{crossProject, CrossType}

lazy val testAllCommand = Command.command("testall") { state =>
  "project typequuxtestsNative" :: "clean" :: "test:run" ::
    "project typequuxtestsJVM" :: "clean" :: "+test" ::
      "project typequuxtestsJS" :: "clean" :: "+test" ::
        state
}

lazy val runcoverageCommand = Command.command("runcoverage") { state =>
  "project typequuxtestsJVM" :: "clean" :: "coverage" :: "test" ::
    "project typequuxJVM" :: "coverageReport" ::
      state
}

lazy val releaseLocalCommand = Command.command("releaselocal") { state =>
  "testall" ::
    "project typequuxNative" :: "publishLocal" ::
      "project typequuxJVM" :: "+publishLocal" ::
        "project typequuxJS" :: "+publishLocal" ::
          state
}

lazy val releaseCommand = Command.command("release") { state =>
  "testall" ::
    "project typequuxNative" :: "publishSigned" ::
      "project typequuxJS" :: "+publishSigned" ::
        "project typequuxJVM" :: "+publishSigned" ::
          "sonatypeRelease" ::
            "ghpagesPushSite" ::
              state
}

lazy val additionalCommands = Seq(testAllCommand, runcoverageCommand, releaseLocalCommand, releaseCommand)

lazy val commonShared = Seq(
  organization := "com.simianquant",
  version := Settings.version,
  scalaVersion := Settings.scalaVersion,
  incOptions := incOptions.value.withLogRecompileOnMacro(false),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  commands ++= additionalCommands,
  wartremoverErrors ++= {
    import Wart._
    Seq(Any2StringAdd,
        EitherProjectionPartial,
        Enumeration,
        ListOps,
        Option2Iterable,
        OptionPartial,
        Product,
        Return,
        Serializable,
        TryPartial)
  },
  scalacOptions in (Compile) ++= Settings.scalacCompileOptions,
  scalacOptions in (Compile) ++= Seq(scalaVersion.value match {
    case x if x.startsWith("2.12.") => "-target:jvm-1.8"
    case x => "-target:jvm-1.6"
  }),
  scalacOptions in (Compile, doc) ++= Settings.scalacDocOptions,
  addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % Settings.Version.linter)
)

lazy val sharedSettings = commonShared ++ Seq(
    name := "typequux",
    previewLaunchBrowser := false,
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo := Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"),
    pomIncludeRepository := { _ =>
    false
  },
    pomExtra := (<url>https://harshad-deo.github.io/typequux/TypeQuux.html</url>
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
      </developers>)
  )

val typequux = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("."))
  .settings(sharedSettings)
  .jvmSettings(
    crossScalaVersions := Settings.crossScalaVersions,
    scalaVersion := Settings.crossScalaVersions.last,
    initialCommands := """| class Witness1[T](val x: T)
      | object Witness1{
      |   def apply[T](x: T): Witness1[T] = new Witness1(x)
      | }
      | class Witness2[T]
      | import typequux._
      | import Typequux._
      | """.stripMargin,
    fork := true
  )
  .jsSettings(
    crossScalaVersions := Settings.crossScalaVersions,
    scalaJSStage in Test := FullOptStage,
    coverageExcludedPackages := ".*"
  )
  .nativeSettings(
    nativeMode := "release",
    coverageExcludedPackages := ".*"
  )

lazy val typequuxJVM =
  typequux.jvm
    .enablePlugins(SiteScaladocPlugin, PamfletPlugin)
    .settings(
      siteSubdirName in SiteScaladoc := "api",
      ghpages.settings,
      git.remoteRepo := "git@github.com:harshad-deo/typequux.git"
    )

lazy val typequuxNative = typequux.native

lazy val typequuxJS = typequux.js.settings(coverageExcludedPackages := ".*")

lazy val testSettings = commonShared ++ Seq(
    name := "typequuxtests",
    crossScalaVersions := Settings.crossScalaVersions,
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % Settings.Version.scalaTest % "test"
    ),
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, Settings.scalaTestOptions)
  )

lazy val typequuxtests = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .in(file("typequuxtests"))
  .settings(commonShared)
  .dependsOn(typequux)
  .aggregate(typequux)
  .jvmSettings(testSettings)
  .jvmSettings(
    fork := true
  )
  .jsSettings(testSettings)
  .jsSettings(
    scalaJSStage in Test := FullOptStage,
    coverageExcludedPackages := ".*"
  )
  .nativeSettings(
    libraryDependencies += "com.simianquant" %% "sntb" % Settings.Version.sntb % "test",
    coverageExcludedPackages := ".*"
  )

lazy val typequuxtestsJVM = typequuxtests.jvm

lazy val typequuxtestsJS = typequuxtests.js

lazy val typequuxtestsNative = typequuxtests.native

commands ++= additionalCommands
