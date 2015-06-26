package controllers


import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.json._
import java.sql.{Connection, DriverManager}
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import scala.collection.mutable.ArrayBuffer

class Application extends Controller {

  def index = Action {
    val conn = play.api.db.DB.getConnection()
    var Games = ArrayBuffer[Map[String, String]]()
    //    var Games = scala.collection.mutable.Map[String, Map[String, String]]()
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT game.id as gameid, game.id_team, game.gametime, team.id, team.name, team.city FROM game JOIN team ON game.id_team=team.id ORDER BY game.gametime")
      while (rs.next()) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
        val date = LocalDateTime.parse(rs.getString("gametime"), formatter)
        val Game = scala.collection.immutable.Map(
          "name" -> rs.getString("name"),
          "city" -> rs.getString("city"),
          "id_team" -> rs.getString("id_team"),
          "dow" -> date.getDayOfWeek().toString(),
          "day" -> date.getDayOfMonth().toString(),
          "month" -> date.getMonth().toString(),
          "hour" -> date.getHour().toString(),
          "minute" -> date.getMinute().toString(),
          "gameid" -> rs.getString("gameid")
        )

        Games += Game
      }
    } finally {
      conn.close()
    }
    Ok(views.html.Application(Games.toArray))
  }

  def facebook = Action { request =>
    val body: AnyContent = request.body
    body.asFormUrlEncoded.map { form =>
      val email = form.get("email").get(0)
      val fname = form.get("first_name").get(0)
      val lname = form.get("last_name").get(0)
      val authprov_uid = form.get("id").get(0)

      val conn = play.api.db.DB.getConnection()
      try {
        val stmt = conn.createStatement
        val rs = stmt.executeQuery("SELECT * FROM user JOIN authprov ON authprov.id=user.id_authprov WHERE user.authprov_uid='" + authprov_uid + "' AND authprov.name='facebook' LIMIT 1")
        val something = rs.next()
        if (something) {
        } else {
          val stmt = conn.createStatement
          val rs = stmt.executeUpdate("INSERT INTO user (email, fname, lname, authprov_uid, id_authprov) values ('" + email + "', '" + fname + "', '" + lname + "', '" + authprov_uid + "', 1)")
        }
      } finally {
        conn.close()
      }
    }.getOrElse {
      BadRequest("Bad bad bad")
    }
    Ok(Json.obj("response" -> 1))
  }

  def gmail = Action { request =>
    val body: AnyContent = request.body
    body.asFormUrlEncoded.map { form =>
      val email = form.get("email").get(0)
      val fname = form.get("first_name").get(0)
      val lname = form.get("last_name").get(0)
      val authprov_uid = form.get("id").get(0)

      val conn = play.api.db.DB.getConnection()
      try {
        val stmt = conn.createStatement
        val rs = stmt.executeQuery("SELECT * FROM user JOIN authprov ON authprov.id=user.id_authprov WHERE user.authprov_uid='" + authprov_uid + "' AND authprov.name='google' LIMIT 1")
        val something = rs.next()
        if (something) {
        } else {
          val stmt = conn.createStatement
          val rs = stmt.executeUpdate("INSERT INTO user (email, fname, lname, authprov_uid, id_authprov) values ('" + email + "', '" + fname + "', '" + lname + "', '" + authprov_uid + "', 2)")
        }
      } finally {
        conn.close()
      }
    }.getOrElse {
      BadRequest("Bad bad bad")
    }
    Ok(Json.obj("response" -> 1))
  }

}
