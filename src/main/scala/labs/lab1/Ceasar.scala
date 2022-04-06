package labs.lab1

import scala.annotation.tailrec
import scala.io.StdIn.{readChar, readInt, readLine}

object Ceasar {

  def encrypt(msg: String, shifts: Int): String = {
    val upText = msg.toUpperCase();
    upText.map{
      case c if 'A'.toInt to 'Z'.toInt contains c.toInt => ((c.toInt - 65) + shifts) % 26 match {
        case x if (x < 0) => (x + 91).toChar
        case x if (x >= 0) => (x + 65).toChar
      }
      case c if (!('A'.toInt to 'Z'.toInt contains c.toInt)) => c
    }
  }

  def decrypt(encryptedMsg: String, shifts: Int): String = {
    val upText = encryptedMsg.toUpperCase();
    upText.map {
      case c if 'A'.toInt to 'Z'.toInt contains c.toInt => ((c.toInt - 65) - shifts) % 26 match {
          case x if (x < 0) => (x + 91).toChar
          case x if (x >= 0) => (x + 65).toChar
        }
      case c if (!('A'.toInt to 'Z'.toInt contains c.toInt)) => c
      }
    }

  def cipher(method: Char): Unit = method match {
    case 'e' =>
      print("Message : ")
      val message: String = readLine()
      print("Shifts : ")
      val shifts: Int = readInt()
      println("Encrypted message : " + encrypt(message, shifts))
    case 'd' =>
      print("Message : ")
      val message: String = readLine()
      print("Shifts : ")
      val shifts: Int = readInt()
      println("Decrypted message : " + decrypt(message, shifts))
    case _ =>
      println("Input : Invalid")
  }

  def menu(): Unit = {
    println("For encryption press: e")
    println("For decryption press: d")
    print("Your choice: ")
    cipher(readChar())
  }

  def main(args: Array[String]): Unit = {
    @tailrec
    def retry(times: Int): Unit = {
      menu()
      println('\n')
      if (times <= 1) {
        println("Attempts are ended.")
        System.exit(0)
      }
      else {
        retry(times - 1)
      }
    }
    retry(5)
  }
}