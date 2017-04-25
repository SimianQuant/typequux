/**
  * Copyright 2017 Harshad Deo
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package typequuxtests

import typequux.Record
import typequux.Typequux.RNil

class RecordSpec extends BaseSpec {

  val r1 = RNil.add("a", List(1, 2, 3)).add("p", 42L).add("v", "unicorn")
  val r2 = r1.add("x", Some("one wants you"))
  val r3 = r1.updated("a", Some(List(1, 2, 3)))

  it should "apply properly" in {
    assertTypeError { """r1("o")""" }

    val r1a: List[Int] = r1("a")
    val r1b: Long = r1("p")
    val r1c: String = r1("v")

    assert(r1a == List(1, 2, 3))
    assert(r1b == 42L)
    assert(r1c == "unicorn")
  }

  it should "add correctly" in {
    assertTypeError { """r1.add("a", None)""" }

    val r2a: List[Int] = r2("a")
    val r2b: Long = r2("p")
    val r2c: String = r2("v")

    assert(r2a == List(1, 2, 3))
    assert(r2b == 42L)
    assert(r2c == "unicorn")
  }

  it should "update correctly" in {
    assertTypeError { """r1.updated("vw", "golf")""" }

    val r3a: Option[List[Int]] = r3("a")
    val r3b: Long = r3("p")
    val r3c: String = r3("v")

    assert(r3a == Some(List(1, 2, 3)))
    assert(r3b == 42L)
    assert(r3c == "unicorn")
  }

  it should "have the correct size" in {
    assert(r1.size == 3)
    assert(r2.size == 4)
    assert(r3.size == 3)
  }

  it should "have the correct hashcode" in {
    val h1: Map[String, Any] = Map("v" -> "unicorn", "p" -> 42L, "a" -> List(1, 2, 3))
    val h2: Map[String, Any] = Map("x" -> Some("one wants you"), "v" -> "unicorn", "p" -> 42L, "a" -> List(1, 2, 3))
    val h3: Map[String, Any] = Map("v" -> "unicorn", "p" -> 42L, "a" -> Some(List(1, 2, 3)))

    assert(r1.## == h1.##)
    assert(r2.## == h2.##)
    assert(r3.## == h3.##)
  }

  it should "have a correct equals implementation" in {
    assert(r1 == r1)
    assert(r2 == r2)
    assert(r3 == r3)

    assert(r1 != r2)
    assert(r2 != r3)
    assert(r3 != r1)

    val r1t = RNil.add("a", List(1, 2, 3)).add("p", 42L).add("v", "unicorn")
    val r2t = RNil.add("a", List(1, 2, 3)).add("p", 42L).add("v", "unicorn").add("x", Some("one wants you"))
    val r3t = RNil.add("a", Some(List(1, 2, 3))).add("p", 42L).add("v", "unicorn")

    assert(r1 == r1t)
    assert(r2 == r2t)
    assert(r3 == r3t)

    assert(r1 != r1.##)
  }

  it should "be obtainable from a class" in {
    class Demo1(a: Int) {
      val b = a.toString
    }

    class Demo2(val a: String, b: Int) {
      def c: Int = 42
    }

    case class Demo3(a: String, b: Long)

    val r1 = Record.class2Record(new Demo1(42))
    val r2 = Record.class2Record(new Demo2("me", 42))
    val r3 = Record.class2Record(Demo3("oogachaka", 42L))

    assertTypeError { """r1("a")""" }
    assertTypeError { """r2("b")""" }
    assertTypeError { """r2("c")""" }

    assert(r1("b") == "42")
    assert(r2("a") == "me")
    assert(r3("a") == "oogachaka")
    assert(r3("b") == 42L)
  }

  it should "convert to map properly" in {
    val m1: Map[String, Any] = Map("a" -> List(1, 2, 3), "p" -> 42L, "v" -> "unicorn")
    val m2: Map[String, Any] = Map("a" -> List(1, 2, 3), "p" -> 42L, "v" -> "unicorn", "x" -> Some("one wants you"))
    val m3: Map[String, Any] = Map("a" -> Some(List(1, 2, 3)), "p" -> 42L, "v" -> "unicorn")

    assert(r1.toMap == m1)
    assert(r2.toMap == m2)
    assert(r3.toMap == m3)
    assert(RNil.toMap == Map.empty[String, Nothing])
  }
}
