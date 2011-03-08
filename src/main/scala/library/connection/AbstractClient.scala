package de.tdng2011.game.library.connection

import java.net.Socket
import java.io.DataInputStream
import de.tdng2011.game.library.util._
import de.tdng2011.game.library._

abstract class AbstractClient(hostname : String, relation : RelationTypes.Value) extends Runnable {

  private var world : World = null

  private var scoreBoard : ScoreBoard = null

  private var publicId : Long = -1

  private var connection : Socket = connect()

//  new Thread(this).start

  def run() {
    var iStream = new DataInputStream(connection.getInputStream)

//    try {
      while(true) {
        readEntity(iStream)
      }
//    } catch {
//      case e => {
 //       e.printStackTrace
  //      println("error while getting frame. trying to reconnect!");
  //      connection = connect();
  //        iStream = new DataInputStream(connection.getInputStream);
  //   }
    //}
  }



  def readEntity(iStream : DataInputStream) {
    StreamUtil.read(iStream, 2).getShort match {
      case x if x == EntityTypes.World.id  => {
        world = World(iStream)
        processWorld(world)
      }
      case x if x == EntityTypes.Scoreboard.id  => {
        scoreBoard = ScoreBoard(iStream)
        processScoreBoard(scoreBoard)
      }
      case x => {
        println("barbra streisand! (unknown bytes, wth?!) typeId: " + x)
        val size = StreamUtil.read(iStream, 4).getInt
        StreamUtil.read(iStream, size) //skip
      }
    }
  }

  private def handshake(s : Socket) {
    relation match {
      case x if x == RelationTypes.Player     => handshakePlayer(s)
      case x if x == RelationTypes.Visualizer => handshakeVisualizer(s)
      case x => {
        println("barbra streisand! (unknown relation, wth?!) typeId: " + x)
        System exit -1
      }
    }
  }

  private def handshakePlayer(s : Socket) {
    val iStream = new DataInputStream(s.getInputStream)
    s.getOutputStream.write(ByteUtil.toByteArray(EntityTypes.Handshake, RelationTypes.Player.id.shortValue, name))

    val buf    = StreamUtil.read(iStream, 6)
    val typeId = buf.getShort
    val size   = buf.getInt

    val response = StreamUtil.read(iStream, size)
    if (typeId == EntityTypes.Handshake.id) {
      val responseCode = response.get
      println("response code: " + responseCode)
      if (responseCode == 0)
        publicId = response.getLong
    }
  }

  private def handshakeVisualizer(s : Socket) {
      s.getOutputStream.write(ByteUtil.toByteArray(EntityTypes.Handshake, RelationTypes.Visualizer.id.shortValue))
  }

  private def connect() : Socket = {
    try {
      val s = new Socket(hostname, 1337)
      handshake(s)
      s
    } catch {
      case e => {
        println("connecting failed. retrying in 5 seconds");
        Thread.sleep(5000)
        connect()
      }
    }
  }

  def getConnection = connection

  def getPublicId = publicId

  def name = "Player"

  def getWorld = world

  def getScoreBoard = scoreBoard

  def processWorld(world : World) : Unit

  def processScoreBoard(scoreBoard : ScoreBoard) {}

  def action(turnLeft : Boolean, turnRight : Boolean, thrust : Boolean, fire : Boolean) {
    getConnection.getOutputStream.write(ByteUtil.toByteArray(EntityTypes.Action, turnLeft, turnRight, thrust, fire))
  }
}
