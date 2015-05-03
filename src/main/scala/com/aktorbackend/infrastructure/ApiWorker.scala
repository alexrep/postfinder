package com.aktorbackend.infrastructure
import akka.actor._
import akka.actor.{ActorRef, Actor}
import akka.pattern.{ ask, pipe, AskTimeoutException }
import com.aktorbackend.models.Tag
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * Created by alex on 28.03.15.
 */
class ApiWorker extends Actor {
  def receive ={
    case tag:Tag =>
      val futurePosts = Future {
        TwitterUtils.getPosts(tag.name)
      }

      futurePosts pipeTo sender

  }


}
