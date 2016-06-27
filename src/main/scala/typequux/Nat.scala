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

/** Peano natural numbers
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait Nat {
  type Match [NonZero[N <: Nat] <: Up, IfZero <: Up, Up] <: Up
  type Compare [N <: Nat] <: Comparison
  type FoldR [Init <: Type, Type, F <: Fold[Nat, Type]] <: Type
  type FoldL [Init <: Type, Type, F <: Fold[Nat, Type]] <: Type
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

  /** Typeclass for typelevel fold
    *
    * @author Harshad Deo
    * @since 0.1
    */
  trait Fold[-Elem, Value] {
    type Apply [E <: Elem, Acc <: Value] <: Value
    def apply[N <: Elem, Acc <: Value](n: N, acc: Acc): Apply[N, Acc]
  }

  object Nat0 extends Nat {
    override type Match[NonZero[N <: Nat] <: Up, IfZero <: Up, Up] = IfZero
    override type Compare[N <: Nat] = N#Match[Nat.ConstLt, EQ, Comparison]
    override type FoldR[Init <: Type, Type, F <: Fold[Nat, Type]] = Init
    override type FoldL[Init <: Type, Type, F <: Fold[Nat, Type]] = Init
  }

  trait Succ[N <: Nat] extends Nat {
    override type Match[NonZero[M <: Nat] <: Up, IfZero <: Up, Up] = NonZero[N]
    override type Compare[M <: Nat] = M#Match[N#Compare, GT, Comparison]
    override type FoldR[Init <: Type, Type, F <: Fold[Nat, Type]] = F#Apply[Succ[N], N#FoldR[Init, Type, F]]
    override type FoldL[Init <: Type, Type, F <: Fold[Nat, Type]] = N#FoldL[F#Apply[Succ[N], Init], Type, F]
  }

  type _0 = Nat0
  type _1 = Succ[_0]
  type _2 = Succ[_1]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]
  type _6 = Succ[_5]
  type _7 = Succ[_6]
  type _8 = Succ[_7]
  type _9 = Succ[_8]

  type ConstFalse[X] = False
  type ConstLt[X] = LT

  type Compare[A <: Nat, B <: Nat] = A#Compare[B]
  type ===[A <: Nat, B <: Nat] = A#Compare[B]#eq
  type <[A <: Nat, B <: Nat] = A#Compare[B]#lt
  type <=[A <: Nat, B <: Nat] = A#Compare[B]#le
  type >[A <: Nat, B <: Nat] = A#Compare[B]#gt
  type >=[A <: Nat, B <: Nat] = A#Compare[B]#ge
  type IsO[A <: Nat] = A#Match[ConstFalse, True, Bool]

  type +[A <: Nat, B <: Nat] = A#FoldR[B, Nat, IncFold]
  type *[A <: Nat, B <: Nat] = A#FoldR[_0, Nat, SumFold[B]]
  type Fact[A <: Nat] = A#FoldL[_1, Nat, ProdFold]
  type ^[A <: Nat, B <: Nat] = B#FoldR[_1, Nat, ExpFold[A]]
  type Sq[A <: Nat] = A ^ _2

  trait IncFold extends Fold[Any, Nat] {
    override type Apply[E, Acc <: Nat] = Succ[Acc]
  }
  trait SumFold[By <: Nat] extends Fold[Nat, Nat] {
    override type Apply[N <: Nat, Acc <: Nat] = By + Acc
  }
  trait ProdFold extends Fold[Nat, Nat] {
    override type Apply[N <: Nat, Acc <: Nat] = *[N, Acc]
  }
  trait ExpFold[By <: Nat] extends Fold[Nat, Nat] {
    override type Apply[N <: Nat, Acc <: Nat] = *[By, Acc]
  }

  sealed class NatRep[N <: Nat](val v: Int) {
    def succ: NatRep[Succ[N]] = new NatRep(v + 1)
  }
  object NatRep {
    implicit object NatRep0 extends NatRep[Nat0](0)
    implicit def natRepSucc[N <: Nat](implicit ev: NatRep[N]): NatRep[Succ[N]] = ev.succ
  }

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

  implicit def natDiff0[M <: Nat]: NatDiff[M, Nat0, M] = new NatDiff[M, Nat0, M] {}
  implicit def natDiffSucc[MP <: Nat, SP <: Nat, D <: Nat](
      implicit ev: NatDiff[MP, SP, D]): NatDiff[Succ[MP], Succ[SP], D] =
    new NatDiff[Succ[MP], Succ[SP], D] {}
}
