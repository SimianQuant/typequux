object Settings {
  val version = "0.7.1"

  val scalaVersion = "2.12.2"

  val crossScalaVersions = Seq("2.11.11", "2.12.2")

  def commonScalacOptions(version: String): Seq[String] = {
    val common = List(
      "-Ywarn-unused-import",
      "-deprecation",
      "-unchecked",
      "-explaintypes",
      "-encoding",
      "UTF-8",
      "-feature",
      "-Xlog-reflective-calls",
      "-Ywarn-dead-code",
      "-Ywarn-inaccessible",
      "-Ywarn-infer-any",
      "-Ywarn-nullary-override",
      "-Ywarn-nullary-unit",
      "-Xfuture"
    )
    if (version.startsWith("2.11")) {
      "-Xlint:_,-missing-interpolator" :: "-Ywarn-unused" :: common
    } else {
      "-Xlint:adapted-args,nullary-unit,inaccessible,nullary-override,infer-any,doc-detached,private-shadow," +
        "type-parameter-shadow,poly-implicit-overload,option-implicit,delayedinit-select,by-name-right-associative," +
        "package-object-classes,unsound-match,stars-align,constant" ::
        "-Ywarn-unused:imports,patvars,privates,locals" ::
        "-opt:l:method" ::
        common
    }
  }

  val scalacDocOptions = Seq(
    "-author",
    "-groups",
    "-implicits"
  )

  object Version {
    val scalaTest = "3.0.1"
    val sntb = "0.3.3"
  }
}
