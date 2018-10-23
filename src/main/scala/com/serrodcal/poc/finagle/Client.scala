package com.serrodcal.poc.finagle

import com.twitter.finagle.{Http, Service, http}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Await, Future}
import com.typesafe.config.{Config, ConfigFactory}

object Client extends App {

  val config: Config = ConfigFactory.load

  val port: String = config.getString("server.port")

  val client: Service[Request, Response] = Http.client.newService(s"localhost:${port}")

  val request = Request(http.Methods.GET, "/")

  request.setContentString("foo")

  val response: Future[Response] = client(request)

  val futureResponse: Response = Await.result(response)

  println(futureResponse.contentString)

}
