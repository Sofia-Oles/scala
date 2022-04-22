package labs.lab2

import scala.annotation.tailrec
import scala.io.StdIn.{readInt, readLine}

object Algo {

  def binarySearch(list: Array[Int], target: Int): Int = {
    @tailrec
    def bs(list: Array[Int], target: Int, start: Int, end: Int): Int = {
      list match {
        case n if start > end => -1
        case arr: Array[Int] if arr(start + (end - start + 1) / 2) == target
        => start + (end - start + 1) / 2
        case arr: Array[Int] if arr(start + (end - start + 1) / 2) > target
        => bs(list, target, start, start + (end - start + 1) / 2 - 1)
        case arr: Array[Int] if arr(start + (end - start + 1) / 2) < target
        => bs(list, target, start + (end - start + 1) / 2 + 1, end)
      }
    }
    bs(list, target, 0, list.length - 1)
  }


  def findMaximumProfit(stockPrices: Array[Int]): Int = {
    val maximumSellPricesFromIonward = stockPrices
      .view
      .scanRight(0)({ case (maximumPriceSoFar, dayPrice) => Math.max(maximumPriceSoFar, dayPrice)})
      .toArray
      .drop(1)

    stockPrices match {
      case x if (stockPrices.length < 2) => -1
      case _ =>
      stockPrices
        .zip(maximumSellPricesFromIonward)
        .map({ case (buyPrice, sellPrice) => getPotentialProfit(buyPrice = buyPrice, sellPrice = sellPrice)})
        .max
    }
  }

  def getPotentialProfit(buyPrice: Int, sellPrice: Int): Int = buyPrice match {
    case x if (sellPrice > buyPrice) => (sellPrice - buyPrice)
    case _ => -1
  }

  @tailrec
  def gcd(a: Int, b: Int): Int = b match {
    case 0 => a
    case x => gcd(x, a % x)
  }

  def lcm(a: Int, b: Int): Int = a * (b / gcd(a, b))

  def fib(n: Int): BigInt = n match {
    case 0 => 0
    case 1 => 1
    case _ => fib(n - 2) + fib(n - 1)
  }

  def fibTail(num: Int): BigInt = {
    @tailrec
    def fibFcn(n: Int, a: BigInt, b: BigInt): BigInt = n match {
      case 0 => a
      case 1 => b
      case _ => fibFcn(n - 1, b, a + b)
    }
    fibFcn(num, 0, 1)
  }

  def time[T](f: => T): T = {
    val start = System.nanoTime()
    val func = f
    val end = System.nanoTime()
    println(s"Time spent: " + (end - start) + " ns")
    func
  }

  def manager(method: Int): Unit = method match {
    case 1 =>
      print("First number: ")
      val num1: Int = readInt()
      print("Second number: ")
      val num2: Int = readInt()
      println("Result: " + gcd(num1, num2))
    case 2 =>
      print("First number: ")
      val num1: Int = readInt()
      print("Second number: ")
      val num2: Int = readInt()
      println("Result: " + lcm(num1, num2))
    case 3 =>
      print("Enter number: ")
      val num: Int = readInt()
      println("Fibonacci result: " + time(fib(num)))
    case 4 =>
      print("Enter number: ")
      val num: Int = readInt()
      println("Fibonacci result: " + time(fibTail(num)))
    case 5 =>
      print("Input array numbers: ")
      val s = readLine()
      print("Input el to find: ")
      val n = readInt()
      println("Founded index: " + binarySearch(s.split(" ").map(_.toInt), n))
    case 6 =>
      print("Input array numbers: ")
      val s = readLine()
      println("Res: " + findMaximumProfit(s.split(" ").map(_.toInt)))
    case _ =>
      println("Input : Invalid")
  }

  def menu(): Unit = {
    println("GCD function: 1")
    println("LCM function: 2")
    println("Fibonacci: 3")
    println("Fibonacci tail: 4")
    println("Binary Search: 5")
    println("Find Maximum Profit: 6")
    print("\nYour choice: ")
    manager(readInt())
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
    retry(10)
  }
}
