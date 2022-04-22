package labs

import labs.lab2.Algo.{binarySearch, fib, fibTail, findMaximumProfit, gcd, lcm}

import org.scalatest._
import org.scalatest.matchers.should.Matchers

class AlgoTest extends FlatSpec with Matchers {

  "Bin search in array" should "find index" in {
    binarySearch(Array(0,1,3,1,4), 3) shouldBe 2
  }

  "Bin search in array" should "find first-in index" in {
    binarySearch(Array(0,1,3,1,4), 1) shouldBe 1
  }

  "Bin search non-existing val in array" should "return -1" in {
    binarySearch(Array(0,1,3,1,4), 90) shouldBe -1
  }

  "GCD" should "find Greatest Common Div" in {
    gcd(40, 80) shouldBe 40
  }

  "GCD" should "with 0" in {
    gcd(0, 80) shouldBe 80
  }

  "LCM" should "find least common mult" in {
    lcm(15, 150) shouldBe 150
  }

  "LCM" should "find least common mult 2" in {
    lcm(10, 15) shouldBe 30
  }

  "Fib" should "find by naive approach" in {
    fib(10) shouldBe 55
  }

  "Fib" should "find by naive approach 2" in {
    fib(0) shouldBe 0
  }

  "Fib" should "find by naive approach 3" in {
    fib(7) shouldBe 13
  }

  "Fibonacci" should "find by tail approach" in {
    fibTail(50) shouldBe 12586269025L
  }

  "Profit" should "find" in {
    findMaximumProfit(Array(163, 112, 105, 100, 151)) shouldBe 51
  }

  "Profit" should "contains -1" in {
    findMaximumProfit(Array(1)) shouldBe -1
  }

  "Profit" should "find max" in {
    findMaximumProfit(Array(101, 10, 151)) shouldBe 141
  }

}