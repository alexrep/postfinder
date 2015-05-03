package com.aktorbackend.infrastructure

import com.aktorbackend.models.{Post=>AbstractPost}
import facebook4j._
import facebook4j.auth.AccessToken
import scala.collection.JavaConversions._

/*
 * Created by alex on 28.03.15.
 */
object FacebookUtils {
  val appId = "986570691355911"
  val appSecret = "c4b7b61a283d30974994910b4c40c6c7"
  val commaSeparetedPermissions = "read_stream,public_profile,email,publish_stream, publish_actions, id, name, first_name, last_name, generic"
  lazy val facebook:Facebook = {
    val fb = new FacebookFactory().getInstance()
    val accessKey = "986570691355911|xF1e_XsekKcIeD8Fx7mhmDX2Ok0"
    fb.setOAuthAppId(appId, appSecret)
    fb.setOAuthPermissions(commaSeparetedPermissions)
    fb.setOAuthAccessToken(new AccessToken(accessKey))
    fb
  }

  def getPosts(search:String):List[AbstractPost]={
    val results:ResponseList[Post] = facebook.searchPosts(search,new Reading().limit(500))
    results.toList.map {post:Post=> AbstractPost(post.getMessage(),post.getName())}
  }

  def main (args: Array[String]) {
    val res = getPosts("scala")
    res.foreach({post=> println(post.text)})
  }


}
