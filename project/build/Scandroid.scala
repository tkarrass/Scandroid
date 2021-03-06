import sbt._

trait Defaults {
  def androidPlatformName = "android-7"
}
class Scandroid(info: ProjectInfo) extends ParentProject(info) {
  override def shouldCheckOutputDirectories = false
  override def updateAction = task { None }

  lazy val main  = project(".", "Scandroid", new MainProject(_) )
//  lazy val tests = project("tests",  "tests", new TestProject(_), main)

  class MainProject(info: ProjectInfo) extends AndroidProject(info) with Defaults with MarketPublish {
	override def packageAlignedName = name + "-current" + ".apk"
	override def keystorePath = info.projectPath + "Scandroid.keystore"
    val keyalias  = "scandroid_debug"
    val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"
	override def getPassword = "android"
  }

//  class TestProject(info: ProjectInfo) extends AndroidTestProject(info) with Defaults
}
