# FinaglePoC

This is a first contact with Finagle.

This project consist of two simple components:

* A server built with Finagle.
* A client built with Finagle.
 
## Server

The imports needed are:

```scala
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.{Await, Future}
import com.twitter.finagle.{Http, Service}
import com.typesafe.config.{Config, ConfigFactory}
```

The server is:

```scala
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
``` 

## Client

The imports needed are:

```scala
import com.twitter.finagle.{Http, Service, http}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Await, Future}
import com.typesafe.config.{Config, ConfigFactory}
```

The server is:

```scala
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
``` 