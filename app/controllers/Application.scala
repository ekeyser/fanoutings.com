package controllers


import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.json._
import java.sql.{Connection, DriverManager}
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

class Application extends Controller {

  def index = Action {
    val conn = play.api.db.DB.getConnection()
    var Games = scala.collection.mutable.Map[String, Map[String, String]]()
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT game.id as gameid, game.id_visitor, game.gametime, visitor.id, visitor.name, visitor.city FROM game JOIN visitor ON game.id_visitor=visitor.id ORDER BY game.gametime")
      while (rs.next()) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
        val date = LocalDateTime.parse(rs.getString("gametime"), formatter)
        val Game = scala.collection.immutable.Map(
          "name" -> rs.getString("name"),
          "city" -> rs.getString("city"),
          "id_visitor" -> rs.getString("id_visitor"),
          "dow" -> date.getDayOfWeek().toString(),
          "day" -> date.getDayOfMonth().toString(),
          "month" -> date.getMonth().toString(),
          "hour" -> date.getHour().toString(),
          "minute" -> date.getMinute().toString()
        )

        Games += (rs.getString("gameid") -> Game)
      }
    } finally {
      conn.close()
    }
    Ok(views.html.index(Games.toMap))
  }

  def facebook = Action { request =>
    val conn = play.api.db.DB.getConnection()
    val userid = request.id
    //    request.queryString.map { case (k,v) => k -> v.mkString }
    //    println(request)
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT * FROM user JOIN authprov ON authprov.id=user.id_authprov WHERE user.authprov_id=" + userid + " AND authprov.name='facebook'")
      println(rs)
    } finally {
      conn.close()
    }
    Ok(Json.obj("response" -> 1))
  }

}
