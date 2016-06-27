scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.6")

name := "typequux"
organization := "typefoo"
version := "0.1"

fork := true
coverageEnabled := true

lazy val compilecheck = taskKey[Unit]("compile and then scalastyle")

compilecheck in Compile := Def.sequential(compile in Compile, (scalastyle in Compile).toTask("")).value

lazy val scalaReflect = Def.setting { "org.scala-lang" % "scala-reflect" % scalaVersion.value }

import Defaults._

libraryDependencies ++= Seq(
  scalaReflect.value,
  "org.typelevel" %% "macro-compat" % "1.1.1",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
  compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.chuusai" %% "shapeless" % "2.3.1" % "test"
)

wartremoverErrors ++= {
  import Wart._
  Seq(Any2StringAdd, EitherProjectionPartial, Enumeration,  IsInstanceOf, ListOps, Option2Iterable, 
    OptionPartial, Product, Return, Serializable, TryPartial)
}

scalacOptions in (Compile,doc) ++= Seq(
  "-deprecation",
  "-unchecked",
  "-explaintypes",
//  "-Ywarn-unused-import",
  "-encoding", "UTF-8",
  "-feature",
  "-Xlog-reflective-calls",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
//  "-Ywarn-infer-any",
//  "-Ywarn-unused",
  "-Ywarn-value-discard",
  "-Xlint",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Xfuture",
  "-author",
  "-groups", 
  "-implicits",
  "-P:linter:disable:UnusedParameter"
)

scalacOptions in (Compile, doc) <++= baseDirectory.map {
  (bd: File) => Seq[String](
    "-sourcepath", 
    bd.getAbsolutePath, 
    "-doc-source-url", 
    "https://github.com/harshad-deo/typequux/tree/master€{FILE_PATH}.scala"
    )
  }

initialCommands := """
class Witness[T](val x: T)
object Witness{
  def apply[T](x: T): Witness[T] = new Witness(x)
}
import typequux._
"""

addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.14")