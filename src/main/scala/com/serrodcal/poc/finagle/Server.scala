package com.serrodcal.poc.finagle

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.{Await, Future}
import com.twitter.finagle.{Http, Service}
import com.typesafe.config.{Config, ConfigFactory}


object Server extends App {

  val config: Config = ConfigFactory.load

  val service = new Service[Request, Response] {
    override def apply(request: Request): Future[Response] = Future {
      println(s"Request recived with following body: ${request.getContentString()}")
      val response = Response()
      response.headerMap.put("Content-Type", "text/plain")
      response.contentString = s"Hi, world!"
      response
    }
  }

  val port: String = config.getString("server.port")

  val server = Http.serve(":" + port, service)
  Await.ready(server)

  println(s"Server started and listening at ${port}...")

}
