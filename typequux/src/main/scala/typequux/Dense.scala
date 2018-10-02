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
package typequux

import annotation.tailrec
import Comparison.{EQ, GT, LT}
import constraint.TrueConstraint
import Dense._
import language.higherKinds
import language.experimental.macros
import reflect.macros.whitebox.Context

/** Typelevel representation of dense numbers, stored as a list of [[Dense.Digit]]
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait Dense {

  /** Lowest priority bit
    *
    * @group Representation
    * @author Harshad Deo
    * @since 0.1
    */
  type digit <: Digit

  /** Rest of the bits, stored in reverse order or priority
    *
    * @group Representation
    * @author Harshad Deo
    * @since 0.1
    */
  type tail <: Dense

  /** Increment the number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Inc <: Dense

  /** Decrement the number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Dec <: Dense

  /** Add to the number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Add[b <: Dense] <: Dense

  /** Multiply with the number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Mult[b <: Dense] = b#Match[Karatsuba[b, DNil], DNil, Dense]

  /** Square the number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Sq <: Dense

  /** Yoda exponent - to the power of the base, raise the exponent (this is the base).
    * Implemented this way for efficiency
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type YodaExp[b <: Dense] = ExpHelper[b, _1] // to the power of two, raise base

  /** Unsigned left shift
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type ShiftL <: Dense

  /** Unsigned right shift
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type ShiftR <: Dense

  /** Typeconstructor for querying whether this is zero
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Match[NonZero <: Up, IfZero <: Up, Up] <: Up

  /** Compares with the other dense number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Compare[B <: Dense] = CompareC[B, EQ]

  /**
    * @author Harshad Deo
    * @since 0.1
    */
  protected type Len <: Dense

  /**
    * @author Harshad Deo
    * @since 0.1
    */
  protected type Karatsuba[x <: Dense, res <: Dense] <: Dense // Karatsuba/Russian Peasant/Egyptian multiplication

  /**
    * @author Harshad Deo
    * @since 0.1
    */
  protected type ExpHelper[arg <: Dense, res <: Dense] <: Dense

  /**
    * @author Harshad Deo
    * @since 0.1
    */
  protected type CompareC[B <: Dense, Carry <: Comparison] <: Comparison
}

/** Contains implementation for [[Dense]] and typeconstructor aliases that make usage more pleasant
  *
  * The operations can be shown to satisfy:
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
  * 11. Exponent combination 1: <code> *[^[A, B], ^[A, C]] =:= ^[A, *[B, C]] </code>
  *
  * 12. Exponent combination 2: <code> ^[^[A, B], C] =:= ^[A, *[B, C]] </code>
  *
  * 13. Exponent combination 3: <code> ^[*[A, B], C] =:= *[^[A, C], ^[B, C]] </code>
  *
  * 14. Total Order
  *
  * @author Harshad Deo
  * @since 0.1
  */
object Dense {

  /** Represents a digit in the dense encoding of a natural number
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  sealed trait Digit {

    /** Typeconstructor for querying whether the digit is zero
      *
      * @author Harshad Deo
      * @since 0.1
      */
    type Match[IfOne <: Up, IfZero <: Up, Up] <: Up

    /** Typeconstructor to compare two bits
      *
      * @author Harshad Deo
      * @since 0.1
      */
    type Compare[D <: Digit] <: Comparison
  }

  /** Represents a 0 in the dense encoding of a natural number
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  final class D0 extends Digit {
    override type Match[IfOne <: Up, IfZero <: Up, Up] = IfZero
    override type Compare[D <: Digit] = D#Match[LT, EQ, Comparison]
  }

  /** Represents a 1 in the dense encoding of a natural number
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  final class D1 extends Digit {
    override type Match[IfOne <: Up, IfZero <: Up, Up] = IfOne
    override type Compare[D <: Digit] = D#Match[EQ, GT, Comparison]
  }

  /** Non-zero dense number. The digit is the least significant bit
    *
    * @tparam d Lowest priority bit
    * @tparam T Rest of the bits, in decreasing order of priority
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  final class DCons[d <: Digit, T <: Dense] extends Dense {

    override type digit = d
    override type tail = T

    override type Inc = d#Match[D0 :: T#Inc, D1 :: T, Dense]
    override type Dec = d#Match[T#Match[D0 :: T, DNil, Dense], D1 :: T#Dec, Dense]
    override type Add[b <: Dense] = b#Match[AddNz[b], d :: T, Dense]
    override type Sq = *[d :: T, d :: T]

    override type ShiftR = tail
    override type ShiftL = D0 :: DCons[d, T]

    override type Match[NonZero <: Up, IfZero <: Up, Up] = NonZero

    protected type AddNz[b <: Dense] = d#Match[Add1[b], b#digit :: tail#Add[b#tail], Dense]
    protected type Add1[b <: Dense] = b#digit#Match[D0 :: tail#Add[b#tail]#Inc, d :: tail#Add[b#tail], Dense]
    protected type NewCarry[prev <: Comparison, od <: Digit] = d#Compare[od]#Match[LT, prev, GT, Comparison]
    override protected type Karatsuba[x <: Dense, res <: Dense] =
      tail#Karatsuba[x#ShiftL, digit#Match[x + res, res, Dense]]
    override protected type ExpHelper[arg <: Dense, res <: Dense] =
      tail#ExpHelper[arg#Sq, digit#Match[*[res, arg], res, Dense]]
    override protected type CompareC[B <: Dense, Carry <: Comparison] =
      B#Match[tail#CompareC[B#tail, NewCarry[Carry, B#digit]], GT, Comparison]
    override protected type Len = _1 + tail#Len
  }

  /** Dense Zero
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  final class DNil extends Dense {

    override type tail = Nothing
    override type digit = Nothing

    override type Inc = D1 :: DNil
    override type Dec = Nothing
    override type Add[b <: Dense] = b
    override type Sq = _0

    override type ShiftR = DNil
    override type ShiftL = DNil

    override type Match[NonZero <: Up, IfZero <: Up, Up] = IfZero

    override protected type Karatsuba[x <: Dense, res <: Dense] = res
    override protected type ExpHelper[arg <: Dense, res <: Dense] = res
    override protected type CompareC[B <: Dense, Carry <: Comparison] = B#Match[LT, Carry, Comparison]
    override protected type Len = _0
  }

  /** Builds a new dense number by consing a bit to an existing dense number. The consed bit is the lowest priority
    * bit in the resulting number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type ::[H <: Digit, T <: Dense] = DCons[H, T]

  /** Alias for adding two dense number
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type +[A <: Dense, B <: Dense] = A#Add[B]

  /** Alias for multiplying two dense numbers
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type *[A <: Dense, B <: Dense] = A#Mult[B]

  /** Alias for raising the first Dense number to the power of the second
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type ^[A <: Dense, B <: Dense] = B#YodaExp[A]

  /** Alias for comparing to dense numbers
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Compare[A <: Dense, B <: Dense] = A#Compare[B]

  /** Alias for checking if two dense numbers are equal
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type ===[A <: Dense, B <: Dense] = A#Compare[B]#eq

  /** Alias for checking if the first dense number is less than the second
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type <[A <: Dense, B <: Dense] = A#Compare[B]#lt

  /** Alias for checking whether the first dense number is less than or equal to the second
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type <=[A <: Dense, B <: Dense] = A#Compare[B]#le

  /** Alias for checking whether the first dense number is greater than the second
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type >[A <: Dense, B <: Dense] = A#Compare[B]#gt

  /** Alias for checking whether the first dense number is greater than or equal to the second
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type >=[A <: Dense, B <: Dense] = A#Compare[B]#ge

  /** Alias for squaring a dense number. Makes the code more pleasant to read
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Sq[A <: Dense] = A#Sq

  /** Alias for determing the greatest of two dense numbers
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Max[A <: Dense, B <: Dense] = A#Compare[B]#Match[B, A, A, Dense]

  /** Alias for determining the lease of two dense numbers
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  type Min[A <: Dense, B <: Dense] = A#Compare[B]#Match[A, A, B, Dense]

  /** Dense 0
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _0 = DNil

  /** Dense 1
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _1 = D1 :: DNil

  /** Dense 2
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _2 = D0 :: D1 :: DNil

  /** Dense 3
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _3 = D1 :: D1 :: DNil

  /** Dense 4
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _4 = D0 :: D0 :: D1 :: DNil

  /** Dense 5
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _5 = D1 :: D0 :: D1 :: DNil

  /** Dense 6
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _6 = D0 :: D1 :: D1 :: DNil

  /** Dense 7
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _7 = D1 :: D1 :: D1 :: DNil

  /** Dense 8
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _8 = D0 :: D0 :: D0 :: D1 :: DNil

  /** Dense 9
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _9 = D1 :: D0 :: D0 :: D1 :: DNil

  /** Dense 10
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _10 = D0 :: D1 :: D0 :: D1 :: DNil

  /** Dense 11
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _11 = D1 :: D1 :: D0 :: D1 :: DNil

  /** Dense 12
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _12 = D0 :: D0 :: D1 :: D1 :: DNil

  /** Dense 13
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _13 = D1 :: D0 :: D1 :: D1 :: DNil

  /** Dense 14
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _14 = D0 :: D1 :: D1 :: D1 :: DNil

  /** Dense 15
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _15 = D1 :: D1 :: D1 :: D1 :: DNil

  /** Dense 16
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _16 = D0 :: D0 :: D0 :: D0 :: D1 :: DNil

  /** Dense 17
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _17 = D1 :: D0 :: D0 :: D0 :: D1 :: DNil

  /** Dense 18
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _18 = D0 :: D1 :: D0 :: D0 :: D1 :: DNil

  /** Dense 19
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _19 = D1 :: D1 :: D0 :: D0 :: D1 :: DNil

  /** Dense 20
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _20 = D0 :: D0 :: D1 :: D0 :: D1 :: DNil

  /** Dense 21
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _21 = D1 :: D0 :: D1 :: D0 :: D1 :: DNil

  /** Dense 22
    *
    * @group Number aliases
    * @author Harshad Deo
    * @since 0.1
    */
  type _22 = D0 :: D1 :: D1 :: D0 :: D1 :: DNil

  /** Builds a value level [[scala.Long]] representation of a dense type.
    *
    * @tparam D Type to be converted to a value
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  final class DenseRep[D <: Dense](val v: Long) extends AnyVal

  /** Contains implicit definitions to build the value level representation of a dense type as a [[scala.Long]]
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.1
    */
  object DenseRep {

    /** Builds an instance of [[DenseRep]] by delegating to the macro
      *
      * @tparam D The Dense type to be converted
      *
      * @author Harshad Deo
      * @since 0.6.4
      */
    implicit def build[D <: Dense]: DenseRep[D] = macro buildImpl[D]

    def buildImpl[D <: Dense](c: Context)(wtt: c.WeakTypeTag[D]): c.Tree = {
      import c.universe._

      def processType(tp: Type) = tp match {
        case z: TypeRef => z.dealias
        case _          => tp
      }

      def allTypes(xs: List[Type]): List[Type] = xs match {
        case a :: b :: Nil => a :: allTypes(processType(b).typeArgs)
        case _             => Nil
      }

      val d0ref = implicitly[c.WeakTypeTag[Dense.D0]].tpe
      val d1ref = implicitly[c.WeakTypeTag[Dense.D1]].tpe

      @tailrec
      def go(pending: List[Type], acc: Long, addn: Long): c.Tree = pending match {
        case h :: t =>
          if (h =:= d0ref) {
            go(t, acc, addn << 1)
          } else if (h =:= d1ref) {
            go(t, acc | addn, addn << 1)
          } else {
            c.abort(c.enclosingPosition, s"DenseRep cannot be materialized for ${show(wtt.tpe)}")
          }
        case Nil => q"new typequux.Dense.DenseRep[${wtt.tpe}]($acc)"
      }

      go(allTypes(processType(wtt.tpe).typeArgs), 0, 1)
    }

  }

  /** Builds a value level [[scala.Int]] representation of a dense type
    *
    * @tparam D Type to be converted to a value
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.3.1
    */
  final class DenseIntRep[D](val v: Int) extends AnyVal

  /** Contains implicit definitional to build a value level representation of a dense type as a [[scala.Int]]
    *
    * @group Implementation
    * @author Harshad Deo
    * @since 0.3.1
    */
  object DenseIntRep {

    /** Builds an instance of [[DenseIntRep]] by delegating to the macro
      *
      * @tparam D The Dense type to be converted
      *
      * @author Harshad Deo
      * @since 0.6.4
      */
    implicit def build[D <: Dense]: DenseIntRep[D] = macro buildImpl[D]

    def buildImpl[D <: Dense](c: Context)(wtt: c.WeakTypeTag[D]): c.Tree = {
      import c.universe._

      def processType(tp: Type) = tp match {
        case z: TypeRef => z.dealias
        case _          => tp
      }

      def allTypes(xs: List[Type]): List[Type] = xs match {
        case a :: b :: Nil => a :: allTypes(processType(b).typeArgs)
        case _             => Nil
      }

      val proc = processType(wtt.tpe)
      val dnilRef = implicitly[c.WeakTypeTag[Dense.DNil]].tpe
      val dconsRef = implicitly[c.WeakTypeTag[Dense.DCons[_, _]]].tpe

      if (proc =:= dnilRef) {
        q"new typequux.Dense.DenseIntRep[${wtt.tpe}](0)"
      } else if (proc <:< dconsRef) {

        val d0ref = implicitly[c.WeakTypeTag[Dense.D0]].tpe
        val d1ref = implicitly[c.WeakTypeTag[Dense.D1]].tpe

        @tailrec
        def go(pending: List[Type], acc: Int, addn: Int): c.Tree = pending match {
          case h :: t =>
            if (h =:= d0ref) {
              go(t, acc, addn << 1)
            } else if (h =:= d1ref) {
              go(t, acc | addn, addn << 1)
            } else {
              c.abort(c.enclosingPosition, s"DenseIntRep cannot be materialized for ${show(wtt.tpe)}")
            }
          case Nil => q"new typequux.Dense.DenseIntRep[${wtt.tpe}]($acc)"
        }
        go(allTypes(proc.typeArgs), 0, 1)
      } else {
        c.abort(c.enclosingPosition, s"DenseIntRep cannot be materialized for ${show(wtt.tpe)}")
      }
    }

  }

  /** Builds value level representation of a [[Dense]] as a [[scala.Long]]
    *
    * @tparam D Dense type to be converted to a value
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.1
    */
  def toLong[D <: Dense](implicit dr: DenseRep[D]): Long = dr.v

  /** Builds value level representation of a [[Dense]] as a [[scala.Int]]
    *
    * @tparam D Dense type to be converted to a value
    *
    * @group Operations
    * @author Harshad Deo
    * @since 0.3.1
    */
  def toInt[D <: Dense](implicit dr: DenseIntRep[D]): Int = dr.v

}

/** Marker trait for typelevel subtraction of [[Dense]] numbers.
  *
  * Takes linear time (compared to log time for addition), therefore its usage should be limited
  *
  * @tparam M Minuend
  * @tparam S Subtrahend
  * @tparam D Difference
  *
  * @author Harshad Deo
  * @since 0.1
  */
sealed trait DenseDiff[M, S, D]

/** Contains implicit definitions to build a [[DenseDiff]] marker
  *
  * @author Harshad Deo
  * @since 0.1
  */
object DenseDiff {

  /** Base case for [[DenseDiff]]
    *
    * @tparam M Minuend
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def dsr0[M <: Dense]: DenseDiff[M, _0, M] = new DenseDiff[M, _0, M] {}

  /** Induction case for [[DenseDiff]]
    *
    * @tparam M Minuend
    * @tparam S Subtrahend
    * @tparam DP Difference
    *
    * @author Harshad Deo
    * @since 0.1
    */
  implicit def dsrN[M <: Dense, S <: Dense, DP <: Dense](implicit ev: DenseDiff[M#Dec, S#Dec, DP],
                                                         ev1: TrueConstraint[>[S, _0]]): DenseDiff[M, S, DP] =
    new DenseDiff[M, S, DP] {}
}
