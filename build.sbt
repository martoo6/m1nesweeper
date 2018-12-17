name := "m1newseeper"

version := "0.1"

scalaVersion := "2.12.8"

val akkaHttpVersion = "10.1.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream"          % "2.5.19",
  "org.mongodb.scala" %% "mongo-scala-driver"   % "2.5.0"
)

enablePlugins(JavaServerAppPackaging)