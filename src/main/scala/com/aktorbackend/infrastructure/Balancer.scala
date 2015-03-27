package com.aktorbackend.infrastructure
import akka.actor._
import akka.actor.{ActorRef, Actor}

/**
 * Created by alex on 27.03.15.
 */
class Balancer extends Actor {
  def receive ={
    case Tag(name)=>
      val result = Post("Test Post") :: Nil
      sender ! result
  }

}
