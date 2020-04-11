/**
  * Copyright 2020 Harshad Deo
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

import typequux.Bool
import typequux.Bool.{False, True}

/**
  * Specifications for church encodings of booleans
  *
  * @author Harshad Deo
  * @since 0.1
  */
class BoolSpec extends BaseSpec {

  import Bool._

  type Rep[A <: Bool] = A#If[Int, String, Any]

  it should "pass rep tests" in {
    assert(eqv[Rep[True], Int])
    assert(eqv[Rep[False], String])
    assertTypeError { """implicitly[Rep[True] =:= String]""" }
    assertTypeError { """implicitly[Rep[False] =:= Int]""" }
    assertTypeError { """implicitly[Rep[True]] =:= Long""" }
  }

  it should "pass truth table tests" in {

    assertCompiles { """implicitly[Not[True] =:= False]""" }
    assertCompiles { """implicitly[Not[False] =:= True]""" }
    assertTypeError { """implicitly[Not[True] =:= True]""" }
    assertTypeError { """implicitly[Not[False] =:= False]""" }

    assertCompiles { """implicitly[False || False =:= False]""" }
    assertCompiles { """implicitly[False || True =:= True]""" }
    assertCompiles { """implicitly[True || False =:= True]""" }
    assertCompiles { """implicitly[True || True =:= True]""" }
    assertTypeError { """implicitly[False || False =:= True]""" }
    assertTypeError { """implicitly[False || True =:= False]""" }
    assertTypeError { """implicitly[True || False =:= False]""" }
    assertTypeError { """implicitly[True || True =:= False]""" }

    assertCompiles { """implicitly[False && False =:= False]""" }
    assertCompiles { """implicitly[False && True =:= False]""" }
    assertCompiles { """implicitly[True && False =:= False]""" }
    assertCompiles { """implicitly[True && True =:= True]""" }
    assertTypeError { """implicitly[False && False =:= True]""" }
    assertTypeError { """implicitly[False && True =:= True]""" }
    assertTypeError { """implicitly[True && False =:= True]""" }
    assertTypeError { """implicitly[True && True =:= False]""" }

    assertCompiles { """implicitly[False Xor False =:= False]""" }
    assertCompiles { """implicitly[True Xor False =:= True]""" }
    assertCompiles { """implicitly[False Xor True =:= True]""" }
    assertCompiles { """implicitly[True Xor True =:= False]""" }
    assertTypeError { """implicitly[False Xor False =:= True]""" }
    assertTypeError { """implicitly[True Xor False =:= False]""" }
    assertTypeError { """implicitly[False Xor True =:= False]""" }
    assertTypeError { """implicitly[True Xor True =:= True]""" }

    assertCompiles { """implicitly[False ->> False =:= True]""" }
    assertCompiles { """implicitly[True ->> False =:= False]""" }
    assertCompiles { """implicitly[False ->> True =:= True]""" }
    assertCompiles { """implicitly[True ->> True =:= True]""" }
    assertTypeError { """implicitly[False ->> False =:= False]""" }
    assertTypeError { """implicitly[True ->> False =:= True]""" }
    assertTypeError { """implicitly[False ->> True =:= False]""" }
    assertTypeError { """implicitly[True ->> True =:= False]""" }

    assertCompiles { """implicitly[False Eqv False =:= True]""" }
    assertCompiles { """implicitly[True Eqv False =:= False]""" }
    assertCompiles { """implicitly[False Eqv True =:= False]""" }
    assertCompiles { """implicitly[True Eqv True =:= True]""" }
    assertTypeError { """ implicitly[False Eqv False =:= False]""" }
    assertTypeError { """ implicitly[True Eqv False =:= True]""" }
    assertTypeError { """ implicitly[False Eqv True =:= True]""" }
    assertTypeError { """ implicitly[True Eqv True =:= False]""" }

  }

  class AssociativityOr[A, B, C]
  implicit def toAssociativeOr[A <: Bool, B <: Bool, C <: Bool](
      implicit ev: ||[A, ||[B, C]] =:= ||[||[A, B], C]
  ): AssociativityOr[A, B, C] =
    new AssociativityOr[A, B, C]

  class AssociativityAnd[A, B, C]
  implicit def toAssociativeAnd[A <: Bool, B <: Bool, C <: Bool](
      implicit ev: &&[A, &&[B, C]] =:= &&[&&[A, B], C]
  ): AssociativityAnd[A, B, C] =
    new AssociativityAnd[A, B, C]

  class DistributivityAndOverOr[A, B, C]
  implicit def toDOOO[A <: Bool, B <: Bool, C <: Bool](
      implicit ev: &&[A, ||[B, C]] =:= ||[&&[A, B], &&[A, C]]
  ): DistributivityAndOverOr[A, B, C] =
    new DistributivityAndOverOr[A, B, C]

  class DistributivityOrOverAnd[A, B, C]
  implicit def toDOOA[A <: Bool, B <: Bool, C <: Bool](
      implicit ev: ||[A, &&[B, C]] =:= &&[||[A, B], ||[A, C]]
  ): DistributivityOrOverAnd[A, B, C] =
    new DistributivityOrOverAnd[A, B, C]

  def ternaryLaws[A <: Bool, B <: Bool, C <: Bool](
      implicit ev0: AssociativityOr[A, B, C],
      ev1: AssociativityAnd[A, B, C],
      ev2: DistributivityAndOverOr[A, B, C],
      ev3: DistributivityOrOverAnd[A, B, C]
  ): Boolean = true

  it should "pass ternary laws " in {
    assertCompiles { """ternaryLaws[False, False, False]""" }
    assertCompiles { """ternaryLaws[False, False, True]""" }
    assertCompiles { """ternaryLaws[False, True, False]""" }
    assertCompiles { """ternaryLaws[False, True, True]""" }
    assertCompiles { """ternaryLaws[True, False, False]""" }
    assertCompiles { """ternaryLaws[True, False, True]""" }
    assertCompiles { """ternaryLaws[True, True, False]""" }
    assertCompiles { """ternaryLaws[True, True, True]""" }
  }

  // Binary Laws

  class CommutativityOr[A, B]
  implicit def toCommutativeOr[A <: Bool, B <: Bool](implicit ev: ||[A, B] =:= ||[B, A]): CommutativityOr[A, B] =
    new CommutativityOr[A, B]

  class CommutativityAnd[A, B]
  implicit def toCommutativeAnd[A <: Bool, B <: Bool](implicit ev: &&[A, B] =:= &&[B, A]): CommutativityAnd[A, B] =
    new CommutativityAnd[A, B]

  class Absorption1[A, B]
  implicit def toAbsorption1[A <: Bool, B <: Bool](implicit ev: &&[A, ||[A, B]] =:= A): Absorption1[A, B] =
    new Absorption1[A, B]

  class Absorption2[A, B]
  implicit def toAbsorption2[A <: Bool, B <: Bool](implicit ev: ||[A, &&[A, B]] =:= A): Absorption2[A, B] =
    new Absorption2[A, B]

  class DeMorgan1[A, B]
  implicit def toDeMorgan1[A <: Bool, B <: Bool](implicit ev: &&[Not[A], Not[B]] =:= Not[A || B]): DeMorgan1[A, B] =
    new DeMorgan1[A, B]

  class Demorgan2[A, B]
  implicit def toDeMorgan2[A <: Bool, B <: Bool](implicit ev: ||[Not[A], Not[B]] =:= Not[A && B]): Demorgan2[A, B] =
    new Demorgan2[A, B]

  def binaryLaws[A <: Bool, B <: Bool](
      implicit ev0: CommutativityOr[A, B],
      ev1: CommutativityAnd[A, B],
      ev2: Absorption1[A, B],
      ev3: Absorption2[A, B],
      ev4: DeMorgan1[A, B],
      ev5: Demorgan2[A, B]
  ): Boolean = true

  it should "pass binary laws" in {
    assertCompiles { """binaryLaws[False, False]""" }
    assertCompiles { """binaryLaws[False, True]""" }
    assertCompiles { """binaryLaws[True, False]""" }
    assertCompiles { """binaryLaws[True, True]""" }
  }

  // Unary Laws

  class IdentityOr[A]
  implicit def toIdentityOr[A <: Bool](implicit ev: A || False =:= A): IdentityOr[A] = new IdentityOr[A]

  class IdentityAnd[A]
  implicit def toIdentityAnd[A <: Bool](implicit ev: A && True =:= A): IdentityAnd[A] = new IdentityAnd[A]

  class AnnhilatorAnd[A]
  implicit def toAnnhilatorAnd[A <: Bool](implicit ev: A && False =:= False): AnnhilatorAnd[A] = new AnnhilatorAnd[A]

  class AnnhilatorOr[A]
  implicit def toAnnhilatorOr[A <: Bool](implicit ev: A || True =:= True): AnnhilatorOr[A] = new AnnhilatorOr[A]

  class IdempotenceAnd[A]
  implicit def toIdempotenceAnd[A <: Bool](implicit ev: A && A =:= A): IdempotenceAnd[A] = new IdempotenceAnd[A]

  class IdempotenceOr[A]
  implicit def toIdempotenceOr[A <: Bool](implicit ev: A || A =:= A): IdempotenceOr[A] = new IdempotenceOr[A]

  class ComplementAnd[A]
  implicit def toComplementAnd[A <: Bool](implicit ev: A && Not[A] =:= False): ComplementAnd[A] = new ComplementAnd[A]

  class ComplementOr[A]
  implicit def toComplementOr[A <: Bool](implicit ev: A || Not[A] =:= True): ComplementOr[A] = new ComplementOr[A]

  class DoubleNegation[A]
  implicit def toDoubleNegation[A <: Bool](implicit ev: Not[Not[A]] =:= A): DoubleNegation[A] = new DoubleNegation[A]

  def unaryLaws[A <: Bool](
      implicit ev0: IdentityOr[A],
      ev1: IdentityAnd[A],
      ev2: AnnhilatorAnd[A],
      ev3: AnnhilatorOr[A],
      ev4: IdempotenceAnd[A],
      ev5: IdempotenceOr[A],
      ev6: ComplementAnd[A],
      ev7: ComplementOr[A],
      ev8: DoubleNegation[A]
  ) = true

  it should "pass unary laws" in {
    assertCompiles { """unaryLaws[True]""" }
    assertCompiles { """unaryLaws[False]""" }
  }

  //class CommutativityOr[A <: Bool, B <: Bool]

  it should "evaluate to values correctly" in {
    assert(toBoolean[True] == true)
    assert(toBoolean[False] == false)
  }
}
