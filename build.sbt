import sbtcrossproject.{crossProject, CrossType}

lazy val commonShared = Seq(
  organization := "com.simianquant",
  version := "0.6.0",
  scalaVersion := "2.11.8",
  incOptions := incOptions.value.withLogRecompileOnMacro(false),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  wartremoverErrors ++= {
    import Wart._
    Seq(Any2StringAdd,
        EitherProjectionPartial,
        Enumeration,
        IsInstanceOf,
        ListOps,
        Option2Iterable,
        OptionPartial,
        Product,
        Return,
        Serializable,
        TryPartial)
  },
  scalacOptions in (Compile) ++= Seq(
    "-deprecation",
    "-unchecked",
    "-explaintypes",
    "-Ywarn-unused-import",
    "-encoding",
    "UTF-8",
    "-feature",
    "-Xlog-reflective-calls",
    "-Ywarn-dead-code",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-unused",
    "-Ywarn-value-discard",
    "-Xlint",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Xfuture",
    "-P:linter:disable:UnusedParameter"
  ),
  scalacOptions in (Compile) ++= Seq(scalaVersion.value match {
    case x if x.startsWith("2.12.") => "-target:jvm-1.8"
    case x => "-target:jvm-1.6"
  }),
  scalacOptions in (Compile, doc) ++= Seq(
    "-author",
    "-groups",
    "-implicits"
  ),
  addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.16")
)

lazy val crossVersions = Seq("2.11.8", "2.12.1")

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
    crossScalaVersions := crossVersions,
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
    crossScalaVersions := crossVersions,
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
    crossScalaVersions := crossVersions,
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % "3.0.1" % "test"
    ),
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
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
    libraryDependencies += "com.simianquant" %% "sntb" % "0.1" % "test",
    coverageExcludedPackages := ".*"
  )

lazy val typequuxtestsJVM = typequuxtests.jvm

lazy val typequuxtestsJS = typequuxtests.js

lazy val typequuxtestsNative = typequuxtests.native

//onLoad in Global := (Command.process("project typequuxJVM", _: State)) compose (onLoad in Global).value
