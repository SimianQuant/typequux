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

import Comparison._
import shapeless.test.illTyped

/**
  * Specification for typelevel comparison
  */
class ComparisonSpec extends BaseSpec {

  type Rep[X <: Comparison] = X#Match[Int, String, Double, Any]

  eqv[Rep[LT], Int]
  eqv[Rep[EQ], String]
  eqv[Rep[GT], Double]

  illTyped { """implicitly[Rep[LT] =:= String]""" }
  illTyped { """implicitly[Rep[EQ] =:= Int]""" }
  illTyped { """implicitly[Rep[GT] =:= Int]""" }
  illTyped { """implicitly[Rep[LT] =:= Double]""" }
  illTyped { """implicitly[Rep[EQ] =:= Double]""" }
  illTyped { """implicitly[Rep[GT] =:= String]""" }

  isTrue[LT#lt]
  isTrue[LT#le]
  isFalse[LT#eq]
  isFalse[LT#gt]
  isFalse[LT#ge]

  isFalse[EQ#lt]
  isTrue[EQ#le]
  isTrue[EQ#eq]
  isTrue[EQ#ge]
  isFalse[EQ#gt]

  isFalse[GT#lt]
  isFalse[GT#le]
  isFalse[GT#eq]
  isTrue[GT#gt]
  isTrue[GT#ge]

  "A comparison type" should "have the correct show implementation" in {
    assert(show[EQ] == "eq")
    assert(show[LT] == "lt")
    assert(show[GT] == "gt")
  }
}
