package labs
import labs.lab1.Atbash.{encryptLetter, encryptText}

import org.scalatest._
import org.scalatest.matchers.should.Matchers

class AtbashTest extends FlatSpec with Matchers{

  "Encrypt letter" should "convert an lowercase letter" in {
    encryptLetter('f') shouldBe 'u'
  }

  it should "convert an uppercase letter" in{
    encryptLetter('W') shouldBe 'D'
  }

  it should "keep symbols" in{
    encryptLetter('@') shouldBe '@'
  }

  "Encrypt text" should "encrypt a word" in{
    encryptText("foobar") shouldBe "ullyzi"
  }

  it should "encrypt a sentence" in {
    encryptText("Hello, it`s one of the oldest methods of encryption.") shouldBe
      "Svool, rg`h lmv lu gsv lowvhg nvgslwh lu vmxibkgrlm."
  }

  it should "decrypt an encrypted text" in {
    encryptText("draziw") shouldBe "wizard"
  }
}