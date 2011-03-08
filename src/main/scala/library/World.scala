package de.tdng2011.game.library

import java.nio.ByteBuffer
import java.io.DataInputStream
import util.{ScubywarsLogger, StreamReading, StreamUtil}

/**
 * Created by IntelliJ IDEA.
 * Author: SuperTux88
 * Date: 06.03.11
 * Time: 05:12
 */

case class World(iStream : DataInputStream) extends StreamReading(iStream) with ScubywarsLogger {

  private val count = buf.getInt

  private var tmpPlayers : IndexedSeq[Player] = IndexedSeq()
  private var tmpShots : IndexedSeq[Shot] = IndexedSeq()
  for (i <- 0 until count) {
    StreamUtil.read(iStream, 2).getShort match {
      case x if x == EntityTypes.Player.id => tmpPlayers = tmpPlayers :+ new Player(iStream)
      case x if x == EntityTypes.Shot.id   => tmpShots   = tmpShots   :+ new Shot(iStream)
      case x => logger.warn("unknown entity in world: " + x)
    }
  }

  val players = tmpPlayers
  val shots = tmpShots
}
