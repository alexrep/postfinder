package com.aktorbackend.controller

import com.aktorbackend.infrastructure._
import com.typesafe.play.mini._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.Future

object App extends Application {

  implicit val locationWrites = new Writes[Post] {
    def writes(post: Post) = Json.obj(
      "title" -> "Some title",
      "text" -> post.text
    )
  }
  def route = {
    case GET(Path("/search")) & QueryString(qs) =>  Action{
      val tag:String = QueryString(qs,"tag").getOrElse("").toString
      val posts : Future[Iterable[Post]]  =  ActorSystemEnvironment.getPosts(tag)
      val result = posts.map {
        postList =>{
          val json = postList.map((post:Post) => Json.toJson(post)).foldLeft[JsArray](new JsArray()){ (array:JsArray,jsPost:JsValue)=> array :+ jsPost}
          Ok(json).as("text/html")
        }
      }
      AsyncResult(result)

    }
  }
}
