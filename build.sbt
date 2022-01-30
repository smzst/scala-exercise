name := "scala-exercise"
version := "0.1.0-SNAPSHOT"

lazy val root = project in file(".")

val AkkaVersion = "2.6.18"
val AkkaHttpVersion = "10.2.7"
lazy val sangria = (project in file("sangria"))
  .settings(
    scalaVersion := "2.13.6",
    scalacOptions ++= Seq("-deprecation", "-feature", "-Xsource:3"),
    libraryDependencies ++= Seq(
      "org.sangria-graphql" %% "sangria" % "2.1.6", // マクロが Scala3 に対応してないっぽい
      "org.sangria-graphql" %% "sangria-circe" % "1.3.2",
      "org.sangria-graphql" %% "sangria-spray-json" % "1.0.2",
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "org.scalatest" %% "scalatest" % "3.2.9" % Test
    )
  )
