import play.api.libs.ws.ning._
import play.api.libs.json._
import play.api.libs.ws._
import scala.concurrent.Future

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
    // the option is probably useless 
    def <||> (y:String):Option[Future[WSResponse]] = x.map {req => req.execute(y)}
  }


  //implicit class ripOutStatus(x:Option[Future[WSResponse]] ){
  //  def </\>(y:String):String = "yeah "+y+ " "+x.toString
  //}
}
