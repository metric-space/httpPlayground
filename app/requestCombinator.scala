import play.api.libs.ws.ning._
import play.api.libs.json._
import play.api.libs.ws._
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import play.api.Logger

package object weirdCombinators {

  implicit val client = NingWSClient()

  def liftez(x:String):Option[WSRequest] = {
    val matcher = """^http(s){0,1}://.+""".r
    x match {
      case matcher(_*) => Option(WS.clientUrl(x))
      case _ => None
    }}

  implicit class combinators(x:Option[WSRequest]){
    // yay functors with map
    def <*> (y:(String,String)) :Option[WSRequest] = x map {req => req.withHeaders(y)}

    // the option is probably useless , needs checking for appropriate method
    def <||> (y:String):Option[WSRequest] = x map { _.withMethod(y) }

  }

  implicit class executeAndRipOutStatus(x:Option[WSRequest]){
    def </~\>(y:String):Any = {
      // damn this thing is ugly
      val nestedMethod: String = x map {_.method} get
      val wrappedResponse:Option[Future[WSResponse]] = x map {req => req.execute(nestedMethod)}
 
      val result = y match {
        case "status" => wrappedResponse.map { _.onSuccess { case response => response.status}}
        case "statusText" => wrappedResponse.map { _.onSuccess { case response => response.statusText}}
        case "body" => wrappedResponse.map { _.onSuccess { case response => response.body}}
        case _     => "method doesn't exist"
      }
      Await.result(wrappedResponse.get,20.seconds) 
      return result
     
     wrappedResponse map {_.onSuccess {case response => response}}
    }
  }

 def QUIT = client.close
}
