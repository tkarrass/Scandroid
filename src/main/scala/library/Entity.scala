package de.tdng2011.game.library

import java.io.DataInputStream
import util.{StreamReading, Vec2}

abstract class Entity(iStream : DataInputStream) extends StreamReading(iStream) {
  val publicId  : Long    = buf.getLong
  val pos       : Vec2    = Vec2(buf.getFloat, buf.getFloat)
  val direction : Float   = buf.getFloat
  val radius    : Short   = buf.getShort
  val speed     : Short   = buf.getShort
}