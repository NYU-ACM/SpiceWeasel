scalaVersion := "2.12.1"

assemblyJarName in assembly := "cleaner.jar"

mainClass in assembly := Some("edu.nyu.dlts.bdtools.Cleaner")

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.2.1",
  "joda-time" % "joda-time" % "2.3",
  "org.json4s" % "json4s-jackson_2.11" % "3.2.10",
  "org.apache.httpcomponents" % "httpclient" % "4.3.6",
  "commons-io" % "commons-io" % "2.4"
)
