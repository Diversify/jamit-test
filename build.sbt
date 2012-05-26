
scalaVersion := "2.9.2"

organization := "diversify.se"

name := "jamit-test"

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                    "releases"  at "http://oss.sonatype.org/content/repositories/releases",
					"repo.codahale.com" at "http://repo.codahale.com")

externalResolvers += "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.11-SNAPSHOT" % "test",
    "junit" % "junit" % "4.8.2" % "test",
    "se.diversify" % "jamit-logic" % "1.0" % "test",
    "org.pegdown" % "pegdown" % "1.1.0",
    "net.databinder" %% "dispatch-http" % "0.8.8",
    "org.apache.commons" % "commons-lang3" % "3.1",
    "com.codahale" % "jerkson_2.9.1" % "0.5.0"
)

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "html", "console", "junitxml")

