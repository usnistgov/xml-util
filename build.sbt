import sbt._
import Keys._

ThisBuild / organization   := "gov.nist"
ThisBuild / version        := "2.0.0"
ThisBuild / scalaVersion  := "2.12.10"
ThisBuild / scalacOptions := Seq(
  "-encoding", "utf8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-target:jvm-1.8",
  "-language:_",
  "-Dconfig.trace=loads",
  "-J-Xmx2g"
)

lazy val moduleSettings =
  Seq(
    crossPaths := false,
    publishMavenStyle := true,
    credentials += Credentials(Path.userHome / ".nexusCredentials"),
    publishTo := {
      val nexus = "http://hit-dev-admin.nist.gov:9001/"
      if (version.value.trim.endsWith("SNAPSHOT"))
        Some(("snapshots" at nexus + "repository/snapshots").withAllowInsecureProtocol(true))
      else
        Some(("releases" at nexus + "repository/releases").withAllowInsecureProtocol(true))
    }
  )

lazy val xmlUtil = (project in file("."))
  .settings(moduleSettings: _*)
  .settings(
    name := "xml-util",
    libraryDependencies ++= Seq(
      "xom" % "xom" % "1.3.7"
    )
  )


