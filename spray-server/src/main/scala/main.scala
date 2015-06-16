import akka.actor.{ActorSystem, Props, ActorRef}
import spray.http._
import spray.can.Http
import akka.io.IO
import akka.routing.RoundRobinPool
import com.typesafe.config.ConfigFactory

//case class Msg(command:MsgType, args:List[AnyRef])
//case class Query(uri:Uri, handler:ActorRef, msg: Msg) 
//case class Translate(content: String)
//case class Post(contentXml: scala.xml.Elem)
trait RestMessage
case class Msg(s:String) extends RestMessage

object main extends App {
  
	println( "--------------- CIAO ---------------")
  
	val conf = ConfigFactory.load("test")
	implicit val system = ActorSystem("spray-server")
	val handler 	= system.actorOf(Props(new Handler), "handler")
	val server		= system.actorOf(Props(new Server( handler)).withDispatcher("my-dispatcher"), "server")
	//val services	= system.actorOf(RoundRobinPool(2).props(Props[Server]), "server")
	
	IO(Http) ! Http.Bind(server, "localhost", port = 8091)
	
	println("Setup ready!")
	
	//system.awaitTermination()
	scala.io.StdIn.readLine(s"Hit ENTER to exit ...${System.getProperty("line.separator")}")
	system.shutdown()
	
	println( "--------------- CIAO ---------------")

}