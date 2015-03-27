import com.aktorbackend.infrastructure._

object Global extends com.typesafe.play.mini.Setup(com.aktorbackend.controller.App) {
  println("Initializing backend")
  ActorSystemEnvironment.init()

}