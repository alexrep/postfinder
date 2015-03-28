package com.aktorbackend.infrastructure
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io.IOUtils
import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Created by alex on 28.03.15.
 */
object TwitterUtils {
  val AccessToken = "496792695-THmXMrO2bWmKGHgUlEXptR8KtYbeUPRP5oA6Mv5i"
  val AccessSecret = "WzNAL7nJ9QKt7ZaQ9j6zO7y3Ia3s0uOUCV4IUs7cwmTbR"
  val ConsumerKey = "tTXGWkC3LNfWNttViCFVmlrak"
  val ConsumerSecret = "XPR1AkUsaMHHqWqqKhdmWkrYOtu246ZCgx0IK52eo4zHxEee7Y"
  implicit val twitterPostReads: Reads[Post] = (
         (JsPath \ "text").read[String] and
           (JsPath \ "user"\\ "name").read[String]
           )(Post.apply _)


  def getPosts(tag:String):List[Post]={
    val url = s"https://api.twitter.com/1.1/search/tweets.json?q=%40$tag"
    val parsedJson = Json.parse(loadData(url))
    val postJsonArray:JsArray =
      parsedJson \ "statuses" match{
        case arr : JsArray => arr
        case _   => JsArray()
      }
    postJsonArray.as[List[Post]]

  }
  def main (args: Array[String]) {
    val posts = getPosts("scala")
    posts.foreach {post => println(post.text)}

  }

  def loadData(url:String):String ={
    val consumer = new CommonsHttpOAuthConsumer(ConsumerKey,ConsumerSecret)
    consumer.setTokenWithSecret(AccessToken, AccessSecret)
    val request = new HttpGet(url)
    consumer.sign(request)
    val client = new DefaultHttpClient()
    val response = client.execute(request)
    IOUtils.toString(response.getEntity().getContent())
  }

}
