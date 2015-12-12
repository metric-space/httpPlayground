package controllers

import play.api._
import play.api.mvc._
import play.api.Logger

class Application extends Controller {

  def wildCardFunction = Action { implicit request =>
    Logger.debug("===========================================")
    Logger.debug("METHOD:")
    Logger.debug(" |-- "+request.method)
    Logger.debug("HEADERS:")
    for (a <- request.headers.toMap) {
      Logger.debug(" |-- "+a.toString)
    }
    Logger.debug("BODY:")
    Logger.debug(" |--"+request.body.toString)
    Ok("Your new application is ready.")
  }

}
