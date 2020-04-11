object Settings {
  object versions {
    val project: String = "0.9.0"
    val scala: String = "2.13.1"
    val scalaTest: String = "3.1.1"
  }

  val scalacOptions: Seq[String] = List(
    "-Xlint:adapted-args,nullary-unit,inaccessible,nullary-override,infer-any,doc-detached,private-shadow," +
      "type-parameter-shadow,poly-implicit-overload,option-implicit,delayedinit-select,package-object-classes,stars-align,constant",
    "-Ywarn-unused:imports,patvars,privates,locals",
    "-opt:l:method",
    "-deprecation",
    "-unchecked",
    "-explaintypes",
    "-encoding",
    "UTF-8",
    "-feature",
    "-Xlog-reflective-calls",
    "-Ywarn-dead-code"
  )

  val scalacDocOptions = Seq(
    "-author",
    "-groups",
    "-implicits"
  )
}
