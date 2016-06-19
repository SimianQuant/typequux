scalaVersion := "2.11.8"

name := "typequux"
organization := "typefoo"
version := "0.1"

fork := true
coverageEnabled := true

lazy val compilecheck = taskKey[Unit]("compile and then scalastyle")

compilecheck in Compile := Def.sequential(compile in Compile, (scalastyle in Compile).toTask(""), 
  (scapegoat in Compile)).value

scapegoatVersion := "1.1.0"

scapegoatDisabledInspections := Seq("UnusedMethodParameter", "LooksLikeInterpolatedString", "NullParameter", 
  "TraversableHead", "AvoidOperatorOverload", "MaxParameters")

wartremoverErrors ++= {
  import Wart._
  Seq(Any2StringAdd, AsInstanceOf, EitherProjectionPartial, Enumeration,  IsInstanceOf, ListOps, Option2Iterable, 
    OptionPartial, Product, Return, Serializable, TryPartial)
}

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.chuusai" %% "shapeless" % "2.3.1" % "test"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-explaintypes",
  "-Ywarn-unused-import",
  "-encoding", "UTF-8",
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
)

initialCommands := """
class Witness[T](val x: T)
object Witness{
  def apply[T](x: T): Witness[T] = new Witness(x)
}
import typequux._
"""

addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.14")

import TodoListPlugin._
compileWithTodolistSettings
testWithTodolistSettings
