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
package typequuxTests

import typequux.{Bool, Dense, DenseDiff}
import typequux.typequux._

/**
  * Specification for Dense numbers
  */
class DenseSpec extends BaseSpec {

  import Dense._

  type Rep[D <: Digit] = D#Match[Int, String, Any]

  it should "pass rep tests" in {
    assertCompiles { """implicitly[Rep[D0] =:= String]""" }
    assertCompiles { """implicitly[Rep[D1] =:= Int]""" }
    assertTypeError { """implicitly[Rep[D0] =:= Int]""" }
    assertTypeError { """implicitly[Rep[D1] =:= String]""" }
  }

  it should "pass comparison tests" in {
    assertCompiles { """implicitly[D0#Compare[D0] =:= EQ]""" }
    assertCompiles { """implicitly[D0#Compare[D1] =:= LT]""" }
    assertCompiles { """implicitly[D1#Compare[D0] =:= GT]""" }
    assertCompiles { """implicitly[D1#Compare[D1] =:= EQ]""" }

    assertCompiles { """isTrue[_0 === _0]""" }
    assertCompiles { """isTrue[_1 === _1]""" }
    assertCompiles { """isTrue[_3 === _3]""" }
    assertCompiles { """isTrue[_5 === _5]""" }
    assertCompiles { """isTrue[_9 === _9]""" }

    assertCompiles { """isTrue[_0 < _1]""" }
    assertCompiles { """isTrue[_1 > _0]""" }
    assertCompiles { """isTrue[_2 < _3]""" }
    assertCompiles { """isTrue[_3 > _2]""" }
    assertCompiles { """isTrue[_3 < _5]""" }
    assertCompiles { """isTrue[_6 > _3]""" }
    assertCompiles { """isTrue[_6 > _5]""" }
    assertCompiles { """isTrue[_4 < _6]""" }
    assertCompiles { """isTrue[_6 < _9]""" }
    assertCompiles { """isTrue[_9 > _7]""" }
    assertCompiles { """isTrue[_10 < _15]""" }
    assertCompiles { """isTrue[_11 <= _11]""" }
  }

  it should "pass increment tests" in {
    assertCompiles { """isTrue[_0#Inc === _1]""" }
    assertCompiles { """isTrue[_1#Inc === _2]""" }
    assertCompiles { """isTrue[_2#Inc === _3]""" }
    assertCompiles { """isTrue[_3#Inc === _4]""" }
    assertCompiles { """isTrue[_4#Inc === _5]""" }
    assertCompiles { """isTrue[_5#Inc === _6]""" }
    assertCompiles { """isTrue[_6#Inc === _7]""" }
    assertCompiles { """isTrue[_7#Inc === _8]""" }
    assertCompiles { """isTrue[_8#Inc === _9]""" }
    assertCompiles { """isTrue[_9#Inc === _10]""" }
    assertCompiles { """isTrue[_10#Inc === _11]""" }
    assertCompiles { """isTrue[_11#Inc === _12]""" }
    assertCompiles { """isTrue[_12#Inc === _13]""" }
    assertCompiles { """isTrue[_13#Inc === _14]""" }
    assertCompiles { """isTrue[_14#Inc === _15]""" }
  }

  it should "pass decrement tests" in {
    assertCompiles { """isTrue[_1#Dec === _0]""" }
    assertCompiles { """isTrue[_2#Dec === _1]""" }
    assertCompiles { """isTrue[_3#Dec === _2]""" }
    assertCompiles { """isTrue[_4#Dec === _3]""" }
    assertCompiles { """isTrue[_5#Dec === _4]""" }
    assertCompiles { """isTrue[_6#Dec === _5]""" }
    assertCompiles { """isTrue[_7#Dec === _6]""" }
    assertCompiles { """isTrue[_8#Dec === _7]""" }
    assertCompiles { """isTrue[_9#Dec === _8]""" }
    assertCompiles { """isTrue[_10#Dec === _9]""" }
    assertCompiles { """isTrue[_11#Dec === _10]""" }
    assertCompiles { """isTrue[_12#Dec === _11]""" }
    assertCompiles { """isTrue[_13#Dec === _12]""" }
    assertCompiles { """isTrue[_14#Dec === _13]""" }
    assertCompiles { """isTrue[_15#Dec === _14]""" }
  }

  it should "pass basic sum tests" in {
    assertCompiles { """isTrue[_0 + _0 === _0]""" }
    assertCompiles { """isTrue[_1 + _0 === _1]""" }
    assertCompiles { """isTrue[_0 + _1 === _1]""" }
    assertCompiles { """isTrue[_0 + _3 === _3]""" }
    assertCompiles { """isTrue[_3 + _0 === _3]""" }
    assertCompiles { """isTrue[_3 + _5 === _8]""" }
    assertCompiles { """isTrue[_5 + _3 === _8]""" }
    assertCompiles { """isTrue[_3 + _2 === _5]""" }
  }

  it should "pass basic product tests" in {
    assertCompiles { """isTrue[_0 * _0 === _0]""" }
    assertCompiles { """isTrue[_1 * _0 === _0]""" }
    assertCompiles { """isTrue[_0 * _1 === _0]""" }
    assertCompiles { """isTrue[_1 * _1 === _1]""" }
    assertCompiles { """isTrue[_1 * _9 === _9]""" }
    assertCompiles { """isTrue[_9 * _1 === _9]""" }
    assertCompiles { """isTrue[_2 * _2 === _4]""" }
    assertCompiles { """isTrue[_2 * _3 === _6]""" }
    assertCompiles { """isTrue[_3 * _2 === _6]""" }
    assertCompiles { """isTrue[_2 * _4 === _8]""" }
    assertCompiles { """isTrue[_4 * _2 === _8]""" }
    assertCompiles { """isTrue[_3 * _3 === _9]""" }
  }

  it should "pass exponent tests" in {
    assertCompiles { """isTrue[_0#YodaExp[_1] === _1]""" }
    assertCompiles { """isTrue[_1#YodaExp[_0] === _0]""" }
    assertCompiles { """isTrue[_0#YodaExp[_0] === _1]""" }
    assertCompiles { """isTrue[_1 ^ _1 === _1]""" }
    assertCompiles { """isTrue[_5 ^ _0 === _1]""" }
    assertCompiles { """isTrue[_5 ^ _1 === _5]""" }
    assertCompiles { """isTrue[_2 ^ _2 === _4]""" }
    assertCompiles { """isTrue[_2 ^ _3 === _8]""" }
    assertCompiles { """isTrue[_3 ^ _2 === _9]""" }
    assertCompiles { """isTrue[_3 ^ _3 === *[_9, _3]]""" }

    // some more tests
    assertCompiles { """isTrue[+[_1, *[_8, _10]] === ^[_9, _2]]""" }
    assertCompiles { """isTrue[*[_4#Sq, _4#Sq] === ^[_2, _8]]""" }
  }

  it should "pass subtraction tests" in {
    assertCompiles { """implicitly[DenseDiff[_10, _10, _0]]""" }
    assertCompiles { """implicitly[DenseDiff[_6, _2, _4]]""" }
    assertCompiles { """implicitly[DenseDiff[_17, _13, _4]]""" }
    assertTypeError { """DenseDiff[_7, _10, _3]""" }
  }

  /************** Property Based Tests *************************/
  // binary properties
  class AdditiveCommutativity[A, B]
  implicit def toAC[A <: Dense, B <: Dense](implicit ev: +[A, B] =:= +[B, A]): AdditiveCommutativity[A, B] =
    new AdditiveCommutativity[A, B]

  class MultiplicativeCommutativity[A, B]
  implicit def toMC[A <: Dense, B <: Dense](implicit ev: *[A, B] =:= *[B, A]): MultiplicativeCommutativity[A, B] =
    new MultiplicativeCommutativity[A, B]

  import Bool._

  class TotalOrderAntiSymmetry[A, B]
  implicit def toTOA[A <: Dense, B <: Dense](
      implicit ev: IsTrue[&&[A <= B, B <= A] ->> ===[A, B]]): TotalOrderAntiSymmetry[A, B] =
    new TotalOrderAntiSymmetry[A, B]

  class TotalOrderTotality[A, B]
  implicit def toTOT[A <: Dense, B <: Dense](implicit ev: IsTrue[||[A <= B, B <= A]]): TotalOrderTotality[A, B] =
    new TotalOrderTotality[A, B]

  // performance is considerable better, so can have more test cases
  def binaryLaws[A <: Dense, B <: Dense](implicit ev0: AdditiveCommutativity[A, B],
                                         ev1: MultiplicativeCommutativity[A, B],
                                         ev2: TotalOrderAntiSymmetry[A, B],
                                         ev3: TotalOrderTotality[A, B]) = true

  it should "pass binary laws" in {
    assertCompiles { """binaryLaws[_3, _9]""" }
    assertCompiles { """binaryLaws[_12, _14]""" }
    assertCompiles { """binaryLaws[_1, _3]""" }
    assertCompiles { """binaryLaws[_6, _11]""" }
    assertCompiles { """binaryLaws[_7, _12]""" }
    assertCompiles { """binaryLaws[_3, _15]""" }
    assertCompiles { """binaryLaws[_1, _4]""" }
    assertCompiles { """binaryLaws[_11, _12]""" }
    assertCompiles { """binaryLaws[_2, _4]""" }
    assertCompiles { """binaryLaws[_12, _15]""" }
    assertCompiles { """binaryLaws[_6, _10]""" }
    assertCompiles { """binaryLaws[_5, _5]""" }
    assertCompiles { """binaryLaws[_7, _14]""" }
    assertCompiles { """binaryLaws[_5, _10]""" }
    assertCompiles { """binaryLaws[_4, _5]""" }
    assertCompiles { """binaryLaws[_8, _15]""" }
    assertCompiles { """binaryLaws[_11, _14]""" }
    assertCompiles { """binaryLaws[_6, _15]""" }
    assertCompiles { """binaryLaws[_3, _12]""" }
    assertCompiles { """binaryLaws[_2, _13]""" }
    assertCompiles { """binaryLaws[_2, _2]""" }
    assertCompiles { """binaryLaws[_13, _15]""" }
    assertCompiles { """binaryLaws[_1, _7]""" }
    assertCompiles { """binaryLaws[_1, _1]""" }
    assertCompiles { """binaryLaws[_2, _6]""" }
    assertCompiles { """binaryLaws[_4, _12]""" }
    assertCompiles { """binaryLaws[_2, _3]""" }
    assertCompiles { """binaryLaws[_11, _11]""" }
    assertCompiles { """binaryLaws[_7, _13]""" }
    assertCompiles { """binaryLaws[_4, _10]""" }
    assertCompiles { """binaryLaws[_6, _9]""" }
    assertCompiles { """binaryLaws[_9, _9]""" }
    assertCompiles { """binaryLaws[_2, _14]""" }
    assertCompiles { """binaryLaws[_12, _12]""" }
    assertCompiles { """binaryLaws[_12, _13]""" }
    assertCompiles { """binaryLaws[_5, _11]""" }
    assertCompiles { """binaryLaws[_0, _0]""" }
    assertCompiles { """binaryLaws[_1, _12]""" }
    assertCompiles { """binaryLaws[_9, _13]""" }
    assertCompiles { """binaryLaws[_1, _10]""" }
    assertCompiles { """binaryLaws[_8, _14]""" }
    assertCompiles { """binaryLaws[_6, _12]""" }
    assertCompiles { """binaryLaws[_4, _11]""" }
    assertCompiles { """binaryLaws[_4, _8]""" }
    assertCompiles { """binaryLaws[_1, _2]""" }
    assertCompiles { """binaryLaws[_6, _8]""" }
    assertCompiles { """binaryLaws[_5, _15]""" }
    assertCompiles { """binaryLaws[_4, _15]""" }
    assertCompiles { """binaryLaws[_3, _7]""" }
    assertCompiles { """binaryLaws[_7, _9]""" }
    assertCompiles { """binaryLaws[_3, _10]""" }
    assertCompiles { """binaryLaws[_8, _13]""" }
    assertCompiles { """binaryLaws[_8, _9]""" }
    assertCompiles { """binaryLaws[_7, _15]""" }
    assertCompiles { """binaryLaws[_1, _9]""" }
    assertCompiles { """binaryLaws[_5, _12]""" }
    assertCompiles { """binaryLaws[_3, _11]""" }
    assertCompiles { """binaryLaws[_3, _5]""" }
    assertCompiles { """binaryLaws[_4, _4]""" }
    assertCompiles { """binaryLaws[_0, _7]""" }
    assertCompiles { """binaryLaws[_2, _12]""" }
    assertCompiles { """binaryLaws[_0, _12]""" }
    assertCompiles { """binaryLaws[_4, _14]""" }
    assertCompiles { """binaryLaws[_8, _10]""" }
    assertCompiles { """binaryLaws[_7, _8]""" }
    assertCompiles { """binaryLaws[_1, _6]""" }
    assertCompiles { """binaryLaws[_4, _6]""" }
    assertCompiles { """binaryLaws[_3, _14]""" }
    assertCompiles { """binaryLaws[_9, _11]""" }
    assertCompiles { """binaryLaws[_8, _11]""" }
    assertCompiles { """binaryLaws[_10, _10]""" }
    assertCompiles { """binaryLaws[_11, _15]""" }
    assertCompiles { """binaryLaws[_2, _7]""" }
    assertCompiles { """binaryLaws[_2, _10]""" }
    assertCompiles { """binaryLaws[_7, _7]""" }
    assertCompiles { """binaryLaws[_1, _5]""" }
    assertCompiles { """binaryLaws[_0, _5]""" }
    assertCompiles { """binaryLaws[_10, _12]""" }
    assertCompiles { """binaryLaws[_0, _8]""" }
    assertCompiles { """binaryLaws[_0, _13]""" }
    assertCompiles { """binaryLaws[_13, _14]""" }
    assertCompiles { """binaryLaws[_14, _14]""" }
    assertCompiles { """binaryLaws[_6, _6]""" }
    assertCompiles { """binaryLaws[_1, _15]""" }
    assertCompiles { """binaryLaws[_7, _11]""" }
    assertCompiles { """binaryLaws[_10, _15]""" }
    assertCompiles { """binaryLaws[_4, _9]""" }
    assertCompiles { """binaryLaws[_15, _15]""" }
    assertCompiles { """binaryLaws[_0, _11]""" }
    assertCompiles { """binaryLaws[_8, _12]""" }
    assertCompiles { """binaryLaws[_4, _13]""" }
    assertCompiles { """binaryLaws[_7, _10]""" }
    assertCompiles { """binaryLaws[_0, _14]""" }
    assertCompiles { """binaryLaws[_6, _13]""" }
    assertCompiles { """binaryLaws[_10, _11]""" }
    assertCompiles { """binaryLaws[_11, _13]""" }
    assertCompiles { """binaryLaws[_2, _11]""" }
    assertCompiles { """binaryLaws[_2, _5]""" }
    assertCompiles { """binaryLaws[_1, _13]""" }
    assertCompiles { """binaryLaws[_0, _10]""" }
  }

  // ternary laws

  class AdditiveAssociativity[A, B, C]
  implicit def toAA[A <: Dense, B <: Dense, C <: Dense](
      implicit ev: +[A, +[B, C]] =:= +[+[A, B], C]): AdditiveAssociativity[A, B, C] =
    new AdditiveAssociativity[A, B, C]

  class MultiplicativeAssociativity[A, B, C]
  implicit def toMA[A <: Dense, B <: Dense, C <: Dense](
      implicit ev: *[A, *[B, C]] =:= *[*[A, B], C]): MultiplicativeAssociativity[A, B, C] =
    new MultiplicativeAssociativity[A, B, C]

  class Distributivity[A, B, C]
  implicit def toDist[A <: Dense, B <: Dense, C <: Dense](
      implicit ev: *[+[A, B], C] =:= +[*[A, C], *[B, C]]): Distributivity[A, B, C] =
    new Distributivity[A, B, C]

  class TotalOrderTransitivity[A, B, C]
  implicit def toToTr[A <: Dense, B <: Dense, C <: Dense](
      implicit ev: IsTrue[&&[A <= B, B <= C] ->> <=[A, C]]): TotalOrderTransitivity[A, B, C] =
    new TotalOrderTransitivity[A, B, C]

  def ternaryLaws[A <: Dense, B <: Dense, C <: Dense](implicit ev0: AdditiveAssociativity[A, B, C],
                                                      ev1: MultiplicativeAssociativity[A, B, C],
                                                      ev2: Distributivity[A, B, C],
                                                      ev3: TotalOrderTransitivity[A, B, C]) = true

  it should "pass ternary laws" in {
    assertCompiles { """ternaryLaws[_1, _3, _6]""" }
    assertCompiles { """ternaryLaws[_0, _2, _13]""" }
    assertCompiles { """ternaryLaws[_3, _14, _15]""" }
    assertCompiles { """ternaryLaws[_5, _8, _8]""" }
    assertCompiles { """ternaryLaws[_2, _11, _13]""" }
    assertCompiles { """ternaryLaws[_0, _2, _8]""" }
    assertCompiles { """ternaryLaws[_6, _14, _14]""" }
    assertCompiles { """ternaryLaws[_2, _2, _15]""" }
    assertCompiles { """ternaryLaws[_10, _11, _11]""" }
    assertCompiles { """ternaryLaws[_0, _0, _0]""" }
    assertCompiles { """ternaryLaws[_0, _0, _10]""" }
    assertCompiles { """ternaryLaws[_5, _6, _15]""" }
    assertCompiles { """ternaryLaws[_1, _5, _13]""" }
    assertCompiles { """ternaryLaws[_3, _4, _10]""" }
    assertCompiles { """ternaryLaws[_2, _5, _10]""" }
    assertCompiles { """ternaryLaws[_2, _3, _3]""" }
    assertCompiles { """ternaryLaws[_4, _13, _15]""" }
    assertCompiles { """ternaryLaws[_4, _8, _11]""" }
    assertCompiles { """ternaryLaws[_0, _7, _13]""" }
    assertCompiles { """ternaryLaws[_3, _4, _14]""" }
    assertCompiles { """ternaryLaws[_4, _10, _15]""" }
    assertCompiles { """ternaryLaws[_9, _9, _12]""" }
    assertCompiles { """ternaryLaws[_4, _11, _12]""" }
    assertCompiles { """ternaryLaws[_2, _11, _11]""" }
    assertCompiles { """ternaryLaws[_0, _12, _15]""" }
    assertCompiles { """ternaryLaws[_3, _3, _13]""" }
    assertCompiles { """ternaryLaws[_8, _9, _11]""" }
    assertCompiles { """ternaryLaws[_8, _8, _12]""" }
    assertCompiles { """ternaryLaws[_1, _9, _15]""" }
    assertCompiles { """ternaryLaws[_3, _6, _12]""" }
    assertCompiles { """ternaryLaws[_9, _9, _10]""" }
    assertCompiles { """ternaryLaws[_1, _8, _8]""" }
    assertCompiles { """ternaryLaws[_2, _3, _12]""" }
    assertCompiles { """ternaryLaws[_1, _6, _15]""" }
    assertCompiles { """ternaryLaws[_1, _8, _12]""" }
    assertCompiles { """ternaryLaws[_2, _6, _15]""" }
    assertCompiles { """ternaryLaws[_2, _4, _6]""" }
    assertCompiles { """ternaryLaws[_1, _1, _1]""" }
    assertCompiles { """ternaryLaws[_4, _8, _15]""" }
    assertCompiles { """ternaryLaws[_8, _10, _10]""" }
    assertCompiles { """ternaryLaws[_7, _7, _7]""" }
    assertCompiles { """ternaryLaws[_8, _8, _10]""" }
    assertCompiles { """ternaryLaws[_2, _2, _14]""" }
    assertCompiles { """ternaryLaws[_6, _6, _7]""" }
    assertCompiles { """ternaryLaws[_7, _8, _10]""" }
    assertCompiles { """ternaryLaws[_3, _5, _14]""" }
    assertCompiles { """ternaryLaws[_2, _10, _13]""" }
    assertCompiles { """ternaryLaws[_2, _3, _10]""" }
    assertCompiles { """ternaryLaws[_0, _0, _1]""" }
    assertCompiles { """ternaryLaws[_4, _4, _8]""" }
    assertCompiles { """ternaryLaws[_2, _13, _13]""" }
    assertCompiles { """ternaryLaws[_10, _14, _15]""" }
    assertCompiles { """ternaryLaws[_3, _13, _15]""" }
    assertCompiles { """ternaryLaws[_7, _12, _14]""" }
    assertCompiles { """ternaryLaws[_6, _7, _14]""" }
    assertCompiles { """ternaryLaws[_6, _6, _10]""" }
    assertCompiles { """ternaryLaws[_2, _8, _12]""" }
    assertCompiles { """ternaryLaws[_2, _13, _15]""" }
    assertCompiles { """ternaryLaws[_2, _2, _8]""" }
    assertCompiles { """ternaryLaws[_0, _12, _13]""" }
    assertCompiles { """ternaryLaws[_4, _5, _8]""" }
    assertCompiles { """ternaryLaws[_7, _8, _8]""" }
    assertCompiles { """ternaryLaws[_4, _5, _13]""" }
    assertCompiles { """ternaryLaws[_0, _5, _15]""" }
    assertCompiles { """ternaryLaws[_2, _13, _14]""" }
    assertCompiles { """ternaryLaws[_0, _5, _12]""" }
    assertCompiles { """ternaryLaws[_2, _2, _9]""" }
    assertCompiles { """ternaryLaws[_3, _6, _8]""" }
    assertCompiles { """ternaryLaws[_0, _6, _9]""" }
    assertCompiles { """ternaryLaws[_5, _5, _6]""" }
    assertCompiles { """ternaryLaws[_2, _5, _11]""" }
    assertCompiles { """ternaryLaws[_0, _3, _14]""" }
    assertCompiles { """ternaryLaws[_1, _3, _3]""" }
    assertCompiles { """ternaryLaws[_4, _8, _14]""" }
    assertCompiles { """ternaryLaws[_1, _2, _6]""" }
    assertCompiles { """ternaryLaws[_9, _10, _10]""" }
    assertCompiles { """ternaryLaws[_1, _2, _2]""" }
    assertCompiles { """ternaryLaws[_1, _10, _14]""" }
    assertCompiles { """ternaryLaws[_4, _9, _15]""" }
    assertCompiles { """ternaryLaws[_0, _4, _6]""" }
    assertCompiles { """ternaryLaws[_3, _11, _13]""" }
    assertCompiles { """ternaryLaws[_5, _7, _12]""" }
    assertCompiles { """ternaryLaws[_4, _13, _14]""" }
    assertCompiles { """ternaryLaws[_3, _10, _13]""" }
    assertCompiles { """ternaryLaws[_0, _2, _2]""" }
    assertCompiles { """ternaryLaws[_5, _8, _9]""" }
    assertCompiles { """ternaryLaws[_4, _6, _14]""" }
    assertCompiles { """ternaryLaws[_1, _8, _10]""" }
    assertCompiles { """ternaryLaws[_1, _5, _8]""" }
    assertCompiles { """ternaryLaws[_6, _11, _15]""" }
    assertCompiles { """ternaryLaws[_1, _1, _6]""" }
    assertCompiles { """ternaryLaws[_6, _10, _13]""" }
    assertCompiles { """ternaryLaws[_4, _11, _14]""" }
    assertCompiles { """ternaryLaws[_5, _11, _11]""" }
    assertCompiles { """ternaryLaws[_3, _4, _11]""" }
    assertCompiles { """ternaryLaws[_0, _5, _14]""" }
    assertCompiles { """ternaryLaws[_4, _4, _15]""" }
    assertCompiles { """ternaryLaws[_2, _2, _12]""" }
    assertCompiles { """ternaryLaws[_9, _11, _12]""" }
    assertCompiles { """ternaryLaws[_0, _4, _15]""" }
  }

  class ExponentCombine0[A, B, C]
  implicit def toExpCmb0[A <: Dense, B <: Dense, C <: Dense](
      implicit ev: *[^[A, B], ^[A, C]] =:= ^[A, +[B, C]]): ExponentCombine0[A, B, C] =
    new ExponentCombine0[A, B, C]

  class ExponentCombine1[A, B, C]
  implicit def toExpCmb1[A <: Dense, B <: Dense, C <: Dense](
      implicit ev: ^[^[A, B], C] =:= ^[A, *[B, C]]): ExponentCombine1[A, B, C] =
    new ExponentCombine1[A, B, C]

  class ExponentCombine2[A, B, C]
  implicit def toExpCmb2[A <: Dense, B <: Dense, C <: Dense](
      implicit ev: ^[*[A, B], C] =:= *[^[A, C], ^[B, C]]): ExponentCombine2[A, B, C] =
    new ExponentCombine2[A, B, C]

  def ternaryExpLaws[A <: Dense, B <: Dense, C <: Dense](
      implicit ev0: ExponentCombine0[A, B, C],
      ev1: ExponentCombine1[A, B, C],
      ev2: ExponentCombine2[A, B, C]
  ) = true

  it should "pass ternary exponent laws" in {
    assertCompiles { """ternaryExpLaws[_0, _0, _0]""" }
    assertCompiles { """ternaryExpLaws[_0, _0, _1]""" }
    assertCompiles { """ternaryExpLaws[_0, _0, _2]""" }
    assertCompiles { """ternaryExpLaws[_0, _0, _3]""" }
    assertCompiles { """ternaryExpLaws[_0, _1, _1]""" }
    assertCompiles { """ternaryExpLaws[_0, _1, _2]""" }
    assertCompiles { """ternaryExpLaws[_0, _1, _3]""" }
    assertCompiles { """ternaryExpLaws[_0, _2, _2]""" }
    assertCompiles { """ternaryExpLaws[_0, _2, _3]""" }
    assertCompiles { """ternaryExpLaws[_0, _3, _3]""" }
    assertCompiles { """ternaryExpLaws[_1, _0, _0]""" }
    assertCompiles { """ternaryExpLaws[_1, _0, _1]""" }
    assertCompiles { """ternaryExpLaws[_1, _0, _2]""" }
    assertCompiles { """ternaryExpLaws[_1, _0, _3]""" }
    assertCompiles { """ternaryExpLaws[_1, _1, _1]""" }
    assertCompiles { """ternaryExpLaws[_1, _1, _2]""" }
    assertCompiles { """ternaryExpLaws[_1, _1, _3]""" }
    assertCompiles { """ternaryExpLaws[_1, _2, _2]""" }
    assertCompiles { """ternaryExpLaws[_1, _2, _3]""" }
    assertCompiles { """ternaryExpLaws[_1, _3, _3]""" }
    assertCompiles { """ternaryExpLaws[_2, _0, _0]""" }
    assertCompiles { """ternaryExpLaws[_2, _0, _1]""" }
    assertCompiles { """ternaryExpLaws[_2, _0, _2]""" }
    assertCompiles { """ternaryExpLaws[_2, _0, _3]""" }
    assertCompiles { """ternaryExpLaws[_2, _1, _1]""" }
    assertCompiles { """ternaryExpLaws[_2, _1, _2]""" }
    assertCompiles { """ternaryExpLaws[_2, _1, _3]""" }
    assertCompiles { """ternaryExpLaws[_2, _2, _2]""" }
    assertCompiles { """ternaryExpLaws[_2, _2, _3]""" }
    assertCompiles { """ternaryExpLaws[_2, _3, _3]""" }
    assertCompiles { """ternaryExpLaws[_3, _0, _0]""" }
    assertCompiles { """ternaryExpLaws[_3, _0, _1]""" }
    assertCompiles { """ternaryExpLaws[_3, _0, _2]""" }
    assertCompiles { """ternaryExpLaws[_3, _0, _3]""" }
    assertCompiles { """ternaryExpLaws[_3, _1, _1]""" }
    assertCompiles { """ternaryExpLaws[_3, _1, _2]""" }
    assertCompiles { """ternaryExpLaws[_3, _1, _3]""" }
    assertCompiles { """ternaryExpLaws[_3, _2, _2]""" }
    assertCompiles { """ternaryExpLaws[_3, _2, _3]""" }
    assertCompiles { """ternaryExpLaws[_3, _3, _3]""" }
    assertCompiles { """ternaryExpLaws[_4, _0, _0]""" }
    assertCompiles { """ternaryExpLaws[_4, _0, _1]""" }
    assertCompiles { """ternaryExpLaws[_4, _0, _2]""" }
    assertCompiles { """ternaryExpLaws[_4, _0, _3]""" }
    assertCompiles { """ternaryExpLaws[_4, _1, _1]""" }
    assertCompiles { """ternaryExpLaws[_4, _1, _2]""" }
    assertCompiles { """ternaryExpLaws[_4, _1, _3]""" }
    assertCompiles { """ternaryExpLaws[_4, _2, _2]""" }
    assertCompiles { """ternaryExpLaws[_4, _2, _3]""" }
    assertCompiles { """ternaryExpLaws[_4, _3, _3]""" }
    assertCompiles { """ternaryExpLaws[_5, _0, _0]""" }
    assertCompiles { """ternaryExpLaws[_5, _0, _1]""" }
    assertCompiles { """ternaryExpLaws[_5, _0, _2]""" }
    assertCompiles { """ternaryExpLaws[_5, _0, _3]""" }
    assertCompiles { """ternaryExpLaws[_5, _1, _1]""" }
    assertCompiles { """ternaryExpLaws[_5, _1, _2]""" }
    assertCompiles { """ternaryExpLaws[_5, _1, _3]""" }
    assertCompiles { """ternaryExpLaws[_5, _2, _2]""" }
    assertCompiles { """ternaryExpLaws[_5, _2, _3]""" }
    assertCompiles { """ternaryExpLaws[_5, _3, _3]""" }
  }

  // unary laws

  class AdditiveIdentity[A]
  implicit def toAdditiveIdentity[A <: Dense](implicit ev0: +[A, _0] =:= A, ev1: +[_0, A] =:= A): AdditiveIdentity[A] =
    new AdditiveIdentity[A]

  class MultiplicativeIdentity[A]
  implicit def toMI[A <: Dense](implicit ev0: *[A, _1] =:= A, ev1: *[A, _1] =:= A): MultiplicativeIdentity[A] =
    new MultiplicativeIdentity[A]

  class DecrementOfIncrement[A]
  implicit def toDecrementOfIncrement[A <: Dense](implicit ev: A#Inc#Dec =:= A): DecrementOfIncrement[A] =
    new DecrementOfIncrement[A]

  class RightShiftOfLeftShift[A]
  implicit def toRightShiftOfLeftShift[A <: Dense](implicit ev: A#ShiftL#ShiftR =:= A): RightShiftOfLeftShift[A] =
    new RightShiftOfLeftShift[A]

  class ExpZero[A]
  implicit def toExpZero[A <: Dense](implicit ev: ^[A, _0] =:= _1): ExpZero[A] = new ExpZero[A]

  class ExpIdentity[A]
  implicit def toExpIdentity[A <: Dense](implicit ev: ^[A, _1] =:= A): ExpIdentity[A] = new ExpIdentity[A]

  class LeftShift[A]
  implicit def toLeftShift[A <: Dense](implicit ev: A#ShiftL =:= *[A, _2]): LeftShift[A] = new LeftShift[A]

  class RightShift[A]
  implicit def toRs[A <: Dense](
      implicit ev: Xor[===[A, *[_2, A#ShiftR]], ===[A, *[_2, A#ShiftR] + _1]] =:= True): RightShift[A] =
    new RightShift[A]

  class ExpOne[A]
  implicit def toExpOne[A <: Dense](implicit ev: ^[_1, A] =:= _1): ExpOne[A] = new ExpOne[A]

  def unaryLaws[A <: Dense](implicit ev0: AdditiveIdentity[A],
                            ev1: MultiplicativeIdentity[A],
                            ev2: DecrementOfIncrement[A],
                            ev3: RightShiftOfLeftShift[A],
                            ev4: ExpZero[A],
                            ev5: ExpIdentity[A],
                            ev6: LeftShift[A],
                            ev7: RightShift[A]) = true

  it should "pass unary laws" in {
    assertCompiles { """unaryLaws[_0]""" }
    assertCompiles { """unaryLaws[_1]""" }
    assertCompiles { """unaryLaws[_2]""" }
    assertCompiles { """unaryLaws[_3]""" }
    assertCompiles { """unaryLaws[_4]""" }
    assertCompiles { """unaryLaws[_5]""" }
    assertCompiles { """unaryLaws[_6]""" }
    assertCompiles { """unaryLaws[_7]""" }
    assertCompiles { """unaryLaws[_8]""" }
    assertCompiles { """unaryLaws[_9]""" }
    assertCompiles { """unaryLaws[_10]""" }
    assertCompiles { """unaryLaws[_11]""" }
    assertCompiles { """unaryLaws[_12]""" }
    assertCompiles { """unaryLaws[_13]""" }
    assertCompiles { """unaryLaws[_14]""" }
    assertCompiles { """unaryLaws[_15]""" }
    assertCompiles { """unaryLaws[_16]""" }
    assertCompiles { """unaryLaws[_17]""" }
    assertCompiles { """unaryLaws[_18]""" }
    assertCompiles { """unaryLaws[_19]""" }
    assertCompiles { """unaryLaws[_20]""" }
    assertCompiles { """unaryLaws[_21]""" }
    assertCompiles { """unaryLaws[_22]""" }
  }

  "A dense number type" should "evaluate to long correctly" in {
    assert(toLong[_0] == 0l)
    assert(toLong[_1] == 1l)
    assert(toLong[_2] == 2l)
    assert(toLong[_3] == 3l)
    assert(toLong[_4] == 4l)
    assert(toLong[_5] == 5l)
    assert(toLong[_6] == 6l)
    assert(toLong[_7] == 7l)
    assert(toLong[_8] == 8l)
    assert(toLong[_9] == 9l)
    assert(toLong[_10] == 10l)
    assert(toLong[_11] == 11l)
    assert(toLong[_12] == 12l)
    assert(toLong[_13] == 13l)
    assert(toLong[_14] == 14l)
    assert(toLong[_15] == 15l)
    assert(toLong[_16] == 16l)
    assert(toLong[_17] == 17l)
    assert(toLong[_18] == 18l)
    assert(toLong[_19] == 19l)
    assert(toLong[_20] == 20l)
    assert(toLong[_21] == 21l)
    assert(toLong[_22] == 22l)
  }

  "A dense number type" should "evaluate to int correctly" in {
    assert(toInt[_0] == 0)
    assert(toInt[_1] == 1)
    assert(toInt[_2] == 2)
    assert(toInt[_3] == 3)
    assert(toInt[_4] == 4)
    assert(toInt[_5] == 5)
    assert(toInt[_6] == 6)
    assert(toInt[_7] == 7)
    assert(toInt[_8] == 8)
    assert(toInt[_9] == 9)
    assert(toInt[_10] == 10)
    assert(toInt[_11] == 11)
    assert(toInt[_12] == 12)
    assert(toInt[_13] == 13)
    assert(toInt[_14] == 14)
    assert(toInt[_15] == 15)
    assert(toInt[_16] == 16)
    assert(toInt[_17] == 17)
    assert(toInt[_18] == 18)
    assert(toInt[_19] == 19)
    assert(toInt[_20] == 20)
    assert(toInt[_21] == 21)
    assert(toInt[_22] == 22)
  }

}
