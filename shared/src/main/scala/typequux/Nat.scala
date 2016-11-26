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

import language.higherKinds
import Nat._
import typequux._

/** Peano encoding of natural numbers
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait Nat {

  /** Typeconstructor for querying whether this is zero or not
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Match[NonZero[N <: Nat] <: Up, IfZero <: Up, Up] <: Up

  /** Compares with the other peano number
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type Compare[N <: Nat] <: Comparison

  /** Typelevel FoldRight
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type FoldR[Init <: Type, Type, F <: Fold[Nat, Type]] <: Type

  /** Typelevel FoldLeft
    *
    * @author Harshad Deo
    * @since 0.1
    */
  type FoldL[Init <: Type, Type, F <: Fold[Nat, Type]] <: Type
}

/** Contains implementation traits for [[Nat]] and typeconstructor aliases that make usage more pleasant.
  *
  * 1. Additive commutativity: <code> +[A, B] =:= +[B, A]  </code>
  *
  * 2. Additive associativity: <code> +[A, +[B, C]] =:= +[+[A, B], C] </code>
  *
  * 3. Additive identity: <code> +[A, _0] =:= A =:= +[_0, A] </code>
  *
  * 4. Multiplicative commutativity: <code> *[A, B] =:= *[B, A] </code>
  *
  * 5. Multiplicative associativity: <code> *[A, *[B, C]] =:= *[*[A, B], C] </code>
  *
  * 6. Multiplicative identity: <code> *[A, _1] =:= A =:= *[_1, A] </code>
  *
  * 7. Distributivity: <code> *[A, +[B, C]] =:= +[*[A, B], *[A, C]] </code>
  *
  * 8. Zero exponent: <code> ^[A, _0] =:= _1 </code>
  *
  * 9. One exponent: <code> ^[_1, A] =:= _1 </code>
  *
  * 10. Exponent Identity: <code> ^[A, _1] =:= A </code>
  *
  * 11. Total Order
  *
  *
  * @author Harshad Deo
  * @since 0.1
  */
object Nat {

  /** Typeclass for typelevel and valuelevel fold
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  trait Fold[-Elem, Value] {

    /** Typelevel Fold
      *
      * @author Harshad Deo
      * @since 0.1
      */
    type Apply[E <: Elem, Acc <: Value] <: Value

    /** Valuelevel fold
      *
      * @author Harshad Deo
      * @since 0.1
      */
    def apply[N <: Elem, Acc <: Value](n: N, acc: Acc): Apply[N, Acc]
  }

  /** Represents zero in peano encoding of natural numbers
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  object Nat0 extends Nat {
    override type Match[NonZero[N <: Nat] <: Up, IfZero <: Up, Up] = IfZero
    override type Compare[N <: Nat] = N#Match[Nat.ConstLt, EQ, Comparison]
    override type FoldR[Init <: Type, Type, F <: Fold[Nat, Type]] = Init
    override type FoldL[Init <: Type, Type, F <: Fold[Nat, Type]] = Init
  }

  /** Represents a successor in the peano encoding of natural numbers
    *
    * @tparam N Peano-type to which this is a successor
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  trait Succ[N <: Nat] extends Nat {
    override type Match[NonZero[M <: Nat] <: Up, IfZero <: Up, Up] = NonZero[N]
    override type Compare[M <: Nat] = M#Match[N#Compare, GT, Comparison]
    override type FoldR[Init <: Type, Type, F <: Fold[Nat, Type]] = F#Apply[Succ[N], N#FoldR[Init, Type, F]]
    override type FoldL[Init <: Type, Type, F <: Fold[Nat, Type]] = N#FoldL[F#Apply[Succ[N], Init], Type, F]
  }

  /** Peano 0
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _0 = Nat0

  /** Peano 1
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _1 = Succ[_0]

  /** Peano 2
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _2 = Succ[_1]

  /** Peano 3
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _3 = Succ[_2]

  /** Peano 4
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _4 = Succ[_3]

  /** Peano 5
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _5 = Succ[_4]

  /** Peano 6
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _6 = Succ[_5]

  /** Peano 7
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _7 = Succ[_6]

  /** Peano 8
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _8 = Succ[_7]

  /** Peano 9
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _9 = Succ[_8]

  private[Nat] type ConstFalse[X] = False
  private[Nat] type ConstLt[X] = LT

  /** Alias for getting the result of comparing two numbers
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Compare[A <: Nat, B <: Nat] = A#Compare[B]

  /** Alias for checking if the two numbers are equal
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type ===[A <: Nat, B <: Nat] = A#Compare[B]#eq

  /** Alias for checking if the first number is less than the second
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type <[A <: Nat, B <: Nat] = A#Compare[B]#lt

  /** Alias for checking if the first number is less than or equal to the second
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type <=[A <: Nat, B <: Nat] = A#Compare[B]#le

  /** Alias for checking if the first number is greater than the second
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type >[A <: Nat, B <: Nat] = A#Compare[B]#gt

  /** Alias for checking if the first number is greater than or equal to the second
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type >=[A <: Nat, B <: Nat] = A#Compare[B]#ge

  /** Alias for checking if the number is zero
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type IsO[A <: Nat] = A#Match[ConstFalse, True, Bool]

  /** Alias for adding two numbers
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type +[A <: Nat, B <: Nat] = A#FoldR[B, Nat, IncFold]

  /** Alias for multiplying two numbers
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type *[A <: Nat, B <: Nat] = A#FoldR[_0, Nat, SumFold[B]]

  /** Alias for computing the factorial of the number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Fact[A <: Nat] = A#FoldL[_1, Nat, ProdFold]

  /** Alias for computing the exponent of the number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type ^[A <: Nat, B <: Nat] = B#FoldR[_1, Nat, ExpFold[A]]

  /** Alias for computing the square of the number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Sq[A <: Nat] = A ^ _2

  /** Fold to compute the increment of a number
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  trait IncFold extends Fold[Any, Nat] {
    override type Apply[E, Acc <: Nat] = Succ[Acc]
  }

  /** Fold to compute the sum
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  trait SumFold[By <: Nat] extends Fold[Nat, Nat] {
    override type Apply[N <: Nat, Acc <: Nat] = By + Acc
  }

  /** Fold to compute the product
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  trait ProdFold extends Fold[Nat, Nat] {
    override type Apply[N <: Nat, Acc <: Nat] = *[N, Acc]
  }

  /** Fold to compute the exponent
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  trait ExpFold[By <: Nat] extends Fold[Nat, Nat] {
    override type Apply[N <: Nat, Acc <: Nat] = *[By, Acc]
  }

  /** Builds a value level representation of the [[Nat]]
    *
    * @tparam N Natural number for which the value level representation is being built
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  sealed class NatRep[N <: Nat](val v: Int) {
    def succ: NatRep[Succ[N]] = new NatRep(v + 1)
  }

  /** Provides implicit definitions to build a value level representation of a [[Nat]]
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  object NatRep {

    /** Implements [[NatRep]] for [[Nat0]]
      *
      * @author Harshad Deo
      * @since 0.1
      */
    implicit object NatRep0 extends NatRep[Nat0](0)

    /** Builds [[NatRep]] for [[Succ]]
      *
      * @author Harshad Deo
      * @since 0.1
      */
    implicit def natRepSucc[N <: Nat](implicit ev: NatRep[N]): NatRep[Succ[N]] = ev.succ
  }

  /** Builds a value level representation of a [[Nat]]
    *
    * @tparam N Nat for which the value level representation is to be built
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  def toInt[N <: Nat](implicit ev: NatRep[N]): Int = ev.v
}

/** Marker trait for typelevel subtraction of [[Nat]]
  *
  * @tparam M Minuend
  * @tparam S Subtrahend
  * @tparam D Difference
  *
  * @author Harshad Deo
  * @since 0.1
  */
trait NatDiff[M <: Nat, S <: Nat, D <: Nat]

/** Contains implicit definitions to build a [[NatDiff]] marker
  *
  * @author Harshad Deo
  * @since 0.1
  */
object NatDiff {

  /** Base case for [[NatDiff]]
    *
    * @tparam M Minuend
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def natDiff0[M <: Nat]: NatDiff[M, Nat0, M] = new NatDiff[M, Nat0, M] {}

  /** Induction case for [[NatDiff]]
    *
    * @tparam MP Minuend - 1
    * @tparam SP Subtrahend - 1
    * @tparam D Difference
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def natDiffSucc[MP <: Nat, SP <: Nat, D <: Nat](
      implicit ev: NatDiff[MP, SP, D]): NatDiff[Succ[MP], Succ[SP], D] =
    new NatDiff[Succ[MP], Succ[SP], D] {}
}
