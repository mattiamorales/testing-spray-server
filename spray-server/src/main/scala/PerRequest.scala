import akka.actor._
import akka.actor.SupervisorStrategy.Stop
import spray.http.StatusCodes._
import spray.routing.RequestContext
import akka.actor.OneForOneStrategy
import spray.httpx.Json4sSupport
import scala.concurrent.duration._
import scala.concurrent.{Future,future}
import org.json4s.DefaultFormats
import spray.http.StatusCode
import scala.util.{Success, Failure}
import PerRequest._

case class Error(message: String)
case class Validation(message: String)

trait PerRequest extends Actor with Json4sSupport with ActorLogging {

  import context._

  val json4sFormats = DefaultFormats

  log.info(Thread.currentThread.getId.toString)
  
  def r: RequestContext
  def target: ActorRef
  def message: RestMessage

  setReceiveTimeout(15.seconds)
  target ! message

  def receive = {
    case f: Future[RestMessage] => oCom(f)
    case res: RestMessage => complete(OK, res)
    case v: Validation    => complete(BadRequest, v)
    case ReceiveTimeout   => complete(GatewayTimeout, Error("Request timeout"))
  }
  
  def oCom(f:Future[RestMessage]) = f onComplete {
    case Success(s) => complete(OK, s)
    case Failure(t) => println("An error has occured: " + t.getMessage)
  }

  def complete[T <: AnyRef](status: StatusCode, obj: T) = {
    r.complete(status, obj)
    stop(self)
  }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case e => {
        complete(InternalServerError, Error(e.getMessage))
        Stop
      }
    }
}

object PerRequest {
  case class WithActorRef(r: RequestContext, target: ActorRef, message: RestMessage) extends PerRequest

  case class WithProps(r: RequestContext, props: Props, message: RestMessage) extends PerRequest {
    lazy val target = context.actorOf(props)
  }
}

trait PerRequestCreator {
  this: Actor =>

  def perRequest(r: RequestContext, target: ActorRef, message: RestMessage) =
    context.actorOf(Props(new WithActorRef(r, target, message)))

  def perRequest(r: RequestContext, props: Props, message: RestMessage) =
    context.actorOf(Props(new WithProps(r, props, message)))
}