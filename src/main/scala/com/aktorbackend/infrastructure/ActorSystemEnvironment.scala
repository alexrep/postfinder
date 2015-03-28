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
  val workerName = "workerBalancer"
  def createApiWorkerActors(count:Int)={
    val workers = (for (i <- 0 until count) yield actorSystem.actorOf(Props[ApiWorker]
      .withDispatcher("dispatchers.proxy-actor-dispatcher"),
      name=("apiWorker" + i))).toList
    val internalLoadBalancer = actorSystem.actorOf(
      Props[ApiWorker].withRouter(RoundRobinRouter(routees = workers)), name = workerName)

  }

  def init(): Unit = {
    actorSystem = ActorSystem(name = "postfinder", config = ConfigFactory.load.getConfig("postfinder"))
    val worker = createApiWorkerActors(20)
    val frontBalancer = actorSystem.actorOf(Props[Balancer].withRouter(SmallestMailboxRouter(nrOfInstances = 4)), name=frontName)
  }
  def getWorkers:Iterable[ActorRef] = {
    actorSystem.actorFor(actorSystem /workerName ) :: Nil
  }
  def getPosts(tag:String):Future[Iterable[Post]] = {
    val frontBalancer:ActorRef =  actorSystem.actorFor(actorSystem /frontName )
    (frontBalancer ? Tag(tag)).mapTo[Iterable[Post]]
  }


}
