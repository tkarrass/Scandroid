package de.tdng2011.game.library

import actors.Actor
import de.tdng2011.game.library.util.StreamUtil

import java.nio.ByteBuffer
import java.io.DataInputStream

/**
 * Created by IntelliJ IDEA.
 * User: benjamin
 * Date: 23.01.11
 * Time: 16:59
 */

case class ScoreBoard (stream : DataInputStream) {
  private val size = StreamUtil.read(stream, 4).getInt
  private val buf = StreamUtil.read(stream, size)

  private var tmpScores = Map[Long, Int]()

  while(buf.hasRemaining) {
    tmpScores = tmpScores + (buf.getLong -> buf.getInt)
  }

  val scores = tmpScores
}