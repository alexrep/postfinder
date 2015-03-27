package com.aktorbackend.infrastructure

import akka.actor._
import akka.pattern.{ ask, pipe, AskTimeoutException }
import akka.routing._
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.util.concurrent.TimeUnit
import akka.util.Timeout

object ActorSystemEnvironment {
  implicit val timeout = Timeout(500, TimeUnit.MILLISECONDS)
  var actorSystem:ActorSystem = null
  val frontName = "frontBalancer"
  def getPosts(tag:String):Future[Iterable[Post]] = {
    val frontBalancer:ActorRef =  actorSystem.actorFor(actorSystem /frontName )
    (frontBalancer ? Tag(tag)).map {l => l.asInstanceOf[Iterable[Post]]}
  }
  def init(): Unit = {
    actorSystem = ActorSystem(name = "postfinder", config = ConfigFactory.load.getConfig("postfinder"))
    val frontBalancer = actorSystem.actorOf(Props[Balancer].withRouter(SmallestMailboxRouter(nrOfInstances = 10)), name=frontName)
  }


}
