package examples

import org.specs2.mutable.Specification
import dispatch._

class RegisterSpec extends Specification {

  "Registration Examples".title

  val register = :/("localhost", 9998) / "register"

  "Register Service" should {
    "allow registring using normal fields" in {
      val params = Map("name" -> "Kalle", "email" -> "kalle@hemma.se", "role" -> "Musician", "password1" -> "kalle123", "password2" -> "kalle123")
      val resp = Http(register << params as_str)
      resp mustEqual """tjohej"""
    }
  }

}
