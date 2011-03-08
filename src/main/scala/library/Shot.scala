package de.tdng2011.game.library

import java.io.DataInputStream

class Shot(iStream : DataInputStream) extends Entity(iStream) {

  val parentId  : Long    = buf.getLong
  val lifeTime  : Float   = buf.getFloat
  
  override def toString() = "Shot(id: " + publicId + ", pos: " + pos + ", direction: " + direction + ", radius: " + radius + ", speed: " + speed + 
                              ", parentId: " + parentId + ", lifeTime: " + lifeTime + ")"
}