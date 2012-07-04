name := "SvnHist"

version := "1"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.7.1" % "test",
  "junit" % "junit" % "4.10" % "test",
  "org.tmatesoft.svnkit" % "svnkit" % "1.7.5" 
)