val typequux = crossProject
  .crossType(CrossType.Pure)
  .enablePlugins(SiteScaladocPlugin, PamfletPlugin)
  .settings(
    scalaVersion := "2.11.8",
    name := "typequux",
    organization := "com.simianquant",
    version := "0.1.2",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.scalatest" %%% "scalatest" % "3.0.0" % "test"
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
    scalacOptions in (Compile, doc) ++= Seq(
      "-author",
      "-groups",
      "-implicits"
    ),
    scalacOptions in (Compile, doc) <++= baseDirectory.map { (bd: File) =>
      Seq[String](
        "-sourcepath",
        bd.getAbsolutePath,
        "-doc-source-url",
        "https://github.com/harshad-deo/typequux/tree/master€{FILE_PATH}.scala"
      )
    },
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD"),
    initialCommands := """| class Witness[T](val x: T)
      | object Witness{
      |   def apply[T](x: T): Witness[T] = new Witness(x)
      | }
      | import typequux._""".stripMargin,
    addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.14"),
    siteSubdirName in SiteScaladoc := "api",
    previewLaunchBrowser := false,
    publishMavenStyle := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
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
  .jvmSettings(
    fork := true
  )
  .jsSettings(
    scalaJSUseRhino in Global := false,
    scalaJSStage in Test := FullOptStage
  )

lazy val typequuxJS = typequux.js
lazy val typequuxJVM = typequux.jvm

ghpages.settings

git.remoteRepo := "git@github.com:harshad-deo/typequux.git"
