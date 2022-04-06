package labs.lab1
import scala.io.StdIn.readLine

object Atbash {

  def encryptText(text: String): String = {
    text.map(x => encryptLetter(x))
  }

  def encryptLetter(letter: Char): Char = letter match {
    case lower if letter.isLower => (('z' - lower) + 'a').toChar
    case upper if letter.isUpper => (('Z' - upper) + 'A').toChar
    case _ => letter
  }

  def main(args: Array[String]) {
    print("Enter your text: ")
    val input_string = readLine()
    println(encryptText(s"$input_string"))
  }
}
