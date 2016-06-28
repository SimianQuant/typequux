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
import typequux._

class StringIndexedCollectionSpec extends BaseSpec {

  val si1 = SINil.add("pi", 3.14).add("e", 2.718).add("h", 6.626).add("phi", 1.618)
  val si2 = si1.add("k", 1.38)
  val si3 = si1.updated("pi", 3.14159).updated("h", 6.62607)

  val m1 = Map("pi" -> 3.14, "e" -> 2.718, "h" -> 6.626, "phi" -> 1.618)
  val m2 = Map("pi" -> 3.14, "e" -> 2.718, "h" -> 6.626, "phi" -> 1.618, "k" -> 1.38)
  val m3 = Map("pi" -> 3.14159, "e" -> 2.718, "h" -> 6.62607, "phi" -> 1.618)

  "A string indexed collection" should "apply correctly" in {
    assert(si1("pi") == 3.14)
    assert(si1("e") == 2.718)
    assert(si1("h") == 6.626)
    assert(si1("phi") == 1.618)
    illTyped { """si("x")""" }
  }

  it should "add correctly" in {
    assert(si2("pi") == 3.14)
    assert(si2("e") == 2.718)
    assert(si2("h") == 6.626)
    assert(si2("phi") == 1.618)
    assert(si2("k") == 1.38)
    illTyped { """si1.add("pi", 42)""" }
  }

  it should "update correctly" in {
    illTyped { """si1.updated("k", 1.3806)""" }
    assert(si3("pi") == 3.14159)
    assert(si3("e") == 2.718)
    assert(si3("h") == 6.62607)
    assert(si3("phi") == 1.618)
  }

  it should "have the correct size" in {
    assert(si1.size == 4)
    assert(si2.size == 5)
    assert(si3.size == 4)
    assert(SINil.size == 0)
  }

  it should "convert to maps properly" in {
    assert(si1.toMap == m1)
    assert(si2.toMap == m2)
    assert(si3.toMap == m3)
    assert(SINil.toMap == Map.empty[String, Nothing])
  }

  it should "have the proper hashcode" in {
    assert(si1.## == m1.##)
    assert(si2.## == m2.##)
    assert(si3.## == m3.##)
  }

  it should "have correct equals implementation" in {
    val sit1 = SINil.add("pi", 3.14).add("e", 2.718).add("h", 6.626).add("phi", 1.618)
    val sit2 = SINil.add("pi", 3.14).add("e", 2.718).add("h", 6.626).add("phi", 1.618).add("k", 1.38)
    val sit3 = SINil.add("pi", 3.14159).add("e", 2.718).add("h", 6.62607).add("phi", 1.618)

    assert(si1 == si1)
    assert(si2 == si2)
    assert(si3 == si3)
    assert(si1 != si2)
    assert(si2 != si3)
    assert(si3 != si1)

    assert(si1 == sit1)
    assert(si2 == sit2)
    assert(si3 == sit3)

    assert(si1 != si1.##)
  }

  it should "convert to string properly" in {
    assert(si1.toString == "StringIndexedCollection(pi -> 3.14, e -> 2.718, h -> 6.626, phi -> 1.618)")
  }
}
