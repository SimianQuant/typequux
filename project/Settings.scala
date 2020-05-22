object Settings {
  object versions {
    val project: String = "0.9.0"
    val scala: String = "2.13.1"
    val scalaTest: String = "3.1.1"
  }

  val scalacOptions: Seq[String] = List(
    "-Xlint:_",
    "-Wdead-code",
    "-Wextra-implicit",
    "-Wnumeric-widen",
    "-Woctal-literal",
    "-Wunused:imports",
    "-Wunused:patvars",
    "-Wunused:privates",
    "-Wunused:locals",
    "-Wunused:explicits",
    "-Wunused:implicits",
    "-Ywarn-unused:imports,patvars,privates,locals",
    "-opt:l:method",
    "-deprecation",
    "-unchecked",
    "-explaintypes",
    "-encoding",
    "UTF-8",
    "-feature",
    "-Xlog-reflective-calls",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard"
  )

  val scalacDocOptions = Seq(
    "-author",
    "-groups",
    "-implicits"
  )
}
