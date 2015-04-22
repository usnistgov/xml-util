organization   := "ssmm"

name           := "xml-util"

version        := "1.0.0-SNAPSHOT"

crossPaths := false

scalacOptions += "-target:jvm-1.7"

libraryDependencies ++= Seq(
  "junit"        %     "junit"        %    "4.12"   %   "test",
  "com.novocode" % "junit-interface"  %    "0.11"   %   "test"
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
