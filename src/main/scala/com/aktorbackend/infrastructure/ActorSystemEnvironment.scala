package com.aktorbackend.infrastructure

import akka.actor._
import akka.actor.{ActorRef, Actor}
import akka.pattern.{ ask, pipe, AskTimeoutException }
import akka.routing._
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future

/**
 * Created by alex on 27.03.15.
 */
object ActorSystemEnvironment {
  var actorSystem:ActorSystem = null
  val frontName = "frontBalancer"
  def getPosts(tag:String):Future[Iterable[Post]] = {
    val frontBalancer:ActorRef =  actorSystem.actorFor(actorSystem /frontName )
    frontBalancer ? Tag(tag)
  }
  def init(): Unit = {
    actorSystem = ActorSystem(name = "postfinder", config = ConfigFactory.load.getConfig("postfinder"))
    val frontBalancer = actorSystem.actorOf(Props[Balancer].withRouter(SmallestMailboxRouter(nrOfInstances = 10)), name=frontName)
  }


}
