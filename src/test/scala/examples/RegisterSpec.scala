package examples

import org.specs2._
import dispatch._
import execute._
import matcher.DataTables
import com.codahale.jerkson.Json._

class RegisterSpec extends Specification with DataTables { def is = noindent  ^ """

### Spec

The registration service should allow users to register theirselves using the following fields:

 * name
 * email
 * phone no
 * role
 * two equal passwords

### Examples                                                                  """ ^
  "allow registring using normal field values"                                ! t1^
  "not allow registering with different passwords or missing arguments"       ! t2^
  end

  val register = :/("localhost", 9998) / "register"

  def t1 = {
    val params = Map("name" -> "Kalle", "email" -> "kalle@hemma.se", "phone" -> "08-987654", "role" -> "Musician", "password1" -> "kalle123", "password2" -> "kalle123")
    val res = Http(register << params as_str)

    val json = parse[Map[String, String]](res)
    json("name") mustEqual(params("name"))
    json("email") mustEqual(params("email"))
    json("phone") mustEqual(params("phone"))
    json("role") mustEqual(params("role"))
    json("password") mustNotEqual("")
  }

  def t2 =
      "name"  || "email"            | "phone"     | "role"  | "password1" | "password2" |
      "Kalle" !! "kalle@hemma.se"   ! "08-987654" ! "Fan"   ! "kalle123"  ! "kalle456"  |
      ""      !! "kalle@hemma.se"   ! "08-987654" ! "Fan"   ! "kalle123"  ! "kalle123"  |
      "Kalle" !! ""                 ! "08-987654" ! "Fan"   ! "kalle123"  ! "kalle123"  |
      "Kalle" !! "kalle@hemma.se"   ! ""          ! "Fan"   ! "kalle123"  ! "kalle123"  |
      "Kalle" !! "kalle@hemma.se"   ! "08-987654" ! ""      ! "kalle123"  ! "kalle123"  |
      "Kalle" !! "kalle@hemma.se"   ! "08-987654" ! "Fan"   ! ""          ! ""          |> {
      (name, mail, phone, role, password1, password2) => registeringShouldFail((name, mail, phone, role, password1, password2))
    }

  type Tup6 = Tuple6[String, String, String, String, String, String]

  def registeringShouldFail(tup: Tup6): Result = {
    val params = Map("name" -> tup._1, "email" -> tup._2, "phone" -> tup._3, "role" -> tup._4, "password1" -> tup._5, "password2" -> tup._6)
    val res = Http(register << params as_str)

    val json = parse[Map[String, String]](res)
    json("status") mustEqual("not ok")
  }
}
