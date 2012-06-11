import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "microlist-play"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "mysql" % "mysql-connector-java" % "5.1.20",
      "com.github.twitter" % "bootstrap" % "2.0.3",
      "com.jquery" % "jquery" % "1.7.1",
      "com.github.play2war" %% "play2-war-core" % "0.4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      resolvers += ("Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"),
      resolvers += ("webjars" at "http://webjars.github.com/m2"),
      resolvers += ("Play2war plugins release" at "http://repository-play-war.forge.cloudbees.com/release/")
    )

}
