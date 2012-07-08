package examples

import org.specs2._
import dispatch._
import execute._
import matcher.DataTables
import com.codahale.jerkson.Json._

class RegisterVenueSpec extends Specification with DataTables {
  def is = noindent ^ """

### Spec

A user should be able to register a venue (place) where a jam session can take place.

Actors: System, Users = Musician and Venue Owner.

Data that can be registered and showed by system should be:

Venue Name, Address (Street + No) = Mandatory, Postal Code, City = Mandatory,
Public/Private, Maximum No of Musicians, Contact = Mandatory

### Examples
                                                            """ ^
    "allow registring venues using normal field values"     ! t1 ^
    "not allow registering venues with missing arguments"   ! t2 ^
    end

  private type Tup7 = Tuple7[String, String, String, String, Boolean, Int, String]
  private val register = :/("localhost", 8080) / "jamit-logic/register-venue"

  private def t1 =
    "name"        || "address"        | "postalCode"  | "city"          | "public"  | "maxNoOfMusicians"  | "contact"                                 |
    ""            !! "Storgatan 3"    ! "12345"       ! "Storstad"      ! false     ! 5                   ! "bilbo@hobsala.se"                        |
    ""            !! "Storgatan 3"    ! "12345"       ! "Storstad"      ! false     ! 0                   ! "Berra 070-9999999"                       |
    ""            !! "Storgatan 3"    ! "12345"       ! "Storstad"      ! false     ! 7                   ! "bluesbandet@boden.se"                    |
    "Mad Bar"     !! "Storgatan 3"    ! "123 45"      ! "Storstad"      ! true      ! 4                   ! "meshuggah@live.se"                       |
    "Mäd Bar"     !! "Stora gatan 3"  ! "12345"       ! "Stora staden"  ! true      ! 6                   ! "textures.band@metal.nu"                  |
    "Mad Bar 99"  !! "Storgatan 333"  ! ""            ! "Storstaden"    ! true      ! 0                   ! "Sune 070-6666666 sunesmail@hotmail.com"  |
    "Maddes Lya"  !! "Storgatan 3"    ! "12345"       ! "Storstaden"    ! false     ! 2                   ! "madde@hemma.nu"                          |> {
    (name, address, postalCode, city, public, maxNoOfMusicians, contact) => registeringShouldWork((name, address, postalCode, city, public, maxNoOfMusicians, contact))
  }

  private def t2 =
    "name" || "address"     | "postalCode"  | "city"      | "public"  | "maxNoOfMusicians"  | "contact"             |
    "Mad"  !! ""            ! "12345"       ! "Storstad"  ! true      ! 66                  ! "opeth@bestband.nu"   |
    "Mad"  !! "Storgatan"   ! "12345"       ! ""          ! true      ! 5                   ! ""                    |> {
    (name, address, postalCode, city, public, maxNoOfMusicians, contact) => registeringShouldFail((name, address, postalCode, city, public, maxNoOfMusicians, contact))
  }

  private def mapTup(tup: Tup7) = Map("name" -> tup._1, "address" -> tup._2, "postalCode" -> tup._3, "city" -> tup._4, "public" -> tup._5.toString, "maxNoOfMusicians" -> tup._6.toString, "contact" -> tup._7)

  private def registeringShouldWork(tup: Tup7): Result = {

    val params = mapTup(tup)
    val res = Http(register << params as_str)
    val json = parse[Map[String, String]](res)

    json("name") mustEqual params("name")
    json("address") mustEqual params("address")
    json("postalCode") mustEqual params("postalCode")
    json("city") mustEqual params("city")
    json("public") mustEqual params("public")
    json("maxNoOfMusicians") mustEqual params("maxNoOfMusicians")
    json("contact") mustEqual params("contact")
  }

  private def registeringShouldFail(tup: Tup7): Result = {
    val params = mapTup(tup)
    val res = Http(register << params as_str)

    val json = parse[Map[String, String]](res)
    json("status") mustEqual ("not ok")
  }
}
