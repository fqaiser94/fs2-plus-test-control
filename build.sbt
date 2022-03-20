ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file(".")).settings(
  name := "fs2_time_testing",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.3.5",
    "co.fs2" %% "fs2-core" % "3.2.5",
    "org.typelevel" %% "cats-effect-testkit" % "3.3.5" % Test,
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test
  )
)
