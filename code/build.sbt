name := "yaro"

version       := "0.1"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-optimise", "-Xlint", "-unchecked", "-deprecation", "-encoding", "utf8")

enablePlugins(ScalaJSPlugin)

//scalaJSSemantics ~= { _.withStrictFloats(false) }

//unmanagedSourceDirectories in Compile += baseDirectory.value / "../shared"

//libraryDependencies += "biz.enef" %%% "scalajs-angulate" % "0.2.1"


libraryDependencies += "biz.enef" %%% "slogging" % "0.3"

libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.3.4"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0"

libraryDependencies += "org.scodec" %%% "scodec-bits" % "1.0.9"

libraryDependencies += "org.scodec" %%% "scodec-core" % "1.8.1"

libraryDependencies += "org.scala-js" %%% "scala-parser-combinators" % "1.0.2"

libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.5"

jsDependencies += ProvidedJS / "rAF.js"

jsDependencies += RuntimeDOM

//jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular.min.js"

//jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular-cookies.min.js" dependsOn "angular.min.js"

//jsDependencies += "org.webjars" % "angularjs" % "1.4.3" / "angular-route.min.js" dependsOn "angular.min.js"

//jsDependencies += "org.webjars" % "jquery" % "1.9.1" / "jquery.min.js"

//jsDependencies += ProvidedJS / "validate.min.js" dependsOn "angular.min.js"

//jsDependencies += ProvidedJS / "bootstrap.min.js" dependsOn "jquery.min.js"

//jsDependencies += ProvidedJS / "ui-bootstrap.min.js" dependsOn ("angular.min.js","bootstrap.min.js")

//jsDependencies += ProvidedJS / "angular-tree-control.js" dependsOn ("angular.min.js","bootstrap.min.js")


libraryDependencies += "com.lihaoyi" %%% "utest" % "0.3.1" % "test"

testFrameworks += new TestFramework("utest.runner.Framework")

persistLauncher in Compile := true
skip in packageJSDependencies := false

mainClass := Some("gie.yaro.app")


