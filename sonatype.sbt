publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ =>
  false
}

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
