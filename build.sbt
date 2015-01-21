name := "AmazonS3LoadBalancer"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.amazonaws" % "aws-java-sdk" % "1.9.14",
  "com.google.code.gson" % "gson" % "2.2")     

play.Project.playJavaSettings
