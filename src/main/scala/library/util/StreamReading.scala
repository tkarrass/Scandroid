package de.tdng2011.game.library.util

import java.io.DataInputStream

abstract class StreamReading(iStream : DataInputStream) {
  protected val buf = StreamUtil.readBody(iStream)
}