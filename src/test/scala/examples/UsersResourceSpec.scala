package examples

import org.specs2._
import mutable.Specification
import dispatch._

class UsersResourceSpec extends Specification {

  val usersUrl = :/("localhost", 9998) / "users"

  "UsersResources" should {
    "return all users" in {
      val res = Http(usersUrl as_str)
      println("res: " + res)
      res mustEqual """[{"id":1,"name":"Kalle Karlsson","email":"kalle@karlsson.se","role":"Local owner","password":"n0VDTwuTf7c+qZyH3urq7bz44uPUWdYN"},{"id":2,"name":"Lasse Larsson","email":"lasse@larsson.se","role":"Musician","password":"ymnbD1iuLVxoxplq8kmGPk+lz/4OvpXp"},{"id":3,"name":"David Davidsson","email":"david@davidsson.se","role":"Fan","password":"5IPdOiNPFfBf04Esck+BuFWR1Uzm0mXS"}]"""
    }
  }
}
