package de.bitsetter.scandroid

import _root_.android.app.Activity
import _root_.android.os._
import _root_.android.os.PowerManager._
import _root_.android.widget.TextView
import _root_.android.hardware._
import _root_.android.content._
import _root_.android.content.pm._
import _root_.android.content.res._
import _root_.android.view._

import java.io._
import de.tdng2011.game.library._
import de.tdng2011.game.library.util._

import scala.math._

// Unsere kleine Android-Scala-Welt
class MainActivity extends Activity with SensorEventListener {
  // Muss auch vom Scuby-Thread adressierbar sein, also global merken:
  private var tv: TextView = null

  // Unser Sensor-Bla:
  private var mcSensorManager: SensorManager = _
  private var mcSensor: Sensor = _
  private var mcVibrator: Vibrator = _

  // Und die Scuby-Connection
  private var mcServer: ServerConnection = _
  private var mcServerThread: Thread = _

  // Status nachhalten – So können Änderungen erkannt werden und
  // ein spammen des Servers kann vermieden werden
  // (wobei ich finde, das sollte in die lib gehören)
  private var mbFire: Boolean = false;
  private var mfDirection: Float = 0.0f
  private var mfX: Float = 0.0f
  private var mfY: Float = 0.0f
  private var mbLeft: Boolean = false;
  private var mbRight: Boolean = false;
  private var mbThrust: Boolean = false;
  private var mbIsFire: Boolean = false;


  // Erstellen wir unsere app:
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    var tv = new TextView(this)
    setContentView(tv)
    mcSensorManager = getSystemService(Context.SENSOR_SERVICE).asInstanceOf[SensorManager]
    mcSensor = mcSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
    mcVibrator = getSystemService(Context.VIBRATOR_SERVICE).asInstanceOf[Vibrator]
  }

  // App existiert im Background: holen wir sie!¡
  override def onResume() {
    super.onResume
    if (tv == null)
      tv = new TextView(this)
    setContentView(tv)
    tv.setText("Opening connection")
    try {
      mcServer = new ServerConnection("test.scubywars.de", "Android", procPack, procScore) // WTF!¡!¡ <<== Das *M U S S* konfigurierbar werden
                                                                           // (was im Übrigen auch für den Client-Namen gilt => ServerConnection.scala)
    } catch {
      case ex =>
        tv.setText(ex.toString() + ": "+ ex.getMessage())
        return
    }
    tv.setText("Registering Listener")
    mcSensorManager.registerListener(this, mcSensor, SensorManager.SENSOR_DELAY_FASTEST) // Alle Sensoren an die Arbeit!¡
    try {
      mcServerThread = new Thread(mcServer)
      mcServerThread.start
    } catch {
      case ex =>
        tv.setText(ex.getMessage())
        return
    }
    tv.setText("done initializing")
  }


  // Callback: Sobald vom Server eine Welt empfangen wurde, wird diese hier verarbeitet
  // Für den Moment reicht es aus, nur die eigenen Daten zu beachten.
  // Sollte eine KI implementiert werden lassen sich hier aber auch alle anderen Daten ermitteln
  def procPack(vcWorld: World) = synchronized {
    var id: Long = mcServer.getPublicId

    for (p:Player <- vcWorld.players) {
      if (p.publicId == id) {
        mfDirection = p.direction / (2 * Pi.floatValue) * 360
        var dx = (mfX - p.pos.x)
        var dy = (mfY - p.pos.y)
        if ( -990 < dx && dx < 990 && -990 < dy && dy < 990 && (dx <= -5 || 5 <= dx
         || dy <= -5 || 5 <= dy)) {
          try {mcVibrator.vibrate(500); } // Brummm brumm brumm – Force feedback beim gestorben werden ;) …
        }
        mfX = p.pos.x
        mfY = p.pos.y
        mbLeft = p.turnLeft
        mbRight = p.turnRight
        mbThrust = p.thrust
        mbIsFire = p.fire
      }
    }
    Nil
  }

  def procScore(vcScore: Map[Long, Int]) = synchronized {
    // TODO
    // zudem müssen noch clientevents behandelt werden
  }

  // Auch wenn es unvorstellbar scheint: manch einer will auch mal beenden:
  // Hier fliegt aus irgendeinem Grund noch eine Exception (wahrscheinlich innerhalb des Threads) was ich aber erstmal
  // ignoriere: selber schuld, wenn man DAS nicht mehr spielen mag …
  override def onPause() {
    try {
      if (mcSensorManager != null)
        mcSensorManager.unregisterListener(this)
    } catch { case e => Nil }
    try {
      mcServerThread.interrupt
      mcServer.getConnection.close
    } catch { case e => Nil }
    super.onPause
  }

  // Bla bla bla – Brauchen wir nicht: Scheiß Java-Interfaces
  override def onAccuracyChanged(vcSensor: Sensor, vlLvl: Int) {  }

  // Etwas Stumpf: Aber wenn einer was mit dem Touchscreen macht, dann ist das BALLERN!¡!¡
  override def onTouchEvent(vcEvent: MotionEvent) : Boolean = {
    // nun: zuweilen kommt es doch zu mehr ballerei als gewünscht: hier noch filtern …
    mbFire = true;
    return true;
  }

  // Ganz großes Tennis: Die app bekommt hier einen Sinn:
  override def onSensorChanged(vcSensorEvent: SensorEvent) {
    if (tv != null) {
      var c: Float = vcSensorEvent.values(1)
      var l: Boolean = false
      var ll: Boolean = false
      var r: Boolean = false
      var rr: Boolean = false;
      var t: Boolean = (-30 <= vcSensorEvent.values(2) && vcSensorEvent.values(2) <= 30)
      var os: OutputStream = mcServer.getConnection.getOutputStream
      if (c < -10) {
        r = true;
        if (c > -30)
          rr = true;
      } else {
        if ( c > 10) {
          l = true;
          if ( c < 30)
            ll = true;
        }
      }
      if (mbFire || mbLeft != l || mbRight != r || mbThrust != t) {
        os.write(ByteUtil.toByteArray(EntityTypes.Action, l, r, t, mbFire))
        os.flush
        if (ll || rr) {
          os.write(ByteUtil.toByteArray(EntityTypes.Action, false, false, t, mbFire))
          os.flush
        }
      }
    }
    mbFire = false;
  }

  // hässlichen bug bei flip des displays vermeiden: nicht die feine englische, aber it works¡¡
  override def onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  }

}
