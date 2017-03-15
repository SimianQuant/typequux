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
package typequuxTests

import typequux.Comparison
import typequux.Comparison._
import typequux.typequux._

/**
  * Specification for typelevel comparison
  */
class ComparisonSpec extends BaseSpec {

  type Rep[X <: Comparison] = X#Match[Int, String, Double, Any]

  it should "pass rep tests" in {
    assertCompiles { """eqv[Rep[LT], Int]""" }
    assertCompiles { """eqv[Rep[EQ], String]""" }
    assertCompiles { """eqv[Rep[GT], Double]""" }
    assertTypeError { """implicitly[Rep[LT] =:= String]""" }
    assertTypeError { """implicitly[Rep[EQ] =:= Int]""" }
    assertTypeError { """implicitly[Rep[GT] =:= Int]""" }
    assertTypeError { """implicitly[Rep[LT] =:= Double]""" }
    assertTypeError { """implicitly[Rep[EQ] =:= Double]""" }
    assertTypeError { """implicitly[Rep[GT] =:= String]""" }
  }

  it should "pass type-comparison tests" in {
    assertCompiles { """isTrue[LT#lt]""" }
    assertCompiles { """isTrue[LT#le]""" }
    assertCompiles { """isFalse[LT#eq]""" }
    assertCompiles { """isFalse[LT#gt]""" }
    assertCompiles { """isFalse[LT#ge]""" }

    assertCompiles { """isFalse[EQ#lt]""" }
    assertCompiles { """isTrue[EQ#le]""" }
    assertCompiles { """isTrue[EQ#eq]""" }
    assertCompiles { """isTrue[EQ#ge]""" }
    assertCompiles { """isFalse[EQ#gt]""" }

    assertCompiles { """isFalse[GT#lt]""" }
    assertCompiles { """isFalse[GT#le]""" }
    assertCompiles { """isFalse[GT#eq]""" }
    assertCompiles { """isTrue[GT#gt]""" }
    assertCompiles { """isTrue[GT#ge]""" }

  }

  "A comparison type" should "have the correct show implementation" in {
    assert(show[EQ] == "eq")
    assert(show[LT] == "lt")
    assert(show[GT] == "gt")
  }
}
