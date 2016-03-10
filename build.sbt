organization   := "gov.nist"

name           := "xml-util"

version        := "1.0.0"

crossPaths := false

libraryDependencies ++= Seq(
  "xom"          %     "xom"          %    "1.2.5"
)

libraryDependencies ++= Seq("junit" % "junit" % "4.12")

libraryDependencies ++= Seq("commons-io" % "commons-io" % "2.4")

//Remove scala version
crossPaths := false

publishMavenStyle := true

credentials       += Credentials(Path.userHome / ".nexusCredentials")

publishTo         := {
  val nexus = "http://vm-070.nist.gov:8081/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "nexus/content/repositories/snapshots")
  else
    Some("releases"  at nexus + "nexus/content/repositories/releases")
}

lazy val noPublishing = Seq(
    publish      := (),
    publishLocal := (),
    publishTo    := None
  )

lazy val m2Repo = 
   Resolver.file("publish-custom-m2-local", 
      file("/vagrant/.m2") )

publishM2Configuration <<= (packagedArtifacts, checksums in publish, ivyLoggingLevel) map { (arts, cs, level) => 
      Classpaths.publishConfig(arts, None, resolverName = m2Repo.name, checksums = cs, logging = level) 
   }

publishM2 <<= Classpaths.publishTask(publishM2Configuration, deliverLocal)

otherResolvers += m2Repo
