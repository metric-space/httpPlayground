import play.api.libs.ws.ning._
import play.api.libs.json._
import play.api.libs.ws._
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

package object weirdCombinators {

  implicit val client = NingWSClient()

  def liftez(x:String):Option[WSRequest] = {
    val matcher = """^http(s){0,1}://.+""".r
    x match {
      case matcher(_*) => Option(WS.clientUrl(x))
      case _ => None
    }}

  implicit class addHeader(x:Option[WSRequest]){
    // yay functors with map
    def <*> (y:(String,String)) :Option[WSRequest] = x.map {req => req.withHeaders(y)}
  }

  implicit class addMethodAndExec(x:Option[WSRequest]){
    // the option is probably useless , needs checking for appropriate method
    def <||> (y:String):Option[Future[WSResponse]] = x.map {req => req.execute(y)}
  }


  implicit class ripOutStatus(x:Option[Future[WSResponse]] ){
    def </\>(y:String):Any = {
      // damn this thing is ugly
      val result = y match {
        case "status" => x.map { _.onSuccess { case response => response.status}}
        case "statusText" => x.map { _.onSuccess { case response => response.statusText}}
        case "body" => x.map { _.onSuccess { case response => response.body}}
        case _     => "method doesn't exist"
      }
      Await.result(x.get,20.seconds) 
      return result
    }
  }

 def QUIT = client.close
}
