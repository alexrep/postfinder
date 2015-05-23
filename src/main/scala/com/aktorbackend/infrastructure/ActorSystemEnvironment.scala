package com.aktorbackend.infrastructure

import akka.actor._
import akka.pattern.{ ask, pipe, AskTimeoutException }
import akka.routing._
import com.aktorbackend.models.{Tag, Post}
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ActorSystemEnvironment {
  var actorSystem:ActorSystem = null
  val frontName = "frontBalancer"
  val workerName = "apiWorker"
  def createApiWorkerActors(count:Int)={
    val routedWorkers =  actorSystem.actorOf(Props[ApiWorker]
      .withRouter(FromConfig()),
      name=workerName)
  }

  def init(): Unit = {
    actorSystem = ActorSystem(name = "postfinder", config = ConfigFactory.load.getConfig("postfinder"))
    val workers = createApiWorkerActors(10)
    val frontBalancer = actorSystem.actorOf(Props[Balancer]
      .withRouter(FromConfig()),
      name=frontName)
  }
  def getWorkers:Iterable[ActorRef] = {
    actorSystem.actorFor(actorSystem /workerName ) :: Nil
  }
  def getPosts(tag:String):Future[Iterable[Post]] = {
    val frontBalancer:ActorRef =  actorSystem.actorFor(actorSystem /frontName )
    (frontBalancer ? Tag(tag)).mapTo[Iterable[Post]]
  }


}
