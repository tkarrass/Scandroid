package de.tdng2011.game.library

import java.io.DataInputStream
import util.StreamUtil

class Player(iStream : DataInputStream) extends Entity(iStream) {

  val rotSpeed  : Float   = buf.getFloat
  val turnLeft  : Boolean = buf.get == 1
  val turnRight : Boolean = buf.get == 1
  val thrust    : Boolean = buf.get == 1
  val fire      : Boolean = buf.get == 1
  
  override def toString() = "Player(id: " + publicId + ", pos: " + pos + ", direction: " + direction + ", radius: " + radius + ", speed: " + speed + 
                              ", rotSpeed: " + rotSpeed + ", turnLeft: " + turnLeft + ", turnRight: " + turnRight + ", thrust: " + thrust + ", fire: " + fire + ")"
}
object Player {
  def parsePlayerId(iStream : DataInputStream) = {
    StreamUtil.readBody(iStream).getLong
  }

  def parsePlayerIdAndName(iStream : DataInputStream) = {
    val size = StreamUtil.read(iStream, 4).getInt
    val buf = StreamUtil.read(iStream, 8)

    (buf.getLong, StreamUtil.read(iStream, size-8).asCharBuffer.toString)
  }
}