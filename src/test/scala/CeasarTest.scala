package labs
import labs.lab1.Ceasar.{encrypt, decrypt}

import org.scalatest._
import org.scalatest.matchers.should.Matchers

class CeasarTest extends FlatSpec with Matchers{

  "Encrypt text with shift 1" should "map +1" in {
    encrypt("A", 1) shouldBe "B"
  }

  it should "move +5" in {
    encrypt("A", 5) shouldBe "F"
  }

  it should "encrypt a sentence" in {
    encrypt("Hello, it`s one of the oldest methods of encryption.", 2) shouldBe
      "JGNNQ, KV`U QPG QH VJG QNFGUV OGVJQFU QH GPETARVKQP."
  }

  it should "decrypt an encrypted text" in {
    decrypt("JGNNQ, KV`U QPG QH VJG QNFGUV OGVJQFU QH GPETARVKQP.", 2)shouldBe
      "HELLO, IT`S ONE OF THE OLDEST METHODS OF ENCRYPTION."
  }
}