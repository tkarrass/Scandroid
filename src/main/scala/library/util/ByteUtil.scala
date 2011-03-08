package de.tdng2011.game.library.util

import java.nio.ByteBuffer
import java.nio.ByteBuffer._
import de.tdng2011.game.library.EntityTypes

object ByteUtil extends ScubywarsLogger {

  def toByteArray(typeId : EntityTypes.Value, a : Any*) : Array[Byte] = {
    var arraySize = 6 // id + size
    for(x <- a){
      x match {
        case x : String => arraySize += x.length*2
        case x : Map[Long, Int] => arraySize += 12 * x.size // Scoreboard
        case x => arraySize += 8  // pessimistic size, works if all elements are 8 bytes
      }
    }
    val byteBuffer : ByteBuffer = allocate(arraySize)
    byteBuffer.putShort(typeId.id.shortValue)
    byteBuffer.position(6)
    for(x <- a){
      x match {
        case x : Float => byteBuffer.putFloat(x)
        case x : Double => byteBuffer.putDouble(x)
        case x : Long => byteBuffer.putLong(x)
        case x : Int => byteBuffer.putInt(x)
        case x : Short => byteBuffer.putShort(x)
        case x : Char => byteBuffer.putChar(x)
        case x : Byte => byteBuffer.put(x)
        case x : Boolean => byteBuffer.put(if(x) 1.byteValue else 0.byteValue)
        case x : String => x.toArray.map(byteBuffer.putChar(_))
        case x : Map[Long, Int] => x.foreach{case (id, score) => {byteBuffer.putLong(id).putInt(score)}}
        case x => logger.error("error! unknown value, your byte array will not contain " + x)
      }
    }
    arraySize = byteBuffer.position
    byteBuffer.position(2)
    byteBuffer.putInt(arraySize - 6)

    val byteArray = new Array[Byte](arraySize)
    byteBuffer.position(0)
    byteBuffer.get(byteArray)
    byteArray
  }
}