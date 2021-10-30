
name := "wordcount"

version := "0.1"

scalaVersion := "2.12.7"

assemblyJarName in assembly := "wordcount.jar"

mainClass in assembly := Some("counting.Main")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.apache.httpcomponents.client5" % "httpclient5-fluent" % "5.1.1"
)
