name := "scala-exercise"
version := "0.1.0-SNAPSHOT"

lazy val root = project in file(".")

lazy val sangria = (project in file("sangria"))
  .settings(
    scalaVersion := "2.13.6",
    scalacOptions ++= Seq("-deprecation", "-feature", "-Xsource:3"),
    libraryDependencies ++= Seq(
      "org.sangria-graphql" %% "sangria" % "2.1.6",
      "org.sangria-graphql" %% "sangria-circe" % "1.3.2",
      "org.scalatest" %% "scalatest" % "3.2.9" % Test
    )
  )
