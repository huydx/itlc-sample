import scalariform.formatter.preferences._
import sbtassembly.PathList

resolvers += "Spy" at "http://files.couchbase.com/maven2/"

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(RewriteArrowSymbols, true)

lazy val common = Seq(
  organization := "adtech",
  version := "0.0.1",
  scalaVersion := "2.11.7",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("reference.conf") => MergeStrategy.concat
    case _ => MergeStrategy.first
  }
)

lazy val Deps = {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  val sprayJsonV = "1.3.2"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-json"    % sprayJsonV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "org.scalaz"          %%  "scalaz-core"   % "7.1.3",
    "com.wix"             %%  "accord-core"   % "0.4.1",
    "net.debasishg"       %% "redisclient"    % "3.0",
    "org.scalikejdbc"     %% "scalikejdbc"    % "2.2.8",
    "com.h2database"      %  "h2"             % "1.4.189",
    "ch.qos.logback"      %  "logback-classic"% "1.1.3",
    "mysql"               % "mysql-connector-java" % "5.1.29",
    "com.bionicspirit"    %% "shade"          % "1.6.0"
  )
}

lazy val hdsp: Project = (project in file("."))
  .settings(common: _*)
  .settings(
    name := "hdsp",
    description := "hitori dsp",
    libraryDependencies ++= Deps,
    fork in run := true
  )
