/**
  * Copyright 2016 Harshad Deo
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
package typequux

import shapeless.test.illTyped

class RecordSpec extends BaseSpec {

  val r1 = RNil.add("a", List(1, 2, 3)).add("p", 42L).add("v", "unicorn")
  val r2 = r1.add("x", Some("one wants you"))
  val r3 = r1.updated("a", Some(List(1, 2, 3)))

  "A record" should "apply properly" in {
    illTyped { """r1("o")""" }

    val r1a: List[Int] = r1("a")
    val r1b: Long = r1("p")
    val r1c: String = r1("v")

    assert(r1a == List(1, 2, 3))
    assert(r1b == 42L)
    assert(r1c == "unicorn")
  }

  it should "add correctly" in {
    illTyped { """r1.add("a", None)""" }

    val r2a: List[Int] = r2("a")
    val r2b: Long = r2("p")
    val r2c: String = r2("v")

    assert(r2a == List(1, 2, 3))
    assert(r2b == 42L)
    assert(r2c == "unicorn")
  }

  it should "update correctly" in {
    illTyped { """r1.updated("vw", "golf")""" }

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
    val h1 = "unicorn" :+: 42L :+: List(1, 2, 3) :+: HNil
    val h2 = Some("one wants you") :+: "unicorn" :+: 42L :+: List(1, 2, 3) :+: HNil
    val h3 = "unicorn" :+: 42L :+: Some(List(1, 2, 3)) :+: HNil

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

    import Record._

    val r1 = class2Record(new Demo1(42))
    val r2 = class2Record(new Demo2("me", 42))
    val r3 = class2Record(Demo3("oogachaka", 42L))

    illTyped { """r1("a")""" }
    illTyped { """r2("b")""" }
    illTyped { """r2("c")""" }

    assert(r1("b") == "42")
    assert(r2("a") == "me")
    assert(r3("a") == "oogachaka")
    assert(r3("b") == 42L)
  }
}
