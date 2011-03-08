package de.tdng2011.game.library.util
//import org.apache.log4j.Logger;

/*
 * Das bring aufm Android nix: Besser wär eine eigene Implementierung gegen die adb
 */


// seeeeeeehr qnd logger dummy um die lib im android zu nutzen
class DummyLogger {
  def warn(vsMessage:String) {}
  def info(vsMessage:String) {}
  def error(vsMessage:String) {}
}

/**
 * simple logging trait using log4j
 */
trait ScubywarsLogger {
  val loggerName = this.getClass.getName
  lazy val logger = new DummyLogger() //Logger.getLogger(loggerName)
}