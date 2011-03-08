package de.tdng2011.game.library.util

import java.nio.ByteBuffer
import java.io.DataInputStream

object StreamUtil {

  def read(iStream : DataInputStream, count : Int) : ByteBuffer = {
    val byteArray = new Array[Byte](count)
    iStream.readFully(byteArray)
    ByteBuffer.wrap(byteArray)
  }

  def readBody(iStream : DataInputStream) : ByteBuffer = {
    val size = StreamUtil.read(iStream, 4).getInt
    StreamUtil.read(iStream, size)
  }
}