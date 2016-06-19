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

sealed trait ReverseConstraint[A, R] {
  def apply(a: A): R
}

object ReverseConstraint {

  implicit def hlReverseConstraint[A <: HList, R <: HList](
      implicit ev: HReverseResult[A, HNil, R]): ReverseConstraint[A, R] = new ReverseConstraint[A, R] {
    override def apply(a: A) = ev(a, HNil)
  }

  implicit def tpReverseConstraint[T, U, HL <: HList, HLR <: HList](
      implicit ev0: Tuple2HListConverter[T, HL],
      ev1: ReverseConstraint[HL, HLR],
      ev2: HList2TupleConverter[U, HLR]): ReverseConstraint[T, U] = new ReverseConstraint[T, U] {
    override def apply(t: T) = {
      val hl = ev0(t)
      val hlr = ev1(hl)
      ev2(hlr)
    }
  }

  sealed trait HReverseResult[A, C, R] {
    def apply(a: A, c: C): R
  }

  implicit def hNilReverseResult[C <: HList]: HReverseResult[HNil, C, C] =
    new HReverseResult[HNil, C, C] {
      override def apply(a: HNil, c: C) = c
    }

  implicit def hConsReverseAppendResult[H, T <: HList, CP <: HList, R <: HList](
      implicit ev: HReverseResult[T, H :+: CP, R]): HReverseResult[H :+: T, CP, R] =
    new HReverseResult[H :+: T, CP, R] {
      override def apply(a: H :+: T, c: CP) = ev(a.tail, a.head :+: c)
    }
}
