object Settings {
  val version = "0.6.7-SNAPSHOT"

  val scalaVersion = "2.11.11"

  val crossScalaVersions = Seq("2.11.11", "2.12.2")

  val commonScalacOptions = Seq(
    "-deprecation",
    "-unchecked",
    "-explaintypes",
    //"-Ywarn-unused-import",
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
    "-Xfuture"
  )

  val scalacDocOptions = Seq(
    "-author",
    "-groups",
    "-implicits"
  )

  val scalaTestOptions = "-oD"

  object Version {
    val scalaTest = "3.0.1"
    val sntb = "0.2.2"
  }
}
