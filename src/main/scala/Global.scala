import com.aktorbackend.controller.App
import com.aktorbackend.infrastructure._

object Global extends com.typesafe.play.mini.Setup(App) {
  println("Initializing backend")
  ActorSystemEnvironment.init()

}