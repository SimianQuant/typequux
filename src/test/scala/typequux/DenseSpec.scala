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

/**
  * Specification for Dense numbers
  */
class DenseSpec extends BaseSpec {

  import Dense._

  /*********************** Unit Tests *****************************/
  type Rep[D <: Digit] = D#Match[Int, String, Any]
  implicitly[Rep[D0] =:= String]
  implicitly[Rep[D1] =:= Int]
  illTyped { """implicitly[Rep[D0] =:= Int]""" }
  illTyped { """implicitly[Rep[D1] =:= String]""" }

  implicitly[D0#Compare[D0] =:= EQ]
  implicitly[D0#Compare[D1] =:= LT]
  implicitly[D1#Compare[D0] =:= GT]
  implicitly[D1#Compare[D1] =:= EQ]

  isTrue[_0 === _0]
  isTrue[_1 === _1]
  isTrue[_3 === _3]
  isTrue[_5 === _5]
  isTrue[_9 === _9]

  isTrue[_0 < _1]
  isTrue[_1 > _0]
  isTrue[_2 < _3]
  isTrue[_3 > _2]
  isTrue[_3 < _5]
  isTrue[_6 > _3]
  isTrue[_6 > _5]
  isTrue[_4 < _6]
  isTrue[_6 < _9]
  isTrue[_9 > _7]
  isTrue[_10 < _15]
  isTrue[_11 <= _11]

  isTrue[_0#Inc === _1]
  isTrue[_1#Inc === _2]
  isTrue[_2#Inc === _3]
  isTrue[_3#Inc === _4]
  isTrue[_4#Inc === _5]
  isTrue[_5#Inc === _6]
  isTrue[_6#Inc === _7]
  isTrue[_7#Inc === _8]
  isTrue[_8#Inc === _9]
  isTrue[_9#Inc === _10]
  isTrue[_10#Inc === _11]
  isTrue[_11#Inc === _12]
  isTrue[_12#Inc === _13]
  isTrue[_13#Inc === _14]
  isTrue[_14#Inc === _15]

  isTrue[_1#Dec === _0]
  isTrue[_2#Dec === _1]
  isTrue[_3#Dec === _2]
  isTrue[_4#Dec === _3]
  isTrue[_5#Dec === _4]
  isTrue[_6#Dec === _5]
  isTrue[_7#Dec === _6]
  isTrue[_8#Dec === _7]
  isTrue[_9#Dec === _8]
  isTrue[_10#Dec === _9]
  isTrue[_11#Dec === _10]
  isTrue[_12#Dec === _11]
  isTrue[_13#Dec === _12]
  isTrue[_14#Dec === _13]
  isTrue[_15#Dec === _14]

  // basic sum tests
  isTrue[_0 + _0 === _0]
  isTrue[_1 + _0 === _1]
  isTrue[_0 + _1 === _1]
  isTrue[_0 + _3 === _3]
  isTrue[_3 + _0 === _3]
  isTrue[_3 + _5 === _8]
  isTrue[_5 + _3 === _8]
  isTrue[_3 + _2 === _5]

  // basic product tests
  isTrue[_0 * _0 === _0]
  isTrue[_1 * _0 === _0]
  isTrue[_0 * _1 === _0]
  isTrue[_1 * _1 === _1]
  isTrue[_1 * _9 === _9]
  isTrue[_9 * _1 === _9]
  isTrue[_2 * _2 === _4]
  isTrue[_2 * _3 === _6]
  isTrue[_3 * _2 === _6]
  isTrue[_2 * _4 === _8]
  isTrue[_4 * _2 === _8]
  isTrue[_3 * _3 === _9]

  isTrue[_0#YodaExp[_1] === _1]
  isTrue[_1#YodaExp[_0] === _0]
  isTrue[_0#YodaExp[_0] === _1]
  isTrue[_1 ^ _1 === _1]
  isTrue[_5 ^ _0 === _1]
  isTrue[_5 ^ _1 === _5]
  isTrue[_2 ^ _2 === _4]
  isTrue[_2 ^ _3 === _8]
  isTrue[_3 ^ _2 === _9]
  isTrue[_3 ^ _3 === *[_9, _3]]

  // some more tests
  isTrue[+[_1, *[_8, _10]] === ^[_9, _2]]
  isTrue[*[_4#Sq, _4#Sq] === ^[_2, _8]]

  // subtraction tests
  implicitly[DenseDiff[_10, _10, _0]]
  implicitly[DenseDiff[_6, _2, _4]]
  implicitly[DenseDiff[_17, _13, _4]]
  illTyped { """DenseDiff[_7, _10, _3]""" }

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
  binaryLaws[_3, _9]
  binaryLaws[_12, _14]
  binaryLaws[_1, _3]
  binaryLaws[_6, _11]
  binaryLaws[_7, _12]
  binaryLaws[_3, _15]
  binaryLaws[_1, _4]
  binaryLaws[_11, _12]
  binaryLaws[_2, _4]
  binaryLaws[_12, _15]
  binaryLaws[_6, _10]
  binaryLaws[_5, _5]
  binaryLaws[_7, _14]
  binaryLaws[_5, _10]
  binaryLaws[_4, _5]
  binaryLaws[_8, _15]
  binaryLaws[_11, _14]
  binaryLaws[_6, _15]
  binaryLaws[_3, _12]
  binaryLaws[_2, _13]
  binaryLaws[_2, _2]
  binaryLaws[_13, _15]
  binaryLaws[_1, _7]
  binaryLaws[_1, _1]
  binaryLaws[_2, _6]
  binaryLaws[_4, _12]
  binaryLaws[_2, _3]
  binaryLaws[_11, _11]
  binaryLaws[_7, _13]
  binaryLaws[_4, _10]
  binaryLaws[_6, _9]
  binaryLaws[_9, _9]
  binaryLaws[_2, _14]
  binaryLaws[_12, _12]
  binaryLaws[_12, _13]
  binaryLaws[_5, _11]
  binaryLaws[_0, _0]
  binaryLaws[_1, _12]
  binaryLaws[_9, _13]
  binaryLaws[_1, _10]
  binaryLaws[_8, _14]
  binaryLaws[_6, _12]
  binaryLaws[_4, _11]
  binaryLaws[_4, _8]
  binaryLaws[_1, _2]
  binaryLaws[_6, _8]
  binaryLaws[_5, _15]
  binaryLaws[_4, _15]
  binaryLaws[_3, _7]
  binaryLaws[_7, _9]
  binaryLaws[_3, _10]
  binaryLaws[_8, _13]
  binaryLaws[_8, _9]
  binaryLaws[_7, _15]
  binaryLaws[_1, _9]
  binaryLaws[_5, _12]
  binaryLaws[_3, _11]
  binaryLaws[_3, _5]
  binaryLaws[_4, _4]
  binaryLaws[_0, _7]
  binaryLaws[_2, _12]
  binaryLaws[_0, _12]
  binaryLaws[_4, _14]
  binaryLaws[_8, _10]
  binaryLaws[_7, _8]
  binaryLaws[_1, _6]
  binaryLaws[_4, _6]
  binaryLaws[_3, _14]
  binaryLaws[_9, _11]
  binaryLaws[_8, _11]
  binaryLaws[_10, _10]
  binaryLaws[_11, _15]
  binaryLaws[_2, _7]
  binaryLaws[_2, _10]
  binaryLaws[_7, _7]
  binaryLaws[_1, _5]
  binaryLaws[_0, _5]
  binaryLaws[_10, _12]
  binaryLaws[_0, _8]
  binaryLaws[_0, _13]
  binaryLaws[_13, _14]
  binaryLaws[_14, _14]
  binaryLaws[_6, _6]
  binaryLaws[_1, _15]
  binaryLaws[_7, _11]
  binaryLaws[_10, _15]
  binaryLaws[_4, _9]
  binaryLaws[_15, _15]
  binaryLaws[_0, _11]
  binaryLaws[_8, _12]
  binaryLaws[_4, _13]
  binaryLaws[_7, _10]
  binaryLaws[_0, _14]
  binaryLaws[_6, _13]
  binaryLaws[_10, _11]
  binaryLaws[_11, _13]
  binaryLaws[_2, _11]
  binaryLaws[_2, _5]
  binaryLaws[_1, _13]
  binaryLaws[_0, _10]

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
  ternaryLaws[_1, _3, _6]
  ternaryLaws[_0, _2, _13]
  ternaryLaws[_3, _14, _15]
  ternaryLaws[_5, _8, _8]
  ternaryLaws[_2, _11, _13]
  ternaryLaws[_0, _2, _8]
  ternaryLaws[_6, _14, _14]
  ternaryLaws[_2, _2, _15]
  ternaryLaws[_10, _11, _11]
  ternaryLaws[_0, _0, _0]
  ternaryLaws[_0, _0, _10]
  ternaryLaws[_5, _6, _15]
  ternaryLaws[_1, _5, _13]
  ternaryLaws[_3, _4, _10]
  ternaryLaws[_2, _5, _10]
  ternaryLaws[_2, _3, _3]
  ternaryLaws[_4, _13, _15]
  ternaryLaws[_4, _8, _11]
  ternaryLaws[_0, _7, _13]
  ternaryLaws[_3, _4, _14]
  ternaryLaws[_4, _10, _15]
  ternaryLaws[_9, _9, _12]
  ternaryLaws[_4, _11, _12]
  ternaryLaws[_2, _11, _11]
  ternaryLaws[_0, _12, _15]
  ternaryLaws[_3, _3, _13]
  ternaryLaws[_8, _9, _11]
  ternaryLaws[_8, _8, _12]
  ternaryLaws[_1, _9, _15]
  ternaryLaws[_3, _6, _12]
  ternaryLaws[_9, _9, _10]
  ternaryLaws[_1, _8, _8]
  ternaryLaws[_2, _3, _12]
  ternaryLaws[_1, _6, _15]
  ternaryLaws[_1, _8, _12]
  ternaryLaws[_2, _6, _15]
  ternaryLaws[_2, _4, _6]
  ternaryLaws[_1, _1, _1]
  ternaryLaws[_4, _8, _15]
  ternaryLaws[_8, _10, _10]
  ternaryLaws[_7, _7, _7]
  ternaryLaws[_8, _8, _10]
  ternaryLaws[_2, _2, _14]
  ternaryLaws[_6, _6, _7]
  ternaryLaws[_7, _8, _10]
  ternaryLaws[_3, _5, _14]
  ternaryLaws[_2, _10, _13]
  ternaryLaws[_2, _3, _10]
  ternaryLaws[_0, _0, _1]
  ternaryLaws[_4, _4, _8]
  ternaryLaws[_2, _13, _13]
  ternaryLaws[_10, _14, _15]
  ternaryLaws[_3, _13, _15]
  ternaryLaws[_7, _12, _14]
  ternaryLaws[_6, _7, _14]
  ternaryLaws[_6, _6, _10]
  ternaryLaws[_2, _8, _12]
  ternaryLaws[_2, _13, _15]
  ternaryLaws[_2, _2, _8]
  ternaryLaws[_0, _12, _13]
  ternaryLaws[_4, _5, _8]
  ternaryLaws[_7, _8, _8]
  ternaryLaws[_4, _5, _13]
  ternaryLaws[_0, _5, _15]
  ternaryLaws[_2, _13, _14]
  ternaryLaws[_0, _5, _12]
  ternaryLaws[_2, _2, _9]
  ternaryLaws[_3, _6, _8]
  ternaryLaws[_0, _6, _9]
  ternaryLaws[_5, _5, _6]
  ternaryLaws[_2, _5, _11]
  ternaryLaws[_0, _3, _14]
  ternaryLaws[_1, _3, _3]
  ternaryLaws[_4, _8, _14]
  ternaryLaws[_1, _2, _6]
  ternaryLaws[_9, _10, _10]
  ternaryLaws[_1, _2, _2]
  ternaryLaws[_1, _10, _14]
  ternaryLaws[_4, _9, _15]
  ternaryLaws[_0, _4, _6]
  ternaryLaws[_3, _11, _13]
  ternaryLaws[_5, _7, _12]
  ternaryLaws[_4, _13, _14]
  ternaryLaws[_3, _10, _13]
  ternaryLaws[_0, _2, _2]
  ternaryLaws[_5, _8, _9]
  ternaryLaws[_4, _6, _14]
  ternaryLaws[_1, _8, _10]
  ternaryLaws[_1, _5, _8]
  ternaryLaws[_6, _11, _15]
  ternaryLaws[_1, _1, _6]
  ternaryLaws[_6, _10, _13]
  ternaryLaws[_4, _11, _14]
  ternaryLaws[_5, _11, _11]
  ternaryLaws[_3, _4, _11]
  ternaryLaws[_0, _5, _14]
  ternaryLaws[_4, _4, _15]
  ternaryLaws[_2, _2, _12]
  ternaryLaws[_9, _11, _12]
  ternaryLaws[_0, _4, _15]

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
  ternaryExpLaws[_0, _0, _0]
  ternaryExpLaws[_0, _0, _1]
  ternaryExpLaws[_0, _0, _2]
  ternaryExpLaws[_0, _0, _3]
  ternaryExpLaws[_0, _1, _1]
  ternaryExpLaws[_0, _1, _2]
  ternaryExpLaws[_0, _1, _3]
  ternaryExpLaws[_0, _2, _2]
  ternaryExpLaws[_0, _2, _3]
  ternaryExpLaws[_0, _3, _3]
  ternaryExpLaws[_1, _0, _0]
  ternaryExpLaws[_1, _0, _1]
  ternaryExpLaws[_1, _0, _2]
  ternaryExpLaws[_1, _0, _3]
  ternaryExpLaws[_1, _1, _1]
  ternaryExpLaws[_1, _1, _2]
  ternaryExpLaws[_1, _1, _3]
  ternaryExpLaws[_1, _2, _2]
  ternaryExpLaws[_1, _2, _3]
  ternaryExpLaws[_1, _3, _3]
  ternaryExpLaws[_2, _0, _0]
  ternaryExpLaws[_2, _0, _1]
  ternaryExpLaws[_2, _0, _2]
  ternaryExpLaws[_2, _0, _3]
  ternaryExpLaws[_2, _1, _1]
  ternaryExpLaws[_2, _1, _2]
  ternaryExpLaws[_2, _1, _3]
  ternaryExpLaws[_2, _2, _2]
  ternaryExpLaws[_2, _2, _3]
  ternaryExpLaws[_2, _3, _3]
  ternaryExpLaws[_3, _0, _0]
  ternaryExpLaws[_3, _0, _1]
  ternaryExpLaws[_3, _0, _2]
  ternaryExpLaws[_3, _0, _3]
  ternaryExpLaws[_3, _1, _1]
  ternaryExpLaws[_3, _1, _2]
  ternaryExpLaws[_3, _1, _3]
  ternaryExpLaws[_3, _2, _2]
  ternaryExpLaws[_3, _2, _3]
  ternaryExpLaws[_3, _3, _3]
  ternaryExpLaws[_4, _0, _0]
  ternaryExpLaws[_4, _0, _1]
  ternaryExpLaws[_4, _0, _2]
  ternaryExpLaws[_4, _0, _3]
  ternaryExpLaws[_4, _1, _1]
  ternaryExpLaws[_4, _1, _2]
  ternaryExpLaws[_4, _1, _3]
  ternaryExpLaws[_4, _2, _2]
  ternaryExpLaws[_4, _2, _3]
  ternaryExpLaws[_4, _3, _3]
  ternaryExpLaws[_5, _0, _0]
  ternaryExpLaws[_5, _0, _1]
  ternaryExpLaws[_5, _0, _2]
  ternaryExpLaws[_5, _0, _3]
  ternaryExpLaws[_5, _1, _1]
  ternaryExpLaws[_5, _1, _2]
  ternaryExpLaws[_5, _1, _3]
  ternaryExpLaws[_5, _2, _2]
  ternaryExpLaws[_5, _2, _3]
  ternaryExpLaws[_5, _3, _3]

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
  unaryLaws[_0]
  unaryLaws[_1]
  unaryLaws[_2]
  unaryLaws[_3]
  unaryLaws[_4]
  unaryLaws[_5]
  unaryLaws[_6]
  unaryLaws[_7]
  unaryLaws[_8]
  unaryLaws[_9]
  unaryLaws[_10]
  unaryLaws[_11]
  unaryLaws[_12]
  unaryLaws[_13]
  unaryLaws[_14]
  unaryLaws[_15]
  unaryLaws[_16]
  unaryLaws[_17]
  unaryLaws[_18]
  unaryLaws[_19]
  unaryLaws[_20]
  unaryLaws[_21]
  unaryLaws[_22]

  "A dense number type" should "evaluate to integers correctly" in {
    assert(toLong[_0] == 0)
    assert(toLong[_1] == 1)
    assert(toLong[_2] == 2)
    assert(toLong[_3] == 3)
    assert(toLong[_4] == 4)
    assert(toLong[_5] == 5)
    assert(toLong[_6] == 6)
    assert(toLong[_7] == 7)
    assert(toLong[_8] == 8)
    assert(toLong[_9] == 9)
    assert(toLong[_10] == 10)
    assert(toLong[_11] == 11)
    assert(toLong[_12] == 12)
    assert(toLong[_13] == 13)
    assert(toLong[_14] == 14)
    assert(toLong[_15] == 15)
    assert(toLong[_16] == 16)
    assert(toLong[_17] == 17)
    assert(toLong[_18] == 18)
    assert(toLong[_19] == 19)
    assert(toLong[_20] == 20)
    assert(toLong[_21] == 21)
    assert(toLong[_22] == 22)
  }
}
