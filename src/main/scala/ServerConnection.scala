package de.bitsetter.scandroid

import _root_.de.tdng2011.game.library._
import _root_.de.tdng2011.game.library.connection._

class ServerConnection(vsHostname: String,
                       vsName: String,
                       procWorld: World => Unit,
                       procScore: Map[Long, Int] => Unit) extends AbstractClient(vsHostname, RelationTypes.Player) {

  override def name = vsName;
  def processWorld(vcWorld: World) : Unit = procWorld(vcWorld)
  override def processScoreBoard(vcScoreBoard: Map[Long, Int]) : Unit = procScore(vcScoreBoard)

}