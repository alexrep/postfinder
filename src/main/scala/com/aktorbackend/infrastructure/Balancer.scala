package com.aktorbackend.infrastructure
import akka.actor._
import akka.actor.{ActorRef, Actor}
import akka.pattern.{ ask, pipe, AskTimeoutException }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * Created by alex on 27.03.15.
 */
class Balancer extends Actor {
  def receive ={
    case tag:Tag=>
      val futures  = ActorSystemEnvironment.getWorkers.map {worker:ActorRef => worker ? tag}
      val concatinated:Future[List[Post]] =  Future.sequence(futures).mapTo[List[List[Post]]].map{
        list:List[List[Post]]=>
          list.foldLeft(List[Post]()){
            (acc:List[Post],postList:List[Post])=>
              acc ++ postList
          }
      }
      concatinated.pipeTo(sender)
  }

}
