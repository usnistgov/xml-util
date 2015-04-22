organization   := "gov.nist"

name           := "xml-util"

version        := "2.0.0-SNAPSHOT"

crossPaths := false

scalacOptions += "-target:jvm-1.7"

libraryDependencies ++= Seq(
  "xom"          %     "xom"          %    "1.2.5"
)

//Remove scala version
crossPaths        := false

publishMavenStyle := true

credentials       += Credentials(Path.userHome / ".nexusCredentials")

publishTo         := {
  val nexus = "http://vm-070.nist.gov:8081/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "nexus/content/repositories/snapshots")
  else
    Some("releases"  at nexus + "nexus/content/repositories/releases")
}
