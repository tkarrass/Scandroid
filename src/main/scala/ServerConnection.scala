package de.bitsetter.scandroid

import _root_.de.tdng2011.game.library._
import _root_.de.tdng2011.game.library.connection._

class ServerConnection(vsHostname: String,
                       vsName: String,
                       procWorld: World => Unit,
                       procScore: ScoreBoard => Unit) extends AbstractClient(vsHostname, RelationTypes.Player) {

  override def name = vsName;
  def processWorld(vcWorld: World) : Unit = procWorld(vcWorld)
  override def processScoreBoard(vcScoreBoard: ScoreBoard) : Unit = procScore(vcScoreBoard)

}