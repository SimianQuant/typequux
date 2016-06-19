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
package typequux.constraint

import typequux._
import typequux._

/**
  * Typeclass to append two objects, usually product datatypes like tuples and hlists
  *
  * @since 0.1
  */
sealed trait AppendConstraint[A, B, R] {
  def apply(a: A, b: B): R
}

object AppendConstraint {

  /** Base case for hlists - appending to an empty list returns the same list 
    * @tparam B List being appended
    */
  implicit def happendNilResult[B <: HList]: AppendConstraint[HNil, B, B] = new AppendConstraint[HNil, B, B] {
    override def apply(a: HNil, b: B) = b
  }

  /** Induction case for hlists: append to the tail and cons the head
    *
    * @tparam H head of the left hand operand
    * @tparam T tail of the left hand operand
    * @tparam B list being appended
    * @tparam R result of appending B to T
    */
  implicit def hAppendConsResult[H, T <: HList, B <: HList, R <: HList](
      implicit ev: AppendConstraint[T, B, R]): AppendConstraint[H :+: T, B, H :+: R] =
    new AppendConstraint[H :+: T, B, H :+: R] {
      override def apply(a: H :+: T, b: B) = a.head :+: ev(a.tail, b)
    }

  /** Appends right tuple to the left one by converting both to hlists and converting the result back to a tuple
    *
    * @tparam A left hand tuple
    * @tparam B right hand tuple
    * @tparam HLA HList equivalent of A
    * @tparam HLB HList equivalent of B
    * @tparam HLR HLB appended to HLA
    * @tparam R Tuple equivalent of HLR
    */
  implicit def tpAppendConstraint[A, B, R, HLA <: HList, HLB <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[A, HLA],
      ev1: Tuple2HListConverter[B, HLB],
      ev2: AppendConstraint[HLA, HLB, HLR],
      ev3: HList2TupleConverter[R, HLR]): AppendConstraint[A, B, R] = new AppendConstraint[A, B, R] {
    override def apply(a: A, b: B) = {
      val hla = ev0(a)
      val hlb = ev1(b)
      val hlr = ev2(hla, hlb)
      ev3(hlr)
    }
  }
}
