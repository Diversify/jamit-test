
scalaVersion := "2.9.2"

organization := "diversify.se"

name := "jamit-test"

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                    "releases"  at "http://oss.sonatype.org/content/repositories/releases",
					"repo.codahale.com" at "http://repo.codahale.com")

externalResolvers += "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.11" % "test",
    "junit" % "junit" % "4.8.2" % "test",
    "org.pegdown" % "pegdown" % "1.1.0",
    "net.databinder" %% "dispatch-http" % "0.8.8",
    "commons-lang" % "commons-lang" % "2.6",
    "com.codahale" % "jerkson_2.9.1" % "0.5.0",
    "org.slf4j" % "slf4j-simple" % "1.6.6"
)

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "html", "console", "junitxml")

