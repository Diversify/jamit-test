

organization := "diversify.se"

name := "jamit-test"

externalResolvers += "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "1.7.1" % "test",
    "junit" % "junit" % "4.8.2" % "test",
    "se.diversify" % "jamit-logic" % "1.0" % "test",
    "org.pegdown" % "pegdown" % "1.1.0",
    "net.databinder" %% "dispatch-http" % "0.8.8"
)

(testOptions in Test) += Tests.Argument(TestFrameworks.Specs2, "html", "console", "junitxml")

