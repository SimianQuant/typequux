addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "1.2.1")

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "0.2.2")

addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.5")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.2.0-RC1")

resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.13")

resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("org.scala-native" % "sbt-cross" % "0.1.0-SNAPSHOT")

addSbtPlugin("org.scala-native" % "sbt-scalajs-cross" % "0.1.0-SNAPSHOT")

addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.1.0-SNAPSHOT")
