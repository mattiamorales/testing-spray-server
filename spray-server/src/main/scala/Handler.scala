import akka.actor.{ActorLogging, Actor, ActorRef}
//import spray.httpx.RequestBuilding._
import spray.http._
import HttpMethods._
import HttpHeaders._
import ContentTypes._
import spray.client.pipelining._
import scala.util.{Success, Failure}
import scala.concurrent.Future
import scala.concurrent.future
import spray.routing.RequestContext

class Handler extends Actor with ActorLogging {
  
	//log.info( context.self.path.toString)
	import context.dispatcher
  
    def receive = {
      
	  case Msg(parm:String) =>
        
        log.info(Thread.currentThread.getId.toString)

        /*sender ! Future {*/
        	Thread.sleep(10000)
        	sender ! Msg(parm + " recibido.") 
	    /*}*/
	      
	     
      case r: RequestContext => 
        	Thread.sleep(10000)
        	r.complete("directo") 
        
      case _ =>
	  
	}    
  
}