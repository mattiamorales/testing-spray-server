import spray.routing.{Route, HttpService}
import akka.actor.{ActorLogging, Actor, ActorRef, Props}

class Server( handler:ActorRef ) extends HttpService with Actor with PerRequestCreator with ActorLogging {

	//import spray.routing.PathMatchers._
	
	implicit def actorRefFactory = context
  
	
/*	def getMeAResponse(input:String) = Future {
    	var it = 0
    	while (it < Long.MaxValue) { 
    	  it = it + 1 
    	}
    	input + " recibido." 
    }*/
	
	def receive = runRoute(route) 
	
	val route = {
	    get {
	      pathPrefix("pathPrefix"){
	          parameterMap { params =>
	            
	            log.info("Request")
	            log.info(Thread.currentThread.getId.toString)
	            
	            spawn {
	            	Msg(params("parm"))
	            } 
	            
	         }
	      }
	    }
	}
	
	def spawn(message : RestMessage): Route = 
	  ctx => perRequest(ctx, Props(new Handler), message)
	
}