package de.tdng2011.game.library

import util.StreamUtil
import java.nio.ByteBuffer
import java.io.DataInputStream

/**
 * Created by IntelliJ IDEA.
 * Author: SuperTux88
 * Date: 06.03.11
 * Time: 05:12
 */

case class World(iStream : DataInputStream){

  private val size = StreamUtil.read(iStream, 4).getInt
  private val worldData = StreamUtil.read(iStream, size)
  private val count = worldData.getInt

  private var tmpPlayers : IndexedSeq[Player] = IndexedSeq()
  private var tmpShots : IndexedSeq[Shot] = IndexedSeq()
  for (i <- 0 until count) {
    StreamUtil.read(iStream, 2).getShort match {
      case x if x == EntityTypes.Player.id => tmpPlayers = tmpPlayers :+ new Player(iStream)
      case x if x == EntityTypes.Shot.id   => tmpShots   = tmpShots   :+ new Shot(iStream)
      case x => println("barbra streisand! !player and !shot")
    }
  }

  val players = tmpPlayers
  val shots = tmpShots
}