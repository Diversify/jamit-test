package examples

import org.specs2._
import dispatch._
import execute._
import matcher.DataTables
import com.codahale.jerkson.Json._

class RegisterUserSpec extends Specification with DataTables { def is = noindent  ^ """

### Spec

Actors: Users = Musician, Fan/Auditor and Venue Owner.

A user should be able to be registered. Data that can be registered should be:

User Name = Mandatory, E-mail = Mandatory, Phone, Role = Mandatory, Password (x2)

### Examples
                                                                                    """ ^
  "allow registring users using normal field values"                                ! t1^
  "not allow registering userswith different passwords or missing arguments"        ! t2^
  end

  private type Tup6 = Tuple6[String, String, String, String, String, String]
  private val register = :/("localhost", 8080) / "jamit-logic/register-user"

  private def t1 =
      "name"  || "email"            | "phone"       | "role"  | "password1" | "password2" |
      "Kalle" !! "kalle@hemma.se"   ! "08-987654"   ! "Fan"   ! "kalle123"  ! "kalle123"  |
      "Lasse" !! "lasse@borta.se"   ! "0709-877554" ! "Fan"   ! "lasse654"  ! "lasse654"  |> {
      (name, mail, phone, role, password1, password2) => registeringShouldWork((name, mail, phone, role, password1, password2))
    }

  private def t2 =
      "name"  || "email"            | "phone"     | "role"  | "password1" | "password2" |
      "Kalle" !! "kalle@hemma.se"   ! "08-987654" ! "Fan"   ! "kalle123"  ! "kalle456"  |
      ""      !! "kalle@hemma.se"   ! "08-987654" ! "Fan"   ! "kalle123"  ! "kalle123"  |
      "Kalle" !! ""                 ! "08-987654" ! "Fan"   ! "kalle123"  ! "kalle123"  |
      "Kalle" !! "kalle@hemma.se"   ! ""          ! "Fan"   ! "kalle123"  ! "kalle123"  |
      "Kalle" !! "kalle@hemma.se"   ! "08-987654" ! ""      ! "kalle123"  ! "kalle123"  |
      "Kalle" !! "kalle@hemma.se"   ! "08-987654" ! "Fan"   ! ""          ! ""          |> {
      (name, mail, phone, role, password1, password2) => registeringShouldFail((name, mail, phone, role, password1, password2))
    }

  private def mapTup(tup: Tup6) = Map("name" -> tup._1, "email" -> tup._2, "phone" -> tup._3, "role" -> tup._4, "password1" -> tup._5, "password2" -> tup._6)

  private def registeringShouldWork(tup: Tup6): Result = {
    val params = mapTup(tup)
    val res = Http(register << params as_str)

    val json = parse[Map[String, String]](res)
    json("name") mustEqual(params("name"))
    json("email") mustEqual(params("email"))
    json("phone") mustEqual(params("phone"))
    json("role") mustEqual(params("role"))
    json("password") mustNotEqual("")
  }

  private def registeringShouldFail(tup: Tup6): Result = {
    val params = mapTup(tup)
    val res = Http(register << params as_str)

    val json = parse[Map[String, String]](res)
    json("status") mustEqual("not ok")
  }
}
