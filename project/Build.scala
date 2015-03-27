import sbt._
import Keys._

object AktorBackendBuild extends Build with ConfigureScalaBuild {


  lazy val root = scalaMiniProject("com.aktorbackend","aktorbackend","1.0")
    .settings(
      organization := "totenarsch",
      scalaVersion := "2.10.0",
      scalacOptions ++= Seq("-unchecked", "-deprecation"),

      resolvers += ("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"),
      parallelExecution in Test := false
    )
    .settings(
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor" % "2.1.0" exclude("org.scala-stm", "scala-stm_2.10.0"),
        "com.typesafe.akka" %% "akka-remote" % "2.1.0" exclude("org.scala-stm", "scala-stm_2.10.0"),
        "com.typesafe.akka" %% "akka-agent" % "2.1.0" exclude("org.scala-stm", "scala-stm_2.10.0"),
        //"com.h2database" % "h2" % "1.2.127",
        //"org.squeryl" % "squeryl_2.10.0-RC5" % "0.9.5-5",
        "org.specs2" %% "specs2" % "1.13" % "test" exclude("org.scala-stm", "scala-stm_2.10.0"),
        "org.eclipse.jetty" % "jetty-distribution" % "8.0.0.M2" % "test" exclude("org.scala-stm", "scala-stm_2.10.0")
      ))
}

trait ConfigureScalaBuild {


  lazy val typesafe = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

  lazy val typesafeSnapshot = "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"

  val netty = Some("play.core.server.NettyServer")

  def scalaMiniProject(org: String, name: String, buildVersion: String, baseFile: java.io.File = file(".")) = Project(id = name, base = baseFile, settings = Project.defaultSettings).settings(
    version := buildVersion,
    organization := org,
    resolvers += typesafe,
    resolvers += typesafeSnapshot,
    libraryDependencies += "com.typesafe" %% "play-mini" % "2.1-RC2" exclude("org.scala-stm", "scala-stm_2.10.0"),
    mainClass in (Compile, run) := netty,
    ivyXML := <dependencies> <exclude org="org.springframework"/> </dependencies>
  )
}